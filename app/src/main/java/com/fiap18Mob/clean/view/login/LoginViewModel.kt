package com.fiap18Mob.clean.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.fiap18Mob.clean.utils.DatabaseUtil
import com.fiap18Mob.clean.utils.RemoteConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId

class LoginViewModel (val mAuth: FirebaseAuth,
                      val firebaseInstanceId: FirebaseInstanceId,
                      val remoteConfig: RemoteConfig): ViewModel() {

    var alreadyAuth: MutableLiveData<Boolean> = MutableLiveData()
    var authorized: MutableLiveData<Boolean> = MutableLiveData()
    var messageError: MutableLiveData<String> = MutableLiveData()
    var clientRegistrationActive: MutableLiveData<Boolean> = MutableLiveData()
    var cleanerRegistrationActive: MutableLiveData<Boolean> = MutableLiveData()

    fun initialAuth() {
        //ATENÇÃO: remover esta linha. Ela está ai para forçar um login todas as vezes que o app é aberto
        //Perguntar para o Heider, porque mesmo que exclua ou inative o usuário no Firebase, o login continua ativo.
        mAuth.signOut()

        if (mAuth.currentUser != null) {
            firebaseSaveToken()
            alreadyAuth.value = true
            return
        }
        alreadyAuth.value = false
    }

    fun auth(emailAddress: String, password: String) {
        mAuth.signInWithEmailAndPassword(
            emailAddress,
            password
        ).addOnCompleteListener {
            authorized.value = it.isSuccessful
            if (!it.isSuccessful) {
                firebaseSaveToken()
                messageError.value = it.exception?.message
            }
        }

    }

    fun checkRemoteConfig() {
        remoteConfig.remoteConfigFetch()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    RemoteConfig.getFirebaseRemoteConfig().activateFetched()

                    val isClientRegistrationActive = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("is_client_registration_active")
                        .toInt()
                    if (isClientRegistrationActive == 1) {
                        clientRegistrationActive.value = true
                    } else {
                        clientRegistrationActive.value = false
                    }

                    val isCleanerRegistrationActive = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("is_cleaner_registration_active")
                        .toInt()
                    if (isCleanerRegistrationActive == 1) {
                        cleanerRegistrationActive.value = true
                    } else {
                        cleanerRegistrationActive.value = false
                    }
                }
            }
    }


    private fun firebaseSaveToken() {
        firebaseInstanceId.instanceId.addOnSuccessListener {
                instanceIdResult ->
            val newToken = instanceIdResult.token
            DatabaseUtil.saveToken(newToken)
        }
    }
}