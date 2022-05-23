package com.jatinvashisht.credentialmanager.core

sealed class Screen(val route: String) {
    object CredentialScreen: Screen("credentialscreen")
    object AddCredentialScreen: Screen("addcredentialscreen")
}