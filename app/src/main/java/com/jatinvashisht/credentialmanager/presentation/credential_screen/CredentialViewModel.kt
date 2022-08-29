package com.jatinvashisht.credentialmanager.presentation.credential_screen

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import com.jatinvashisht.credentialmanager.presentation.credential_screen.components.CredentialScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var lastDeletedCredential: Stack<CredentialEntity> = Stack<CredentialEntity>()


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
                is UiEvents.Navigate -> {
                    uiEvents.send(event)
                }

            }
        }
    }

    fun onSnackbarUndoButtonClicked() {
        Log.d("viewmodeltag", "undo button clicked, credential is $lastDeletedCredential")

        viewModelScope.launch {
            lastDeletedCredential.let {
                credentialRepository.insertCredential(credentialEntity = it.pop())
            }
        }
    }

    fun onDeleteIconButtonClicked(credentialEntity: CredentialEntity) {
        viewModelScope.launch {
            lastDeletedCredential.add(credentialEntity)
            credentialRepository.deleteCredential(credentialEntity = credentialEntity)
            fireUiEvents(event = UiEvents.ShowSnackbar(message = "credential was deleted successfully"))
        }
    }

    private suspend fun getAllCredentials() {
        viewModelScope.launch {
            credentialRepository.getAllCredentialsOrderByLastAdded()
                .collectLatest { encryptedCredentials ->
                    val decryptedCredentials = encryptedCredentials.map {
                        decryptCredential(it)
                    }
                    credentialScreenState.value =
                        CredentialScreenState(
                            error = "",
                            isLoading = false,
                            data = decryptedCredentials
                        )
                }
        }
    }

    private fun decryptCredential(encryptedCredentialEntity: CredentialEntity): CredentialEntity {
        val decryptedCredential =
            credentialRepository.getDecryptedCredential(credentialEntity = encryptedCredentialEntity)
        return decryptedCredential
    }
}

sealed class UiEvents {
    class ShowSnackbar(val message: String) : UiEvents()
    class Navigate(val route: String) : UiEvents()
}

