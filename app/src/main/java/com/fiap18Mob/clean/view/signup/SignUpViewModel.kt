package com.fiap18Mob.clean.view.signup

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fiap18Mob.clean.model.Address
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.AddressRepository
import com.fiap18Mob.clean.repository.UserRepository
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import com.fiap18Mob.clean.utils.MessageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.*

class SignUpViewModel(application: Application,
                      val addressRepository: AddressRepository,
                      private val userLocalRepository: UserRepository,
                      val userRemoteRepository: UserRepositoryRemote) : AndroidViewModel(application) {

    val address: MutableLiveData<Address> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()
    val isUserSignUp: MutableLiveData<Boolean> = MutableLiveData()
    val isUserCreated: MutableLiveData<Boolean> = MutableLiveData()
    val latitude: MutableLiveData<Double> = MutableLiveData()
    val longitude: MutableLiveData<Double> = MutableLiveData()
    val isGeoInfoPopulated: MutableLiveData<Boolean> = MutableLiveData()

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
        userLocalRepository.insertUser(user, onComplete = {}, onError = {})
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
                isLoading.value = false
                messageError.value = it?.message
            }
            )
    }

    fun insertUserRemote(user: User) = viewModelScope.launch (Dispatchers.IO) {
        userRemoteRepository.insertUser(
            user,
            onComplete = {
                isUserCreated.value = true
                isLoading.value = false
            },
            onError = {
                isLoading.value = false
                isUserCreated.value = false
                messageError.value = it?.message
            })
    }


    fun getLatitudeLongitude(address: String) {

        val geocoder = Geocoder(getApplication<Application>().applicationContext, Locale.getDefault())
        val addressGeocoding = geocoder.getFromLocationName(address, 1)

        latitude.value = addressGeocoding.first().latitude
        longitude.value = addressGeocoding.first().longitude

        isGeoInfoPopulated.value = true
    }

}