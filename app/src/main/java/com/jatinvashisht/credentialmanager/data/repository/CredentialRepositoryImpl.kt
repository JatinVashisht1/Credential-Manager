package com.jatinvashisht.credentialmanager.data.repository

import android.util.Base64
import android.util.Log
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CredentialRepositoryImpl @Inject constructor(
    private val credentialDatabase: CredentialDatabase
) : CredentialRepository {
    override suspend fun insertCredential(credentialEntity: CredentialEntity) {
        val encryptedCredential = encryptCredential(credentialEntity = credentialEntity)
        Log.d("repository", "encryptedCredential is $encryptedCredential")
        val digestedCredentialEntity = createMessageDigest(credentialEntity = encryptedCredential)

        Log.d(
            "repository",
            "encrypted credentials is $encryptedCredential, \ndigestedCredential is $digestedCredentialEntity"
        )

        credentialDatabase.dao.insertCredential(credentialEntity = encryptedCredential)
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

    // utility function to encrypt credentials
    private fun encryptCredential(credentialEntity: CredentialEntity): CredentialEntity {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        val staticKey = Constants.AES_KEY.toByteArray()
        val keySpec = SecretKeySpec(staticKey, "AES")
        val ivSpec = IvParameterSpec(Constants.IV_VECTOR)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val encryptedCredentialTitle = cipher.doFinal(credentialEntity.credentialTitle.toByteArray())
        val encryptedCredentialInfo = cipher.doFinal(credentialEntity.credentialInfo.toByteArray())
        val encryptedCredentialEntity = CredentialEntity(credentialTitle = Base64.encodeToString(encryptedCredentialTitle,Base64.NO_WRAP or Base64.DEFAULT),
        credentialInfo = Base64.encodeToString(encryptedCredentialInfo,Base64.NO_WRAP or Base64.DEFAULT) )
        return encryptedCredentialEntity
    }



    private fun createMessageDigest(credentialEntity: CredentialEntity): CredentialEntity {
        val credentialTitleMessage = credentialEntity.credentialTitle
        val credentialInfoMessage = credentialEntity.credentialInfo

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val credentialTitleDigest = messageDigest.digest(credentialTitleMessage.toByteArray())
        val credentialInfoDigest = messageDigest.digest(credentialInfoMessage.toByteArray())

        val digestedCredentialEntity = CredentialEntity(
            credentialTitle = String(credentialTitleDigest),
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