package com.jatinvashisht.credentialmanager.presentation.add_credential_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddCredentialScreen(addCredentialViewModel: AddCredentialViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = addCredentialViewModel::onInsertCredentialButtonClicked) {
            Text(text = "Add Credential")
        }
    }
}