package com.jatinvashisht.credentialmanager.data.cryptography

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.*
import javax.inject.Inject

class CryptographyManagerImpl @Inject constructor() : CryptographyManager {

    private val KEY_SIZE = 256
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val PRIVATEKEY = "biometric_sample_encryption_key"

    override fun getInitializedCipherForEncryption(): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(PRIVATEKEY)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun getInitializedCipherForDecryption(
        initializationVector: ByteArray
    ): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(PRIVATEKEY)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        return cipher
    }

    override fun encryptData(credentialEntity: CredentialEntity): CredentialEntity {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        val staticKey = Constants.AES_KEY.toByteArray()
        val keySpec = SecretKeySpec(staticKey, "AES")
        val ivSpec = IvParameterSpec(Constants.IV_VECTOR)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val encryptedCredentialTitle =
            cipher.doFinal(credentialEntity.credentialTitle.toByteArray())
        val encryptedCredentialInfo = cipher.doFinal(credentialEntity.credentialInfo.toByteArray())
        val encryptedCredentialEntity = CredentialEntity(
            credentialTitle = Base64.encodeToString(
                encryptedCredentialTitle,
                Base64.NO_WRAP or Base64.DEFAULT
            ),
            credentialInfo = Base64.encodeToString(
                encryptedCredentialInfo,
                Base64.NO_WRAP or Base64.DEFAULT
            )
        )
        return encryptedCredentialEntity
    }

    override fun decryptData(credentialEntity: CredentialEntity, cipher: Cipher): CredentialEntity {
//        val plaintext = cipher.doFinal(ciphertext)
        val encryptedCredentialTitle = credentialEntity.credentialTitle
        val encryptedCredentialInfo = credentialEntity.credentialInfo

        val staticKey = Constants.AES_KEY.toByteArray()
        val ivSpec = IvParameterSpec(Constants.IV_VECTOR)
        val keySpec = SecretKeySpec(staticKey, "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decryptedCredentialTitle =
            cipher.doFinal(Base64.decode(encryptedCredentialTitle, Base64.DEFAULT))
        val decryptedCredentialInfo =
            cipher.doFinal(Base64.decode(encryptedCredentialInfo, Base64.DEFAULT))
        val decryptedCredentialEntity = CredentialEntity(
            credentialTitle = String(decryptedCredentialTitle),
            credentialInfo = String(decryptedCredentialInfo),
            primaryKey = credentialEntity.primaryKey
        )
        return decryptedCredentialEntity
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(keyName: String): SecretKey {
        // If Secretkey was previously created for that keyName, then grab and return it.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(PRIVATEKEY, null)?.let { return it as SecretKey }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }
}