package com.fiap18Mob.clean.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    var alreadyAuth: MutableLiveData<Boolean> = MutableLiveData()
    var authorized: MutableLiveData<Boolean> = MutableLiveData()
    var exceptionMessage: MutableLiveData<String> = MutableLiveData()

    fun initialAuth() {
        mAuth = FirebaseAuth.getInstance()

        //ATENÇÃO: remover esta linha. Ela está ai para forçar um login todas as vezes que o app é aberto
        //Perguntar para o Heider, porque mesmo que exclua ou inative o usuário no Firebase, o login continua ativo.
        mAuth.signOut()

        if (mAuth.currentUser != null) {
            alreadyAuth.value = true
            return
        }
        alreadyAuth.value = false
    }

    fun auth(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            authorized.value = it.isSuccessful
            if (!it.isSuccessful) {
                exceptionMessage.value = it.exception?.message
            }
        }
    }

}