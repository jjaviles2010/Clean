package com.fiap18Mob.clean.api

import com.fiap18Mob.clean.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressService {

    @GET("/ws/{cep}/json/")
    fun getAddress(
        @Path("cep")cep: String
    ) : Call<Address>
}