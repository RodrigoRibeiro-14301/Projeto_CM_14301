package com.example.campo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun SettingsScreen(onBackClick: () -> Unit = {}, onProClick: () -> Unit = {}) {
    val auth = remember { FirebaseAuth.getInstance() }
    val userEmail = auth.currentUser?.email ?: stringResource(R.string.no_account)
    val initial = userEmail.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.btn_back), tint = TextPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Text(stringResource(R.string.menu_settings), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(SoftGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 32.sp)
            }

            Spacer(Modifier.height(32.dp))

            // Account info section
            Text(
                stringResource(R.string.section_account_info),
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            // Email field
            SettingsField(label = stringResource(R.string.label_email), value = userEmail)
            Spacer(Modifier.height(10.dp))

            // Password field
            SettingsField(label = stringResource(R.string.label_password), value = "••••••••")

            Spacer(Modifier.height(24.dp))

            // PRO section
            Text(
                stringResource(R.string.section_subscription),
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SoftGreen.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .clickable { onProClick() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚡", fontSize = 16.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.app_display_name) + " PRO", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.pro_subtitle_settings), color = TextSecondary, fontSize = 12.sp)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SoftGreen, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun SettingsField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(label, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(value, color = TextPrimary, fontSize = 15.sp)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    CampoTheme {
        SettingsScreen()
    }
}
