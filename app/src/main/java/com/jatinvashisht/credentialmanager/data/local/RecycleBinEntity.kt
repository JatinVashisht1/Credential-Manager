package com.jatinvashisht.credentialmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class RecycleBinEntity(
    val credentialTitle: String = "",
    val credentialInfo: String = "",
    @PrimaryKey(autoGenerate = true) val primaryKey: Long? = null,
)
