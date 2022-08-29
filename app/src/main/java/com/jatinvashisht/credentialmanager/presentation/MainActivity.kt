package com.jatinvashisht.credentialmanager.presentation

import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.presentation.add_credential_screen.AddCredentialScreen
import com.jatinvashisht.credentialmanager.presentation.credential_screen.CredentialScreen
import com.jatinvashisht.credentialmanager.presentation.login_screen.LoginScreen
import com.jatinvashisht.credentialmanager.presentation.ui.theme.CredentialManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        setContent {
            CredentialManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen.route,
                    ) {
                        composable(Screen.LoginScreen.route) {
                            LoginScreen(navController = navController)
                        }

                        composable(Screen.CredentialScreen.route) {
                            CredentialScreen(navController = navController) {
                                finish()
                            }
                        }
                        composable(Screen.AddCredentialScreen.route + "?id={id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )) {
                            AddCredentialScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}