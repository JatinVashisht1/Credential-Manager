package com.jatinvashisht.credentialmanager.presentation.credential_screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity

@Composable
fun CredentialListItem(
    modifier: Modifier,
    credentialEntity: CredentialEntity,
    onDeleteIconButtonClick: () -> Unit
) {
    val showInfo = rememberSaveable { mutableStateOf<Boolean>(false) }
    Card(modifier = modifier.animateContentSize(tween(300))) {
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
                if (showInfo.value) {
                    Text(
                        text = credentialEntity.credentialInfo, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onDeleteIconButtonClick) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
                IconButton(onClick = { showInfo.value = !showInfo.value }) {
                    Icon(imageVector = Icons.Outlined.HideImage, contentDescription = null)
                }

            }
        }
    }
}