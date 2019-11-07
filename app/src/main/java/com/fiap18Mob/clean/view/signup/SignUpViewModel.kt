package com.fiap18Mob.clean.view.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.Address
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.AddressRepository
import com.fiap18Mob.clean.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application,
                      val addressRepository: AddressRepository,
                      private val userRepository: UserRepository) : AndroidViewModel(application) {

    val address: MutableLiveData<Address> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()

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


    fun getUser(cpf: String) {
        user.value = userRepository.getUser(cpf) as? User
    }

    fun insertUser(user: User) = viewModelScope.launch (Dispatchers.IO) {
        userRepository.insertUser(user)
    }

}