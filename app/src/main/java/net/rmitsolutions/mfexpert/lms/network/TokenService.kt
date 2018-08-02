package net.rmitsolutions.mfexpert.lms.network

import net.rmitsolutions.mfexpert.lms.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TokenService {
    @PublishedApi
    internal var retrofit: Retrofit

    init {
        val httpClient = OkHttpClient.Builder()
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
    }

    inline fun <reified T> getService(): T {
        return retrofit.create(T::class.java)
    }
}