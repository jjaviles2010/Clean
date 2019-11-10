package com.fiap18Mob.clean.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.fiap18Mob.clean.dao.UserDao
import com.fiap18Mob.clean.model.User

class UserRepositoryLocal (val userDao: UserDao) : UserRepository {

    @WorkerThread
    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    override fun getUser(cpf: String): User {
        return userDao.getUser(cpf)
    }
}