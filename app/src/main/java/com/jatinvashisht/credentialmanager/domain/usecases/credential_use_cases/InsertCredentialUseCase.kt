package com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases

import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertCredentialUseCase @Inject constructor(private val credentialRepository: CredentialRepository){
    suspend operator fun invoke(credentialEntity: CredentialEntity){
//        credentialRepository.insertCredential(credentialEntity = credentialEntity)
    }
}