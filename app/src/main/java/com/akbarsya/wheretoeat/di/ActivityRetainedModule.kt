package com.akbarsya.wheretoeat.di

import com.akbarsya.wheretoeat.data.repository.CloudflareWorkerRepositoryImpl
import com.akbarsya.wheretoeat.data.repository.FirebaseAuthRepositoryImpl
import com.akbarsya.wheretoeat.data.repository.NominatimOSMRestAPIRepositoryImpl
import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import com.akbarsya.wheretoeat.domain.repository.FirebaseAuthRepository
import com.akbarsya.wheretoeat.domain.repository.NominatimOSMRestAPIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCloudflareWorkerRepository(repository: CloudflareWorkerRepositoryImpl): CloudflareWorkerRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindFirebaseAuthRepository(repository: FirebaseAuthRepositoryImpl): FirebaseAuthRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindNominatimOSMRestAPIRepository(repository: NominatimOSMRestAPIRepositoryImpl): NominatimOSMRestAPIRepository
}