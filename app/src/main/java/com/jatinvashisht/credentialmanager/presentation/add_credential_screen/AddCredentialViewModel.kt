package com.jatinvashisht.credentialmanager.presentation.add_credential_screen

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import kotlinx.coroutines.launch

@HiltViewModel
class AddCredentialViewModel @Inject constructor(
    val credentialDatabase: CredentialDatabase
) : ViewModel() {
    init {
        viewModelScope.launch {
            credentialDatabase.dao.insertCredential(
                CredentialEntity(
                    credentialTitle = "RandomTitle",
                    credentialInfo = "This is secret info"
                )
            )
        }
    }
}