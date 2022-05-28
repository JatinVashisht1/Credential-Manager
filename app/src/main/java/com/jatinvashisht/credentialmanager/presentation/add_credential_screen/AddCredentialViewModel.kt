package com.jatinvashisht.credentialmanager.presentation.add_credential_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.credentialmanager.core.Constants
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import com.jatinvashisht.credentialmanager.presentation.add_credential_screen.components.CustomTextFieldState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@HiltViewModel
class AddCredentialViewModel @Inject constructor(
     savedStateHandle: SavedStateHandle,
    private val repository: CredentialRepository
) : ViewModel() {
    private val componentCredentialTitleMutableState = mutableStateOf<CustomTextFieldState>(
        value = CustomTextFieldState(
            label = "Credential Title",
            placeholder = "Credential title can't be empty",
        )
    )
    val componentCredentialTitleState = componentCredentialTitleMutableState

    private val componentCredentialInfoMutableState = mutableStateOf<CustomTextFieldState>(
        value = CustomTextFieldState(
            label = "Credential Info",
            placeholder = "Credential info can't be empty"
        )
    )
    val componentCredentialInfoState = componentCredentialInfoMutableState

    private val componentCredentialKeyMutableState = mutableStateOf<CustomTextFieldState>(
        value = CustomTextFieldState(
            label = "Private Key",
            placeholder = "Private Key cannot be empty"
        )
    )
    val componentCredentialKey = componentCredentialKeyMutableState

    val uiEvents = Channel<UiEvents>()

    init {
        val id = savedStateHandle.get<Int>(Constants.CREDENTIAL_PRIVATE_KEY)
        if (id != -1) {
            viewModelScope.launch {
                id?.let {
                    getCredentialById(id = it)
                }
            }
        }
    }

    suspend fun fireUiEvent(event: UiEvents) {
        when (event) {
            is UiEvents.Navigate -> uiEvents.send(event)
            is UiEvents.ShowSnackbar -> uiEvents.send(event)
        }
    }

    fun onSaveCredentialButtonClicked() {
        viewModelScope.launch {
            if (componentCredentialInfoMutableState.value.value.trim()
                    .isNotBlank() && componentCredentialTitleMutableState.value.value.trim()
                    .isNotBlank() && componentCredentialKeyMutableState.value.value.trim().isNotBlank()
            ) {
                repository.insertCredential(
                    CredentialEntity(
                        credentialTitle = componentCredentialTitleMutableState.value.value,
                        credentialInfo = componentCredentialInfoMutableState.value.value,
                    ),
                    privateKey = componentCredentialKeyMutableState.value.value.trim()
                )
                fireUiEvent(event = UiEvents.Navigate(Screen.CredentialScreen.route))
            } else {
                fireUiEvent(event = UiEvents.ShowSnackbar("Credential title and info should not be empty"))
            }
        }
    }

    private suspend fun getCredentialById(id: Int) {
        val credential = repository.getCredentialByPrimaryKey(id = id)
        credential?.let { credentialEntity ->
            componentCredentialTitleMutableState.value = CustomTextFieldState(
                value = credentialEntity.credentialTitle,
                label = "Credential Title",
                placeholder = "Credential title can't be empty",
                isError = credentialEntity.credentialTitle.isBlank()
            )
            componentCredentialInfoMutableState.value = CustomTextFieldState(
                value = credentialEntity.credentialInfo,
                label = "Credential Info",
                placeholder = "Credential info can't be empty",
                isError = credentialEntity.credentialInfo.isBlank()
            )
        }
    }


    fun onComponentCredentialTitleValueChanged(newValue: String) {
        componentCredentialTitleMutableState.value = CustomTextFieldState(
            value = newValue,
            label = "Credential Title",
            placeholder = "Credential title can't be empty",
            isError = newValue.isBlank()
        )
    }

    fun onComponentCredentialInfoValueChanged(newValue: String) {
        componentCredentialInfoMutableState.value = CustomTextFieldState(
            value = newValue,
            label = "Credential Info",
            placeholder = "Credential info can't be empty",
            isError = newValue.isBlank()
        )
    }

    fun onComponentCredentialKeyChanged(newValue: String){
        componentCredentialKey.value = CustomTextFieldState(
            value = newValue,
            label = "Private Key",
            placeholder = "Private Key cannot be empty",
            isError = newValue.isBlank()
        )
    }
}

sealed class UiEvents {
    data class Navigate(val route: String) : UiEvents()
    data class ShowSnackbar(val message: String) : UiEvents()
}