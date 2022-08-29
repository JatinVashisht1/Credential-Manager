package com.jatinvashisht.credentialmanager.presentation.credential_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.data.cryptography.BiometricPromptUtils
import com.jatinvashisht.credentialmanager.presentation.credential_screen.components.CredentialListItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun CredentialScreen(
    credentialViewModel: CredentialViewModel = hiltViewModel(),
    navController: NavHostController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    BackHandler {
        onBackPressed()
    }
    val credentialScreenState = credentialViewModel.credentialScreen.value
    val scaffoldState = rememberScaffoldState()



    LaunchedEffect(key1 = Unit) {
        credentialViewModel.uiEvents
            .receiveAsFlow()
            .collect { event ->
                when (event) {
                    is UiEvents.ShowSnackbar -> {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short,
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            credentialViewModel.onSnackbarUndoButtonClicked()
                        }
                    }
                    is UiEvents.Navigate -> navController.navigate(event.route) {
                        launchSingleTop = true
                    }
                }
            }
    }

    when {
        credentialScreenState.isLoading -> {
            Log.d("credentialscreen", "content is loading [loading state]")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        credentialScreenState.error.isNotBlank() -> {
            Log.d("credentialscreen", "unable to load content [error state]")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = credentialScreenState.error, color = Color.Red)
            }
        }
        else -> {
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { credentialViewModel.fireUiEvents(event = UiEvents.Navigate(route = Screen.AddCredentialScreen.route)) },
                        modifier = Modifier.padding(bottom = 32.dp, end = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new credential"
                        )
                    }
                }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()
                ) {
                    items(items = credentialScreenState.data) { item ->
                        Log.d("homescreen", "data is $item")
                        CredentialListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), credentialEntity = item,
                            onEditCredentialEntityButtonClicked = {
                                val promptInfo = BiometricPromptUtils.createPromptInfo(context as FragmentActivity)
                                val prompt = BiometricPromptUtils.createBiometricPrompt(context){
                                    credentialViewModel.fireUiEvents(UiEvents.Navigate(Screen.AddCredentialScreen.route + "?id=${item.primaryKey}"))
                                }
                                prompt.authenticate(promptInfo)
                            }
                        ) {
                            val promptInfo = BiometricPromptUtils.createPromptInfo(context as FragmentActivity)
                            val prompt = BiometricPromptUtils.createBiometricPrompt(context){
                                credentialViewModel.onDeleteIconButtonClicked(credentialEntity = item)
                            }
                            prompt.authenticate(promptInfo)
                        }
                    }
                }
            }
        }
    }
}
