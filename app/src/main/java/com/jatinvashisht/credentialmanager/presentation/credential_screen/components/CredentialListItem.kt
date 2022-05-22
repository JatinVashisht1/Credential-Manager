package com.jatinvashisht.credentialmanager.presentation.credential_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity

@Composable
fun CredentialListItem(modifier: Modifier, credentialEntity: CredentialEntity, onDeleteIconButtonClick: ()->Unit) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                Text(
                    text = credentialEntity.credentialTitle, modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    text = credentialEntity.credentialInfo, modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
            IconButton(onClick = onDeleteIconButtonClick) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null )
            }
        }
    }
}