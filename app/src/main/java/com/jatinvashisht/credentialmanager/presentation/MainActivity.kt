package com.jatinvashisht.credentialmanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.presentation.add_credential_screen.AddCredentialScreen
import com.jatinvashisht.credentialmanager.presentation.credential_screen.CredentialScreen
import com.jatinvashisht.credentialmanager.presentation.ui.theme.CredentialManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CredentialManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.CredentialScreen.route){
                        composable(Screen.CredentialScreen.route){
                            CredentialScreen(navController = navController)
                        }
                        composable(Screen.AddCredentialScreen.route + "?id={id}",
                        arguments = listOf(
                            navArgument("id"){
                                type = NavType.IntType
                                this.defaultValue = -1
                            }
                        )){
                            AddCredentialScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
