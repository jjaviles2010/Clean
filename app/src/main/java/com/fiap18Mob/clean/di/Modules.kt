package com.fiap18Mob.clean.di

import androidx.room.Room
import com.fiap18Mob.clean.api.AddressService
import com.fiap18Mob.clean.dao.CleanRoomDatabase
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.*
import com.fiap18Mob.clean.utils.RemoteConfig
import com.fiap18Mob.clean.utils.URLProvider
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailViewModel
import com.fiap18Mob.clean.view.forgotpassword.ForgotPasswordViewModel
import com.fiap18Mob.clean.view.listServices.ListServicesViewModel
import com.fiap18Mob.clean.view.login.LoginViewModel
import com.fiap18Mob.clean.view.serviceDetail.ServiceDetailViewModel
import com.fiap18Mob.clean.view.signup.SignUpViewModel
import com.fiap18Mob.clean.view.usersList.UsersListViewModel
import com.fiap18mob.mylib.CustomToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get(), get()) }
    viewModel { CleanerDetailViewModel(get()) }
    viewModel { ListServicesViewModel(get()) }
    viewModel { UsersListViewModel(get()) }
    viewModel { ServiceDetailViewModel(get()) }
}

val uiModule = module {
    factory { User() }
    factory { CleaningService() }
    factory { CustomToast() }
}

val repositoryModule = module {
    single<AddressRepository> { AddressRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryLocal(get()) }
    single<UserRepositoryRemote> { UserRepositoryRemote(get(),get()) }
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

    single {
        FirebaseDatabase.getInstance()
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseInstanceId.getInstance()
    }

    single {
        RemoteConfig
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