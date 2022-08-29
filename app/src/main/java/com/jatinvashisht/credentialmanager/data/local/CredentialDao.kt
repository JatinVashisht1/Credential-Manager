package com.jatinvashisht.credentialmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao{

    @Insert(onConflict = REPLACE, entity = CredentialEntity::class)
    suspend fun insertCredential(credentialEntity: CredentialEntity)

    @Delete(entity = CredentialEntity::class)
    suspend fun deleteCredential(credentialEntity: CredentialEntity)

    @Query("SELECT * FROM credentialentity ORDER BY credentialTitle DESC")
    fun getAllCredentialsOrderByCredentialTitle(): Flow<List<CredentialEntity>>

    @Query("SELECT * FROM credentialentity")
    fun getAllCredentialsOrderByLastAdded(): Flow<List<CredentialEntity>>

    @Query("SELECT * FROM credentialentity WHERE primaryKey=:id")
    suspend fun getCredentialByPrimaryKey(id: Int): CredentialEntity?

    @Insert(entity = RecycleBinEntity::class, onConflict = REPLACE)
    suspend fun insertRecycleBinEntity(recycleBinEntity: RecycleBinEntity)

    @Query("SELECT * FROM recyclebinentity")
    suspend fun getRecycleBinEntities(): Flow<List<RecycleBinEntity>>

    @Delete()
    suspend fun deleteRecycleBinEntity(recycleBinEntity: RecycleBinEntity)




}