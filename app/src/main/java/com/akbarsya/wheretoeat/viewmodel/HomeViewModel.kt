package com.akbarsya.wheretoeat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.data.entity.request.NominatimReverseGeoRequest
import com.akbarsya.wheretoeat.domain.entity.NominatimReverseGeoEntity
import com.akbarsya.wheretoeat.domain.entity.PlaceResultEntity
import com.akbarsya.wheretoeat.domain.usecase.FetchPlaceByTagUseCase
import com.akbarsya.wheretoeat.domain.usecase.FetchPlaceForYouUseCase
import com.akbarsya.wheretoeat.domain.usecase.FetchPreferenceTagUseCase
import com.akbarsya.wheretoeat.domain.usecase.ReverseGeocodeUseCase
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPreferenceTagUseCase: FetchPreferenceTagUseCase,
    private val reverseGeocodeUseCase: ReverseGeocodeUseCase,
    private val fetchPlaceForYouUseCase: FetchPlaceForYouUseCase,
    private val fetchPlaceByTagUseCase: FetchPlaceByTagUseCase
): ViewModel() {
    val preferenceTagLiveData: MutableLiveData<Resource<List<PreferenceTag>>> = MutableLiveData()

    fun fetchTags() {
        viewModelScope.launch {
            val response = fetchPreferenceTagUseCase.invoke()

            when(response.status) {
                ResourceState.SUCCESS -> {
                    val data = response.data.map { entity ->
                        PreferenceTag(
                            id = entity.id,
                            icon = entity.icon,
                            text = entity.label
                        )
                    }

                    preferenceTagLiveData.postValue(
                        Resource.success(data)
                    )
                }
                ResourceState.FAILED -> {
                    preferenceTagLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    preferenceTagLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }

    val reverseGeocodeLiveData: MutableLiveData<Resource<NominatimReverseGeoEntity>> = MutableLiveData()

    fun reverseGeoCode(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val response = reverseGeocodeUseCase.
            invoke(
                NominatimReverseGeoRequest(
                    lat = latitude.toString(),
                    lon = longitude.toString()
                )
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    reverseGeocodeLiveData.postValue(
                        response
                    )
                }
                ResourceState.FAILED -> {
                    reverseGeocodeLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    reverseGeocodeLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }

    val fetchPlaceLiveData: MutableLiveData<Resource<PlaceResultEntity>> = MutableLiveData()

    fun fetchPlaceForYou(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            val response: Resource<PlaceResultEntity> = fetchPlaceForYouUseCase.invoke(
                firebaseUid = Firebase.auth.currentUser?.uid.orEmpty(),
                latitude = latitude,
                longitude = longitude
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    fetchPlaceLiveData.postValue(
                        response
                    )
                }
                ResourceState.FAILED -> {
                    fetchPlaceLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    fetchPlaceLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }

    fun fetchPlaceByTag(
        tagId: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            val response: Resource<PlaceResultEntity> = fetchPlaceByTagUseCase.invoke(
                tagId = tagId,
                firebaseUid = Firebase.auth.currentUser?.uid.orEmpty(),
                latitude = latitude,
                longitude = longitude
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    fetchPlaceLiveData.postValue(
                        response
                    )
                }
                ResourceState.FAILED -> {
                    fetchPlaceLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    fetchPlaceLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }
}