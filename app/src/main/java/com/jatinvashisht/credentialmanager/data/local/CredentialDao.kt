package com.jatinvashisht.credentialmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao{

    @Insert(onConflict = REPLACE)
    suspend fun insertCredential(credentialEntity: CredentialEntity)

    @Delete()
    suspend fun deleteCredential(credentialEntity: CredentialEntity)

    @Query("SELECT * FROM credentialentity ORDER BY credentialTitle DESC")
    fun getAllCredentialsOrderByCredentialTitle(): Flow<List<CredentialEntity>>

    @Query("SELECT * FROM credentialentity")
    fun getAllCredentialsOrderByLastAdded(): Flow<List<CredentialEntity>>
}