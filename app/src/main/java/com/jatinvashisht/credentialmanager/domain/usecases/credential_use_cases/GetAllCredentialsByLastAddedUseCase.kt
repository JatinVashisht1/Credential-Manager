package com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases

import android.util.Log
import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCredentialsByLastAddedUseCase @Inject constructor(private val credentialRepository: CredentialRepository) {
//    suspend operator fun invoke(): Flow<Resource<List<CredentialEntity>>> {
        // TODO: Do this mf thing later
}