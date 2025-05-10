package com.cokgyzlar.hotpot.di

import com.cokgyzlar.hotpot.data.AuthInterceptor
import com.cokgyzlar.hotpot.data.auth.login.LoginApi
import com.cokgyzlar.hotpot.data.auth.login.LoginRepository
import com.cokgyzlar.hotpot.data.auth.login.LoginRepositoryImpl
import com.cokgyzlar.hotpot.data.auth.premium.PremiumApi
import com.cokgyzlar.hotpot.data.auth.premium.PremiumRepository
import com.cokgyzlar.hotpot.data.auth.premium.PremiumRepositoryImpl
import com.cokgyzlar.hotpot.data.auth.register.RegisterApi
import com.cokgyzlar.hotpot.data.auth.register.RegisterRepository
import com.cokgyzlar.hotpot.data.auth.register.RegisterRepositoryImpl
import com.cokgyzlar.hotpot.data.meal.MealApi
import com.cokgyzlar.hotpot.data.meal.MealRepository
import com.cokgyzlar.hotpot.data.meal.MealRepositoryImpl
import com.cokgyzlar.hotpot.data.openai.OpenAIApi
import com.cokgyzlar.hotpot.data.openai.OpenAIRepository
import com.cokgyzlar.hotpot.data.openai.OpenAIRepositoryImpl
import com.cokgyzlar.hotpot.data.posts.comments.CommentsApi
import com.cokgyzlar.hotpot.data.posts.comments.CommentsRepository
import com.cokgyzlar.hotpot.data.posts.comments.CommentsRepositoryImpl
import com.cokgyzlar.hotpot.data.posts.favorites.FavoritesApi
import com.cokgyzlar.hotpot.data.posts.favorites.FavoritesRepository
import com.cokgyzlar.hotpot.data.posts.favorites.FavoritesRepositoryImpl
import com.cokgyzlar.hotpot.data.posts.posts.PostsApi
import com.cokgyzlar.hotpot.data.posts.posts.PostsRepository
import com.cokgyzlar.hotpot.data.posts.posts.PostsRepositoryImpl
import com.cokgyzlar.hotpot.data.profile.ProfileRepository
import com.cokgyzlar.hotpot.data.profile.ProfileRepositoryImpl
import com.cokgyzlar.hotpot.data.profile.ProfilelApi
import com.cokgyzlar.hotpot.ui.viewmodels.MainActivityVM
import com.prowheelxrassistv01.data.AppStorage
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.101.16:8080"

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

    single { get<Retrofit>(named("interceptorRetrofit")).create(FavoritesApi::class.java) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }

    single { get<Retrofit>(named("interceptorRetrofit")).create(CommentsApi::class.java) }
    single<CommentsRepository> { CommentsRepositoryImpl(get()) }

    single { get<Retrofit>(named("interceptorRetrofit")).create(MealApi::class.java) }
    single<MealRepository> { MealRepositoryImpl(get()) }

    single { get<Retrofit>(named("interceptorRetrofit")).create(ProfilelApi::class.java) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single { get<Retrofit>(named("interceptorRetrofit")).create(PremiumApi::class.java) }
    single<PremiumRepository> { PremiumRepositoryImpl(get()) }

    single(named("openAIHttpClient")) {
        OkHttpClient.Builder()
            //.addInterceptor()
            .build()
    }

    // Retrofit with interceptor
    single(named("openAIRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(get(named("openAIHttpClient")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>(named("openAIRetrofit")).create(OpenAIApi::class.java) }
    single<OpenAIRepository> { OpenAIRepositoryImpl(get()) }

    viewModel { MainActivityVM() }

}
