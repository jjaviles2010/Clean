package com.fiap18Mob.clean.view.listServices

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListServicesViewModel (val userRemoteRepository: UserRepositoryRemote) : ViewModel() {

    val messageError: MutableLiveData<String> = MutableLiveData()
    val services: MutableLiveData<ArrayList<CleaningService>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: MutableLiveData<Boolean> = MutableLiveData()

    fun getCleaningServices() {
        isLoading.value = true
        userRemoteRepository.getCleaningServices(
            onComplete = {
                isLoading.value = false
                services.value = it
                messageError.value = ""
            },
            onError = {
                services.value = ArrayList()
                messageError.value = it?.message
                isLoading.value = false
            }
        )
    }

    fun deleteCleaningServices(cleaningService: CleaningService) = viewModelScope.launch (
        Dispatchers.IO) {
        userRemoteRepository.deleteCleaningService(cleaningService,
            onComplete = {
                isDeleted.value = true
                messageError.value = ""
            },
            onError = {
                messageError.value = it?.message
                isDeleted.value = false
            }
        )
    }

}