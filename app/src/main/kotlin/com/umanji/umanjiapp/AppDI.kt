package com.umanji.umanjiapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.umanji.umanjiapp.common.util.DateUtils
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.common.util.RestApiErrorUtils
import com.umanji.umanjiapp.data.executor.JobExecutor
import com.umanji.umanjiapp.data.network.ApiHeaders
import com.umanji.umanjiapp.data.network.ApiResponse
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.data.repository.*
import com.umanji.umanjiapp.data.util.MediaUtils
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.*
import com.umanji.umanjiapp.ui.UIThread
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Dagger [Component] for App.
 */
@Singleton
@Component(modules = [AppModule::class, ApiModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(target: MyApplication)
    fun getContext(): Context
    fun getLocationProvider(): ReactiveLocationProvider
    fun getNetworkUtils(): NetworkUtils
    fun getDateUtils(): DateUtils
    fun getMediaUtils(): MediaUtils
    fun getRestApiErrorUtils(): RestApiErrorUtils
    fun getApiHeaders(): ApiHeaders
    fun getThreadExecutor(): ThreadExecutor
    fun getPostExecutionThread(): PostExecutionThread
    fun getUserRepository(): UserRepository
    fun getAuthRepository(): AuthRepository
    fun getPostRepository(): PostRepository
    fun getGeoRepository(): GeoRepository
    fun getGroupRepository(): GroupRepository
    fun getBankRepository(): BankRepository
}


@Module
class AppModule(private val app: MyApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread = uiThread

    @Provides
    @Singleton
    fun provideLocationProvider(context: Context): ReactiveLocationProvider =
            ReactiveLocationProvider(context)
}


@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userRepository: UserDataRepository): UserRepository = userRepository

    @Provides
    @Singleton
    fun provideAuthRepository(authRepository: AuthDataRepository): AuthRepository = authRepository

    @Provides
    @Singleton
    fun providePostRepository(postRepository: PostDataRepository): PostRepository = postRepository

    @Provides
    @Singleton
    fun provideGeoRepository(geoRepository: GeoDataRepository): GeoRepository = geoRepository

    @Provides
    @Singleton
    fun provideGroupRepository(groupRepository: GroupDataRepository): GroupRepository =
            groupRepository

    @Provides
    @Singleton
    fun provideBankRepository(bankDataRepository: BankDataRepository): BankRepository =
            bankDataRepository
   }

@Singleton
class ApiHeadersImpl @Inject constructor() : ApiHeaders, Interceptor {
    override var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().let {
            if (authToken != null) {
                it.newBuilder()
                        .header("Authorization", "Bearer $authToken")
                        .build()
            } else {
                it
            }
        })
    }
}

@Module
class ApiModule(private val baseURL: String) {

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    .create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideApiHeaders(apiHeaders: ApiHeadersImpl): ApiHeaders = apiHeaders

    @Provides
    @Singleton
    fun provideOKHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            apiHeaders: ApiHeadersImpl): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(apiHeaders)

        if (BuildConfig.DEBUG)
            builder.addInterceptor(loggingInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideResponseBodyConverter(retrofit: Retrofit): Converter<ResponseBody, ApiResponse<*>> {
        return retrofit.responseBodyConverter(ApiResponse::class.java, arrayOf())
    }

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }
}
