package com.akbarsya.wheretoeat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.domain.usecase.FetchPreferenceTagUseCase
import com.akbarsya.wheretoeat.model.PreferenceTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesChooserViewModel @Inject constructor(
    private val fetchPreferenceTagUseCase: FetchPreferenceTagUseCase
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
}