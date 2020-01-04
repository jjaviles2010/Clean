package com.fiap18Mob.clean.view.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.UserRepositoryRemote

class MapsViewModel(val userLocalRepository: UserRepositoryRemote) : ViewModel() {

    val messageError: MutableLiveData<String> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getUserList() {
        isLoading.value = true
        userLocalRepository.getUsersByProfile(
            "CLEANER",
            onComplete = {
                isLoading.value = false
                users.value = it
                messageError.value = ""
            },
            onError = {
                messageError.value = it?.message
                isLoading.value = false
            }
        )
    }


}