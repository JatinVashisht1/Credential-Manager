package com.jatinvashisht.credentialmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CredentialEntity::class], version = 1)
abstract class CredentialDatabase : RoomDatabase(){
    abstract val dao: CredentialDao
}