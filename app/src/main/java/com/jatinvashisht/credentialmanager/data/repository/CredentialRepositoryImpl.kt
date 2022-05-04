package com.jatinvashisht.credentialmanager.data.repository

import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject


class CredentialRepositoryImpl @Inject constructor(
    private val credentialDatabase: CredentialDatabase
) : CredentialRepository {
    override suspend fun insertCredential(credentialEntity: CredentialEntity) {
        credentialDatabase.dao.insertCredential(credentialEntity = credentialEntity)
    }

    override suspend fun deleteCredential(credentialEntity: CredentialEntity) {
        credentialDatabase.dao.deleteCredential(credentialEntity = credentialEntity)
    }

    override fun getAllCredentialsOrderByCredentialTitle(): Flow<Resource<List<CredentialEntity>>> =
        flow {
            try {
                emit(Resource.Loading<List<CredentialEntity>>())
                credentialDatabase.dao.getAllCredentialsOrderByCredentialTitle()
                    .collectLatest { credentialList ->
                        emit(Resource.Success<List<CredentialEntity>>(data = credentialList))
                    }
            } catch (e: Exception){
                emit(Resource.Error<List<CredentialEntity>>(errorMessage = e.localizedMessage?: "Unable to load data"))
            }
        }


    override fun getAllCredentialsOrderByLastAdded(): Flow<Resource<List<CredentialEntity>>> = flow {
        try {
            emit(Resource.Loading<List<CredentialEntity>>())
            credentialDatabase.dao.getAllCredentialsOrderByCredentialTitle()
                .collectLatest { credentialList ->
                    emit(Resource.Success<List<CredentialEntity>>(data = credentialList))
                }
        } catch (e: Exception){
            emit(Resource.Error<List<CredentialEntity>>(errorMessage = e.localizedMessage?: "Unable to load data"))
        }
    }
}