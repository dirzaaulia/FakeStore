package com.dirzaaulia.fakestore.di

import com.dirzaaulia.fakestore.network.KtorClient
import com.dirzaaulia.fakestore.repository.NetworkRepository
import com.dirzaaulia.fakestore.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun proviewNetworkRepository(
        ktor: KtorClient
    ): NetworkRepository {
        return NetworkRepositoryImpl(ktor)
    }
}