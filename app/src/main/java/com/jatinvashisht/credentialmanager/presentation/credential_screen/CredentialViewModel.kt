package com.jatinvashisht.credentialmanager.presentation.credential_screen

import android.util.Base64
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import com.jatinvashisht.credentialmanager.presentation.credential_screen.components.CredentialScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Byte.decode
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor(
//    private val insertCredentialUseCase: InsertCredentialUseCase,
//    private val getAllCredentialsByLastAddedUseCase: GetAllCredentialsByLastAddedUseCase
    private val credentialRepository: CredentialRepository
) : ViewModel() {

    val uiEvents: Channel<UiEvents> = Channel<UiEvents>()

    private val credentialScreenState =
        mutableStateOf<CredentialScreenState>(CredentialScreenState())
    val credentialScreen = credentialScreenState

    init {
        viewModelScope.launch {
            getAllCredentials()
        }
    }

    fun fireUiEvents(event: UiEvents) {
        viewModelScope.launch {
            when (event) {
                is UiEvents.ShowSnackbar -> {
                    uiEvents.send(event)
                }
            }
        }
    }

    fun onInsertCredentialButtonClicked() {
        viewModelScope.launch {
            credentialRepository.insertCredential(
                credentialEntity = CredentialEntity(
                    credentialTitle = "#Title",
                    credentialInfo = "#Info"
                )
            )
        }
    }

    fun onDeleteIconButtonClicked(credentialEntity: CredentialEntity) {
        viewModelScope.launch {
            credentialRepository.deleteCredential(credentialEntity = credentialEntity)
            fireUiEvents(event = UiEvents.ShowSnackbar(message = "credential was deleted successfully"))
        }
    }

    private suspend fun getAllCredentials() {
        viewModelScope.launch {
            credentialRepository.getAllCredentialsOrderByLastAdded().collectLatest {encryptedCredentials->
                val decryptedCredentials = encryptedCredentials.map {
                    decryptCredential(it)
                }
                    credentialScreenState.value =
                        CredentialScreenState(error = "", isLoading = false, data = decryptedCredentials)
                }
            }
        }
    }

    private fun decryptCredential(encryptedCredentialEntity: CredentialEntity): CredentialEntity {
        val encryptedCredentialTitle = encryptedCredentialEntity.credentialTitle
        val encryptedCredentialInfo = encryptedCredentialEntity.credentialInfo
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        val staticKey = Constants.AES_KEY.toByteArray()
        val ivSpec = IvParameterSpec(Constants.IV_VECTOR)
        val keySpec = SecretKeySpec(staticKey, "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decryptedCredentialTitle = cipher.doFinal(Base64.decode(encryptedCredentialTitle, Base64.DEFAULT))
        val decryptedCredentialInfo = cipher.doFinal(Base64.decode(encryptedCredentialInfo, Base64.DEFAULT))
        val decryptedCredentialEntity =  CredentialEntity(credentialTitle = String(decryptedCredentialTitle),
        credentialInfo = String(decryptedCredentialInfo), primaryKey = encryptedCredentialEntity.primaryKey)
        return decryptedCredentialEntity
    }


sealed class UiEvents {
    class ShowSnackbar(val message: String) : UiEvents()
}