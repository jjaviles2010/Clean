package com.fiap18Mob.clean.view.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.Address
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.AddressRepository
import com.fiap18Mob.clean.repository.UserRepository
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application,
                      val addressRepository: AddressRepository,
                      private val userLocalRepository: UserRepository,
                      val userRemoteRepository: UserRepositoryRemote) : AndroidViewModel(application) {

    val address: MutableLiveData<Address> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()
    val isUserSignUp: MutableLiveData<Boolean> = MutableLiveData()

    fun getAddress(cep: String) {
        isLoading.value = true

        addressRepository.getAddress(
            cep,
            onComplete = {
                isLoading.value = false
                address.value = it
            },
            onError = {
                isLoading.value = false
                messageError.value = it?.message
            })
    }


    fun getUserLocally(cpf: String) = viewModelScope.launch (Dispatchers.IO) {
        user.postValue(userLocalRepository.getUser(cpf))
    }

    fun insertUserLocally(user: User) = viewModelScope.launch (Dispatchers.IO) {
        userLocalRepository.insertUser(user)
    }

    fun getUserRemote(cpf: String) {
        user.value = userRemoteRepository.getUser(cpf)
    }

    fun signUpUser(user: User, password: String) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.createUser(
            user,
            password,
            onComplete = {
                isUserSignUp.value = it
            },
            onError = {
                messageError.value = it?.message
            }
            )
    }

    fun insertUserRemote(user: User) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.insertUser(user)
    }

}