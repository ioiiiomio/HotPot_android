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
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://172.20.10.12:8080"

val appModule = module {

    single { AppStorage.getInstance(androidContext()) }

    single(named("noAuthOkHttpClient")) {
        OkHttpClient.Builder()
            .build()
    }

    // Retrofit without interceptor
    single(named("noInterceptorRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("noAuthOkHttpClient")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>(named("noInterceptorRetrofit")).create(LoginApi::class.java) }
    single<LoginRepository> { LoginRepositoryImpl(get()) }


    // OkHttpClient with interceptor
    single(named("authOkHttpClient")) {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(get(), get()))
            .build()
    }

    // Retrofit with interceptor
    single(named("interceptorRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("authOkHttpClient")))  // Uses OkHttpClient with AuthInterceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    single { get<Retrofit>(named("interceptorRetrofit")).create(RegisterApi::class.java) }
    single<RegisterRepository> { RegisterRepositoryImpl(get()) }

    single { get<Retrofit>(named("interceptorRetrofit")).create(PostsApi::class.java) }
    single<PostsRepository> { PostsRepositoryImpl(get()) }





}
