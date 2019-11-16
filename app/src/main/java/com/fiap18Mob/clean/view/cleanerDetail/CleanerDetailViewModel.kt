package com.fiap18Mob.clean.view.cleanerDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CleanerDetailViewModel(val userRemoteRepository: UserRepositoryRemote) : ViewModel() {

    val cleaningService: MutableLiveData<CleaningService> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()
    val cleaningServiceScheduled: MutableLiveData<Boolean> = MutableLiveData()

    fun insertCleaningService(cleaningService: CleaningService) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.insertCleaningService(
            cleaningService,
            onComplete = {
                isLoading.value = false
                cleaningServiceScheduled.value = true
            },
            onError = {
                isLoading.value = false
                messageError.value = it?.message
                cleaningServiceScheduled.value = false
            }
        )
    }

}