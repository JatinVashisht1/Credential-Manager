package com.jatinvashisht.credentialmanager.presentation.add_credential_screen

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.credentialmanager.data.local.CredentialDatabase
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.launch

@HiltViewModel
class AddCredentialViewModel @Inject constructor(
    val credentialRepository: CredentialRepository
) : ViewModel() {
    init {
//        addCredentialToDatabase()
    }

    fun onInsertCredentialButtonClicked(){
        viewModelScope.launch {
            credentialRepository.insertCredential(
                CredentialEntity(
                    credentialTitle = "RandomTitle",
                    credentialInfo = "This is secret info"
                )
            )
        }
    }
}