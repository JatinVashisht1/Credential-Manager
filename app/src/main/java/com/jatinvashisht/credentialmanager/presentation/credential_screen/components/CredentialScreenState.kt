package com.jatinvashisht.credentialmanager.presentation.credential_screen.components

import com.jatinvashisht.credentialmanager.data.local.CredentialEntity

data class CredentialScreenState(
    val error: String = "",
    val isLoading: Boolean = false,
    val data: List<CredentialEntity> = emptyList()
)
