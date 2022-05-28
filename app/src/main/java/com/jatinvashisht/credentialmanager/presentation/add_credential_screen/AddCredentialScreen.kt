package com.jatinvashisht.credentialmanager.presentation.add_credential_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jatinvashisht.credentialmanager.presentation.add_credential_screen.components.ComponentCustomTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun AddCredentialScreen(
    addCredentialViewModel: AddCredentialViewModel = hiltViewModel(),
    lazyListState: LazyListState = rememberLazyListState(),
    navController: NavController
) {
    val componentTitle = addCredentialViewModel.componentCredentialTitleState.value
    val componentInfo = addCredentialViewModel.componentCredentialInfoState.value
    val componentKey = addCredentialViewModel.componentCredentialKey.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        addCredentialViewModel.uiEvents.receiveAsFlow().collectLatest { uiEvent->
            when(uiEvent){
                is UiEvents.Navigate -> navController.navigateUp()
                is UiEvents.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(uiEvent.message)
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        LazyColumn(state = lazyListState) {
            item(1) {
                Spacer(modifier = Modifier.height(4.dp))
                ComponentCustomTextField(
                    customTextFieldState = componentTitle,
                    onCustomTextFieldValueChange = addCredentialViewModel::onComponentCredentialTitleValueChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ComponentCustomTextField(
                    customTextFieldState = componentInfo,
                    onCustomTextFieldValueChange = addCredentialViewModel::onComponentCredentialInfoValueChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ComponentCustomTextField(
                    customTextFieldState = componentKey,
                    onCustomTextFieldValueChange = addCredentialViewModel::onComponentCredentialKeyChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = addCredentialViewModel::onSaveCredentialButtonClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Text("Add Credential")
                }
            }
        }
    }
}