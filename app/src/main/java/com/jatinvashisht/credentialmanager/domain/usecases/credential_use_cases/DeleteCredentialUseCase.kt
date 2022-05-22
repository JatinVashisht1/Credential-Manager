package com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases

import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import javax.inject.Inject

class DeleteCredentialUseCase @Inject constructor(private val credentialRepository: CredentialRepository) {
    suspend operator fun invoke(credentialEntity: CredentialEntity){
        credentialRepository.deleteCredential(credentialEntity = credentialEntity)
    }
}