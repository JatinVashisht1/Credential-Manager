package com.jatinvashisht.credentialmanager.presentation.credential_screen.components

//import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jatinvashisht.credentialmanager.R.drawable
import com.jatinvashisht.credentialmanager.data.cryptography.BiometricPromptUtils
import com.jatinvashisht.credentialmanager.data.local.CredentialEntity

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CredentialListItem(
    modifier: Modifier,
    credentialEntity: CredentialEntity,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEditCredentialEntityButtonClicked: () -> Unit,
    onDeleteIconButtonClick: () -> Unit,
) {
    val showInfo = rememberSaveable { mutableStateOf<Boolean>(false) }
    val context = LocalContext.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                showInfo.value = false
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)
        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Box(
        modifier = modifier
            .drawBehind {
                this.drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf<Color>(
                            Color(0xFF14A0E0),
                            Color(0xffc471ed),
                            Color(0xfff64f59)
                        )
                    ), cornerRadius = CornerRadius(x = 5f, y = 5f)
                )
            }
            .animateContentSize(tween(300)),
    ) {
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
                        .padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h6.fontSize
                )
                AnimatedVisibility(visible = showInfo.value) {
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

                IconButton(onClick = {
                    if (!showInfo.value) {
                        val authenticator =
                            BiometricPromptUtils.createBiometricPrompt(activity = context as FragmentActivity) {
                                showInfo.value = true
                            }
                        val promptInfo =
                            BiometricPromptUtils.createPromptInfo(context as FragmentActivity)
                        authenticator.authenticate(promptInfo)

                    } else {
                        showInfo.value = !showInfo.value
                    }
                }) {
                    Icon(
                        painter = if (showInfo.value) painterResource(id = drawable.ic_hide_password) else painterResource(
                            id = drawable.ic_show_password
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(width = 35.dp, height = 35.dp)
                    )
                }

                IconButton(onClick = onEditCredentialEntityButtonClicked) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit credential")
                }
            }
        }
    }
}