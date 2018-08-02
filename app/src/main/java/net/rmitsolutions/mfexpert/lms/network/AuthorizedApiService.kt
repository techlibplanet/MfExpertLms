package net.rmitsolutions.mfexpert.lms.network

import com.google.gson.GsonBuilder
import net.rmitsolutions.mfexpert.lms.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthorizedApiService(accessToken: String) {
    internal lateinit var retrofit: Retrofit


    init {
        this.build(accessToken)
    }

    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun build(accessToken: String) {
        retrofit = buildRetrofit(accessToken)
    }

    companion object {

        fun buildRetrofit(accessToken: String): Retrofit {
            val httpClient = OkHttpClient.Builder()
                    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val original = chain.request()

                        val requestBuilder = original.newBuilder()
                                .header("Authorization", accessToken)

                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }

            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

            return Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
    }


}