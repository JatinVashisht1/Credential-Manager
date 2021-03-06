package com.jatinvashisht.credentialmanager.domain.repository

import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {
    suspend fun insertCredential(credentialEntity: CredentialEntity, privateKey: String)
    suspend fun deleteCredential(credentialEntity: CredentialEntity)
    suspend fun getAllCredentialsOrderByCredentialTitle(): Flow<List<CredentialEntity>>
    fun getAllCredentialsOrderByLastAdded(): Flow<List<CredentialEntity>>
    suspend fun getCredentialByPrimaryKey(id: Int): CredentialEntity?
    fun getDecryptedCredential(credentialEntity: CredentialEntity): CredentialEntity
}