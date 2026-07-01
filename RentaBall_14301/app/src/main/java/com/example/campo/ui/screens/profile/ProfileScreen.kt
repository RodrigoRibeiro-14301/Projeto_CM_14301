package com.example.campo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.data.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun ProfileScreen(onLoggedOut: () -> Unit, onTermsClick: () -> Unit = {}, onSettingsClick: () -> Unit = {}, onHelpClick: () -> Unit = {}) {
    val auth = remember { FirebaseAuth.getInstance() }
    val userEmail = auth.currentUser?.email ?: stringResource(R.string.no_account)
    val initial = userEmail.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    var isPro by remember { mutableStateOf(false) }
    LaunchedEffect(userEmail) {
        BookingRepository.getIsPro(userEmail) { isPro = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(userEmail, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (isPro) {
                Spacer(Modifier.height(4.dp))
                Text("⚡ PRO", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(stringResource(R.string.section_account), color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        ProfileMenuRow(emoji = "🔔", label = stringResource(R.string.menu_notifications))
        ProfileMenuRow(emoji = "💳", label = stringResource(R.string.menu_payment), subtitle = "•••• 4242")
        ProfileMenuRow(emoji = "📄", label = stringResource(R.string.menu_terms), onClick = onTermsClick)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(R.string.section_preferences), color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        ProfileMenuRow(emoji = "❓", label = stringResource(R.string.menu_help), onClick = onHelpClick)
        ProfileMenuRow(emoji = "⚙️", label = stringResource(R.string.menu_settings), onClick = onSettingsClick)

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                auth.signOut()
                onLoggedOut()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant, contentColor = AlertRed)
        ) {
            Text(stringResource(R.string.btn_logout), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileMenuRow(emoji: String, label: String, subtitle: String? = null, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 16.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = TextPrimary, fontSize = 14.sp)
            if (subtitle != null) {
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(20.dp))
    }
    Spacer(Modifier.height(10.dp))
}

@Preview
@Composable
fun ProfileScreenPreview() {
    CampoTheme {
        ProfileScreen(onLoggedOut = {})
    }
}
