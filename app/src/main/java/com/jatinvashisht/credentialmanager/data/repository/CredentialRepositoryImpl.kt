package com.jatinvashisht.credentialmanager.data.repository

import android.util.Base64
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.cryptography.CryptographyManager
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CredentialRepositoryImpl @Inject constructor(
    private val credentialDatabase: CredentialDatabase,
    private val cryptographyManager: CryptographyManager
) : CredentialRepository {
    override suspend fun insertCredential(credentialEntity: CredentialEntity) {
        val cipher = cryptographyManager.getInitializedCipherForEncryption()
        val encryptedCredentialEntity = cryptographyManager.encryptData(credentialEntity = credentialEntity)
        credentialDatabase.dao.insertCredential(credentialEntity = encryptedCredentialEntity)
    }

    override suspend fun deleteCredential(credentialEntity: CredentialEntity) {
        credentialDatabase.dao.deleteCredential(credentialEntity = credentialEntity)
    }

    override suspend fun getAllCredentialsOrderByCredentialTitle(): Flow<List<CredentialEntity>> {
        return credentialDatabase.dao.getAllCredentialsOrderByCredentialTitle()
    }

    override fun getAllCredentialsOrderByLastAdded(): Flow<List<CredentialEntity>> {
        return credentialDatabase.dao.getAllCredentialsOrderByLastAdded()
    }

    override suspend fun getCredentialByPrimaryKey(id: Int): CredentialEntity? {
        val encryptedCredentialEntity = credentialDatabase.dao.getCredentialByPrimaryKey(id = id)?: return null
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        val decryptedCredentialEntity = cryptographyManager.decryptData(credentialEntity = encryptedCredentialEntity, cipher = cipher)
        return decryptedCredentialEntity
    }

    override fun getDecryptedCredential(credentialEntity: CredentialEntity): CredentialEntity {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        return cryptographyManager.decryptData(credentialEntity = credentialEntity, cipher = cipher)
    }
}