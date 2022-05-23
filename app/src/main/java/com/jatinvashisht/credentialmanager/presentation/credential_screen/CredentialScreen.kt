package com.jatinvashisht.credentialmanager.presentation.credential_screen

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jatinvashisht.credentialmanager.core.Screen
import com.jatinvashisht.credentialmanager.presentation.credential_screen.components.CredentialListItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow

@Composable
fun CredentialScreen(
    credentialViewModel: CredentialViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val credentialScreenState = credentialViewModel.credentialScreen.value
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = Unit) {
        credentialViewModel.uiEvents.consumeAsFlow().collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                is UiEvents.Navigate -> navController.navigate(event.route) {
                    this.launchSingleTop = true
                }
            }
        }
    }

    when {
        credentialScreenState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        credentialScreenState.error.isNotBlank() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = credentialScreenState.error, color = Color.Red)
            }
        }
        else -> {
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddCredentialScreen.route){launchSingleTop = true} },
                        modifier = Modifier.padding(bottom = 32.dp, end = 5.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add new credential")
                    }
                }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    items(items = credentialScreenState.data) { item ->
                        Log.d("homescreen", "data is $item")
                        CredentialListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), credentialEntity = item
                        ) {
                            credentialViewModel.onDeleteIconButtonClicked(credentialEntity = item)
                        }
                    }
                }
            }
        }
    }
}
