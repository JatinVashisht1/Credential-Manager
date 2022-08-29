package com.jatinvashisht.credentialmanager.presentation.login_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.data.cryptography.BiometricPromptUtils

@Composable
fun LoginScreen(navController: NavController) {
    val showAuthenticationPrompt = rememberSaveable { mutableStateOf<Int>(0) }
    val context = LocalContext.current
    val biometricPrompt =
        BiometricPromptUtils.createBiometricPrompt(LocalContext.current as FragmentActivity) {
            navController.navigate(Screen.CredentialScreen.route)
        }

    LaunchedEffect(key1 = showAuthenticationPrompt.value){
        biometricPrompt.authenticate(BiometricPromptUtils.createPromptInfo(context as FragmentActivity))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Text(
            text = "Authenticate to continue", modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Button(
            onClick = { showAuthenticationPrompt.value++ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
        ) {
            Text("Authenticate")
        }
    }
}