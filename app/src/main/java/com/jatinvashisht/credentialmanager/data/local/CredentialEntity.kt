package com.jatinvashisht.credentialmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="credentialentity")
data class CredentialEntity(
    val credentialTitle: String = "",
    val credentialInfo: String = "",
    @PrimaryKey(autoGenerate = true) val primaryKey: Int? = null,
)