package com.jatinvashisht.credentialmanager.data.mappers

import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.data.local.RecycleBinEntity
import com.jatinvashisht.credentialmanager.domain.models.ModelCredentialItem

fun CredentialEntity.toModelCredentialItem() : ModelCredentialItem = ModelCredentialItem(
    credentialTitle = credentialTitle,
    credentialInfo = credentialInfo
)

fun ModelCredentialItem.toCredentialEntity(): CredentialEntity = CredentialEntity(
    credentialTitle = credentialTitle,
    credentialInfo = credentialInfo
)

fun RecycleBinEntity.toModelCredentialItem(): ModelCredentialItem = ModelCredentialItem(
    credentialInfo = credentialInfo,
    credentialTitle = credentialTitle
)

fun ModelCredentialItem.toRecycleBinEntity(): RecycleBinEntity = RecycleBinEntity(
    credentialTitle = credentialTitle,
    credentialInfo = credentialInfo
)