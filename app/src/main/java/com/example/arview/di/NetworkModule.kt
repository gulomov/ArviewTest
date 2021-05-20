package com.example.arview.di

import com.example.arview.network.Api
import com.example.arview.network.pojo.ErrorResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.schedulers.Schedulers.single
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL: String = "https://api.twitch.tv/kraken/"
val networkModule = module {
    single<Api> {
        val retrofit = get<Retrofit>()
        retrofit.create(Api::class.java)

    }
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token: String = "ea0bkr981kos4crw8gfstoqxp2tp0i"
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Accept", "application/vnd.twitchtv.v5+json")
                    request.addHeader("Client-ID", "v36n23dm8c9d46n886ds9rd9czf1qz")
                    if (token != "")
                        request.addHeader("Authorization", "Bearer $token")
                    return@addInterceptor chain.proceed(request.build())
                } catch (e: Throwable) {

                }
                return@addInterceptor chain.proceed(chain.request())
            }
        clientBuilder.build()
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(ChuckInterceptor(get()))
        }
        clientBuilder.build()
    }
    factory<Converter<ResponseBody, ErrorResponse>> {
        get<Retrofit>().responseBodyConverter(
            ErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )
    }
}