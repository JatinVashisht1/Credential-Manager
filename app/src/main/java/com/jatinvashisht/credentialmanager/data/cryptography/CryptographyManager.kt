package com.jatinvashisht.credentialmanager.data.cryptography

import android.content.Context
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import javax.crypto.Cipher

interface CryptographyManager {

    fun getInitializedCipherForEncryption(): Cipher

    fun getInitializedCipherForDecryption( initializationVector: ByteArray): Cipher

    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    fun encryptData(credentialEntity: CredentialEntity): CredentialEntity

    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    fun decryptData(credentialEntity: CredentialEntity, cipher: Cipher): CredentialEntity

}