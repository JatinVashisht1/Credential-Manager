package com.jatinvashisht.credentialmanager.domain.usecases.credential_use_cases

import com.jatinvashisht.credentialmanager.core.Resource
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity
import com.jatinvashisht.credentialmanager.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCredentialsByNameUseCase @Inject constructor(private val credentialRepository: CredentialRepository) {
    /*
    suspend operator fun invoke(): Flow<Resource<List<CredentialEntity>>> = flow {
        try {
            emit(Resource.Loading<List<CredentialEntity>>())
            credentialRepository.getAllCredentialsOrderByCredentialTitle()
                .collectLatest { credentialList ->
                    emit(Resource.Success<List<CredentialEntity>>(data = credentialList))
                }
        } catch (exception: Exception) {
            emit(
                Resource.Error<List<CredentialEntity>>(
                    errorMessage = exception.localizedMessage ?: "unable to fetch credentials"
                )
            )
        }
    }
     */
}
