package com.jatinvashisht.credentialmanager.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.repository.CredentialRepositoryImpl
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCredentialDatabase(app: Application): CredentialDatabase = Room
        .databaseBuilder(app, CredentialDatabase::class.java, Constants.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideCredentialRepository(credentialDatabase: CredentialDatabase): CredentialRepository =
        CredentialRepositoryImpl(credentialDatabase = credentialDatabase)
}