package com.fiap18Mob.clean.di

import com.fiap18Mob.clean.api.AddressService
import com.fiap18Mob.clean.repository.AddressRepository
import com.fiap18Mob.clean.repository.AddressRepositoryImpl
import com.fiap18Mob.clean.utils.URLProvider
import com.fiap18Mob.clean.view.signup.SignUpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val viewModelModule = module {
    viewModel { SignUpViewModel(get()) }
}

val repositoryModule = module {
    single<AddressRepository> { AddressRepositoryImpl(get()) }
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