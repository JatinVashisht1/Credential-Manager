package com.jatinvashisht.credentialmanager.data.repository

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject


class CredentialRepositoryImpl @Inject constructor(
    private val credentialDatabase: CredentialDatabase
) : CredentialRepository {
    override suspend fun insertCredential(credentialEntity: CredentialEntity) {
        val credentialEntityToSave = encryptCredential(credentialEntity = credentialEntity)
        Log.d("repository", "credentialEntityToSave is $credentialEntityToSave")
        val digestedCredentialEntity = createMessageDigest(credentialEntity = credentialEntityToSave)
//        val signedCredentials = generateDigitalSignature(credentialEntity = credentialEntityToSave)
//        Log.d("repository", "signed credentials is $signedCredentials")

        credentialDatabase.dao.insertCredential(credentialEntity = digestedCredentialEntity)
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

    // utility function to encrypt credentials
    private fun encryptCredential(credentialEntity: CredentialEntity): CredentialEntity{
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(credentialEntity.credentialInfo.toByteArray())
        val credentialTitle = cipher.doFinal(credentialEntity.credentialTitle.toByteArray())
        val iv = cipher.iv
        Log.d("repository", "value of cipherText is ${String(cipherText)} and value of iv if ${String(iv)}")
        val credentialEntityToSave = CredentialEntity(
            credentialTitle = String(cipherText),
            credentialInfo = String(credentialTitle)
        )
//        Log.d("mytag", "credentialEntityToSave is $credentialEntityToSave")
        return credentialEntityToSave
    }
    private fun createMessageDigest(credentialEntity: CredentialEntity): CredentialEntity {
        val credentialTitleMessage = credentialEntity.credentialTitle
        val credentialInfoMessage = credentialEntity.credentialInfo

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val credentialTitleDigest = messageDigest.digest(credentialTitleMessage.toByteArray())
        val credentialInfoDigest = messageDigest.digest(credentialInfoMessage.toByteArray())

        val digestedCredentialEntity = CredentialEntity(
            credentialTitle =  String(credentialTitleDigest),
            credentialInfo = String(credentialInfoDigest)
        )

        return digestedCredentialEntity
    }

/*
private fun generateDigitalSignature(credentialEntity: CredentialEntity): ByteArray? {
val credentialInfoMessage = credentialEntity.credentialInfo
val credentialTitleMessage = credentialEntity.credentialTitle

//        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//        val parameterSpec = KeyGenParameterSpec.Builder(
//            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
//        )


val signature = Signature.getInstance("SHA256withECDSA")
.apply {
initSign(key)
update(credentialInfoMessage.toByteArray())
update(credentialTitleMessage.toByteArray())
}
val signedSignature = signature.sign()
return signedSignature
}
*/
}