package com.akbarsya.wheretoeat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.domain.entity.PlaceResultEntity
import com.akbarsya.wheretoeat.domain.usecase.FetchPlaceByTagUseCase
import com.akbarsya.wheretoeat.domain.usecase.FetchPlaceForYouUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceMapViewModel @Inject constructor(
    private val fetchPlaceForYouUseCase: FetchPlaceForYouUseCase,
    private val fetchPlaceByTagUseCase: FetchPlaceByTagUseCase
): ViewModel() {
    val fetchPlaceLiveData: MutableLiveData<Resource<PlaceResultEntity>> = MutableLiveData()

    fun fetchPlace() {
//        viewModelScope.launch {
//            val response: Resource<List<DirectusPlaceEntity>> = fetchPlaceUseCase.invoke()
//
//            when(response.status) {
//                ResourceState.SUCCESS -> {
//                    fetchPlaceLiveData.postValue(
//                        response
//                    )
//                }
//                ResourceState.FAILED -> {
//                    fetchPlaceLiveData.postValue(
//                        response.convertError()
//                    )
//                }
//                ResourceState.LOADING -> {
//                    fetchPlaceLiveData.postValue(
//                        Resource.loading()
//                    )
//                }
//            }
//        }
    }
}