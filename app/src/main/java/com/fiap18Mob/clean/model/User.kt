package com.fiap18Mob.clean.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey @ColumnInfo(name = "cpf") var cpf: String = "",
    var nome: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var zipCode: String = "",
    var street: String = "",
    var number: Int = 0,
    var complement: String = "",
    var neighborhood: String = "",
    var city: String = "",
    var uf: String = "",
    var profile: String = "",
    var hourValue: Double = 0.0
)