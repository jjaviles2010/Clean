package com.fiap18Mob.clean.repository

import com.fiap18Mob.clean.api.AddressService
import com.fiap18Mob.clean.model.Address
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepositoryImpl(val addressService: AddressService) : AddressRepository {

    override fun getAddress(
        cep: String,
        onComplete: (Address?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        addressService.getAddress(cep)
            .enqueue(object : Callback<Address> {
                override fun onFailure(call: Call<Address>, t: Throwable) {
                    onError(t)
                }

                override fun onResponse(call: Call<Address>, response: Response<Address>) {
                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError(Throwable("Erro na requisição"))
                    }
                }
            })
    }


}