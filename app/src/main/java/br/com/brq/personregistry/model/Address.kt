package br.com.brq.personregistry.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        @SerializedName("cep")
        var cep: String? = "",

        @SerializedName("logradouro")
        var street: String? = "",

        @SerializedName("complemento")
        var complement: String? ="",

        @SerializedName("bairro")
        var neighborhood: String? = "",

        var number: String? = "",

        @SerializedName("localidade")
        var city: String? = "",

        @SerializedName("uf")
        var state: String? = ""
) : Parcelable
