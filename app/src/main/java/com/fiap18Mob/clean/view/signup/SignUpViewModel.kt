package com.fiap18Mob.clean.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.model.Address
import com.fiap18Mob.clean.repository.AddressRepository

class SignUpViewModel(val addressRepository: AddressRepository) : ViewModel() {

    val address: MutableLiveData<Address> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()

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



}