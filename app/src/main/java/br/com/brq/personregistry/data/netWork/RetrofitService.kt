package br.com.brq.personregistry.data.netWork

import br.com.brq.personregistry.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    private lateinit var retrofit: Retrofit

    companion object {
        var baseUrl: String  = "https://viacep.com.br/ws/"
    }

    private fun getRetrofit(): Retrofit {

            // configurações da conexão
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            httpClient.connectTimeout(30, TimeUnit.SECONDS)
            httpClient.writeTimeout(30, TimeUnit.SECONDS)

            // Se for Debug habilitamos os logs
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(httpLoggingInterceptor)
                httpClient.addNetworkInterceptor(StethoInterceptor())
            }

            // Inicializamos o retrofit
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

        return retrofit
    }

    // Retornamos a instancia da API criada com o retrofit
    fun getApiService(): Api {
        return getRetrofit().create(Api::class.java)
    }

}