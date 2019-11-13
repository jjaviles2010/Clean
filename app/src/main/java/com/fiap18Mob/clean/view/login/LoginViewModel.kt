package com.fiap18Mob.clean.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel (val mAuth: FirebaseAuth): ViewModel() {

    var alreadyAuth: MutableLiveData<Boolean> = MutableLiveData()
    var authorized: MutableLiveData<Boolean> = MutableLiveData()
    var messageError: MutableLiveData<String> = MutableLiveData()

    fun initialAuth() {
        //ATENÇÃO: remover esta linha. Ela está ai para forçar um login todas as vezes que o app é aberto
        //Perguntar para o Heider, porque mesmo que exclua ou inative o usuário no Firebase, o login continua ativo.
        mAuth.signOut()

        if (mAuth.currentUser != null) {
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
                messageError.value = it.exception?.message
            }
        }
    }
}