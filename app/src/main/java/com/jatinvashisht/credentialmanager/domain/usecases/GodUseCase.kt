package com.jatinvashisht.credentialmanager.domain.usecases

import com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases.DeleteCredentialUseCase
import com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases.GetAllCredentialsByLastAddedUseCase
import com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases.GetAllCredentialsByNameUseCase
import com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases.InsertCredentialUseCase
import javax.inject.Inject

data class GodUseCase @Inject constructor(
    val insertCredentialUseCase: InsertCredentialUseCase,
    val deleteCredentialUseCase: DeleteCredentialUseCase,
    val getAllCredentialsByLastAddedUseCase: GetAllCredentialsByLastAddedUseCase,
    val getAllCredentialsByNameUseCase: GetAllCredentialsByNameUseCase
)