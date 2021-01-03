package com.joseph.mvvmbasic.data.api

import android.util.Log
import androidx.core.os.BuildCompat
import com.google.gson.internal.GsonBuildConfig
import com.joseph.mvvmbasic.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

const val API_KEY = BuildConfig.TMDB_API_KEY
const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_URL = "https://image.tmdb.org/t/p/original"

object TheMovieDbClient {
    fun getClient(): TheMovieDbInterface {

        val requestInterceptor = Interceptor { chain ->
            // Interceptor take only one argument which is a lambda function so parenthesis can be omitted

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)   //explicitly return a value from whit @ annotation. lambda always returns the value of the last expression implicitly
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDbInterface::class.java)

    }
}