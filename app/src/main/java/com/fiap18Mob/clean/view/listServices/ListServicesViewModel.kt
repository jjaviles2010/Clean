package com.fiap18Mob.clean.view.listServices

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.repository.UserRepositoryRemote

class ListServicesViewModel (val userRemoteRepository: UserRepositoryRemote) : ViewModel() {

    val messageError: MutableLiveData<String> = MutableLiveData()
    val services: MutableLiveData<List<CleaningService>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getCleaningServices() {
        isLoading.value = true
        userRemoteRepository.getCleaningServices(
            onComplete = {
                isLoading.value = false
                services.value = it
                messageError.value = ""
            },
            onError = {
                services.value = emptyList()
                messageError.value = it?.message
                isLoading.value = false
            }
        )
    }

}