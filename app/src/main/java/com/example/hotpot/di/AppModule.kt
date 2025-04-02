package com.example.hotpot.di

import com.example.hotpot.data.AuthInterceptor
import com.example.hotpot.data.auth.login.LoginApi
import com.example.hotpot.data.auth.login.LoginRepository
import com.example.hotpot.data.auth.login.LoginRepositoryImpl
import com.example.hotpot.data.auth.register.RegisterApi
import com.example.hotpot.data.auth.register.RegisterRepository
import com.example.hotpot.data.auth.register.RegisterRepositoryImpl
import com.example.hotpot.data.posts.posts.PostsApi
import com.example.hotpot.data.posts.posts.PostsRepository
import com.example.hotpot.data.posts.posts.PostsRepositoryImpl
import com.prowheelxrassistv01.data.AppStorage
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module
import retrofit2.Retrofit

const val BASE_URL = "http://172.20.10.12:8080"

val appModule = module {
    single { AppStorage.getInstance(androidContext()) }

    single { AuthInterceptor(get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    single {get<Retrofit>().create(RegisterApi::class.java)}
    single<RegisterRepository> {RegisterRepositoryImpl(get())}

    single {get<Retrofit>().create(LoginApi::class.java)}
    single<LoginRepository> {LoginRepositoryImpl(get())}

    single {get<Retrofit>().create(PostsApi::class.java)}
    single<PostsRepository> {PostsRepositoryImpl(get())}

}