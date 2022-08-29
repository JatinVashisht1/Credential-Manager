package com.jatinvashisht.credentialmanager.presentation.recycle_bin.components

import com.jatinvashisht.credentialmanager.domain.models.ModelCredentialItem

data class RecycleBinScreenState(
    val recycledItems : List<ModelCredentialItem> = emptyList()
)
