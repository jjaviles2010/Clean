package com.fiap18Mob.clean.view.serviceDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceDetailViewModel(val userRemoteRepository: UserRepositoryRemote) : ViewModel() {
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()
    val cleanerInfo: MutableLiveData<User> = MutableLiveData()
    val cleaningServiceUpdated: MutableLiveData<Boolean> = MutableLiveData()

    fun getCleanerInfo(cpf: String) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.getCleanerByCpf(
            cpf,
            onComplete = {
                isLoading.value = false
                cleanerInfo.value = it
            },
            onError = {
                isLoading.value = false
                messageError.value = it?.message
            }
        )
    }

    fun updateCleaningService(cleaningService: CleaningService) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.updateCleaningService(
            cleaningService,
            onComplete = {
                isLoading.value = false
                cleaningServiceUpdated.value = true
            },
            onError = {
                isLoading.value = false
                messageError.value = it?.message
                cleaningServiceUpdated.value = false
            }
        )
    }
}