package com.jatinvashisht.credentialmanager.data.repository

import android.util.Log
import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject


class CredentialRepositoryImpl @Inject constructor(
    private val credentialDatabase: CredentialDatabase
) : CredentialRepository {
    override suspend fun insertCredential(credentialEntity: CredentialEntity) {
        val credentialEntityToSave = encryptCredential(credentialEntity = credentialEntity)
        Log.d("repository", "credentialEntityToSave is $credentialEntityToSave")
        credentialDatabase.dao.insertCredential(credentialEntity = credentialEntityToSave)
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

    private fun encryptCredential(credentialEntity: CredentialEntity): CredentialEntity{
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(credentialEntity.credentialInfo.toByteArray())
        val iv = cipher.iv
        Log.d("repository", "value of cipherText is $cipherText and value of iv if $iv")
        val credentialEntityToSave = CredentialEntity(
            credentialTitle = credentialEntity.credentialTitle,
            credentialInfo = iv.contentToString()
        )
        Log.d("mytag", "credentialEntityToSave is $credentialEntityToSave")
        return credentialEntityToSave
    }
}