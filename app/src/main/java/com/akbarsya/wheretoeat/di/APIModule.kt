package com.akbarsya.wheretoeat.di

import com.akbarsya.wheretoeat.common.constant.APIEndpoint
import com.akbarsya.wheretoeat.data.api.CloudflareWorkerAPI
import com.akbarsya.wheretoeat.data.api.NominatimOSMRestAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @Singleton
    fun provideCloudflareWorkerAPI(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): CloudflareWorkerAPI {
        return Retrofit.Builder()
            .baseUrl(APIEndpoint.CLOUDFLARE_WORKER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CloudflareWorkerAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNominatimOSMRestAPI(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): NominatimOSMRestAPI {
        return Retrofit.Builder()
            .baseUrl(APIEndpoint.NOMINATIM_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NominatimOSMRestAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}