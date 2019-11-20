package com.fiap18Mob.clean.repository

import android.content.ClipData
import com.fiap18Mob.clean.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryRemote (val firebaseAuth: FirebaseAuth,
    val firebaseDB: FirebaseDatabase) : UserRepository {

    private val firebaseReferenceNode = "users"


    override suspend fun insertUser(user: User, onComplete: (Boolean?) -> Unit,
                                    onError: (Throwable?) -> Unit) {
        firebaseDB.getReference(firebaseReferenceNode)
            .child(firebaseAuth.currentUser?.uid ?: "")
            .setValue(user)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    onComplete(true)
                } else {
                    onError(it.exception)
                }
            }
    }

    override fun getUser(cpf: String): User {
        var userResult: User = User()
        firebaseDB.getReference(firebaseReferenceNode)
            .child(firebaseAuth.currentUser?.uid ?: "")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    print(error.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userResult = dataSnapshot?.getValue(User::class.java)!!
                }
            })
        return userResult
    }


    fun createUser(user: User, password: String, onComplete: (Boolean?) -> Unit,
                   onError: (Throwable?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onComplete(true)
                    } else {
                        onError(it.exception)
                    }
            }
    }

    fun getUsersByProfile(profile: String, onComplete: (Boolean?) -> Unit, onError: (Throwable?) -> Unit): List<User> {

        var usersResult: MutableList<User> = mutableListOf()

        firebaseDB.getReference(firebaseReferenceNode)
            .orderByChild("profile")
            .equalTo("CLEANER")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    print(error.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapshot in dataSnapshot?.getChildren()) {
                        usersResult.add(dataSnapshot.getValue(User::class.java)!!)
                    }
                }
            })

        return  usersResult
    }

}