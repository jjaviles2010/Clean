package com.fiap18Mob.clean.repository

import android.content.ClipData
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryRemote (val firebaseAuth: FirebaseAuth,
    val firebaseDB: FirebaseDatabase) : UserRepository {

    private val firebaseReferenceUserNode = "users"
    private val firebaseReferenceCleaningServiceNode = "cleaningServices"


    override suspend fun insertUser(user: User, onComplete: (Boolean?) -> Unit,
                                    onError: (Throwable?) -> Unit) {
        firebaseDB.getReference(firebaseReferenceUserNode)
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
        firebaseDB.getReference(firebaseReferenceUserNode)
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

    fun getUsersByProfile(profile: String, onComplete: (List<User>?) -> Unit, onError: (Throwable?) -> Unit): List<User> {

        var usersResult: MutableList<User> = mutableListOf()

        firebaseDB.getReference(firebaseReferenceUserNode)
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
                    onComplete(usersResult)
                }
            })
        return usersResult;
    }


    fun insertCleaningService(
        cleaningService: CleaningService, onComplete: (Boolean?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        firebaseDB.getReference("$firebaseReferenceUserNode/${firebaseAuth.currentUser?.uid}/$firebaseReferenceCleaningServiceNode")
            .push()
            .setValue(cleaningService)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(true)
                } else {
                    onError(it.exception)
                }
            }
    }

    fun updateCleaningService(
        cleaningService: CleaningService, onComplete: (Boolean?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        firebaseDB.getReference("$firebaseReferenceUserNode/${firebaseAuth.currentUser?.uid}/$firebaseReferenceCleaningServiceNode/${cleaningService.id}")
            .setValue(cleaningService)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(true)
                } else {
                    onError(it.exception)
                }
            }
    }

    fun deleteCleaningService(
        cleaningService: CleaningService, onComplete: (Boolean?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        firebaseDB.getReference("$firebaseReferenceUserNode/${firebaseAuth.currentUser?.uid}/$firebaseReferenceCleaningServiceNode/${cleaningService.id}")
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(true)
                } else {
                    onError(it.exception)
                }
            }
    }

    fun getCleaningServices(
        onComplete: (ArrayList<CleaningService>?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        var servicesResult: ArrayList<CleaningService> = ArrayList()
        var service: CleaningService = CleaningService()

        firebaseDB.getReference("$firebaseReferenceUserNode/${firebaseAuth.currentUser?.uid}/$firebaseReferenceCleaningServiceNode")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onError(Throwable("Erro ao carregar os serviços"))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapshot in dataSnapshot?.getChildren()) {
                        service = dataSnapshot.getValue(CleaningService::class.java)!!
                        service.id = dataSnapshot.key ?: ""
                        servicesResult.add(service)
                    }
                    onComplete(servicesResult)
                }
            })
    }

    fun getCleanerByCpf(cpf: String, onComplete: (User?) -> Unit, onError: (Throwable?) -> Unit) {

        var userResult: User = User()

        firebaseDB.getReference(firebaseReferenceUserNode)
            .orderByChild("cpf")
            .equalTo(cpf)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    onError(Throwable("Erro ao carregar dados da diarista"))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapshot in dataSnapshot?.getChildren()) {
                        userResult = dataSnapshot.getValue(User::class.java)!!
                    }

                    onComplete(userResult)
                }
            })
    }

}