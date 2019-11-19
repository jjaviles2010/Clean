package com.fiap18Mob.clean.view.usersList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.UserRepositoryRemote

class RecyclerViewModel(val userLocalRepository: UserRepositoryRemote) : ViewModel() {

    val messageError: MutableLiveData<String> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()


    fun getUserList() {
        isLoading.value = true
        userLocalRepository.getUsersByProfile(
            "CLEANER",
            onComplete = {

            },
            onError = {
                messageError.value = it?.message
                isLoading.value = false
            }
        )
    }


}