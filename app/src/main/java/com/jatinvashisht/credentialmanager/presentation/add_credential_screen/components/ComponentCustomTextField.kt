package com.jatinvashisht.credentialmanager.presentation.add_credential_screen.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ComponentCustomTextField(
    modifier: Modifier = Modifier,
    customTextFieldState: CustomTextFieldState,
    onCustomTextFieldValueChange: (String)->Unit
) {
    OutlinedTextField(
        value = customTextFieldState.value,
        onValueChange = onCustomTextFieldValueChange,
        modifier = modifier,
        label = { Text(text = customTextFieldState.label) },
        placeholder = { Text(text = customTextFieldState.placeholder) },
        isError = customTextFieldState.isError
    )
}