package com.fiap18Mob.clean.di

import androidx.room.Room
import com.fiap18Mob.clean.api.AddressService
import com.fiap18Mob.clean.dao.CleanRoomDatabase
import com.fiap18Mob.clean.repository.AddressRepository
import com.fiap18Mob.clean.repository.AddressRepositoryImpl
import com.fiap18Mob.clean.repository.UserRepository
import com.fiap18Mob.clean.repository.UserRepositoryLocal
import com.fiap18Mob.clean.utils.URLProvider
import com.fiap18Mob.clean.view.signup.SignUpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val viewModelModule = module {
    viewModel { SignUpViewModel(get(), get(), get()) }
}

val repositoryModule = module {
    single<AddressRepository> { AddressRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryLocal(get()) }
}

val dbModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CleanRoomDatabase::class.java,
            "clean_database"
        ).build()
    }

    single {
        get<CleanRoomDatabase>().userDao()
    }
}

val networkModule = module {
    single { createNetworkClient(get(named("baseURL"))
    ).create(AddressService::class.java) }
    single(named("baseURL")) { URLProvider.baseURL }
}

private fun createNetworkClient(baseURL: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}