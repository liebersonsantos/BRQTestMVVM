package br.com.brq.personregistry.data.netWork

import br.com.brq.personregistry.model.Address
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("{cep}/json/")
    fun getAddress (@Path ("cep")cep: String): Single<Address>
}