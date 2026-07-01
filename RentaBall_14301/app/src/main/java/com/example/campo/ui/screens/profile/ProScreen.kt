package com.example.campo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.campo.data.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun ProScreen(onBackClick: () -> Unit = {}) {

    val userEmail = remember { FirebaseAuth.getInstance().currentUser?.email ?: "" }
    var isPro by remember { mutableStateOf(false) }
    LaunchedEffect(userEmail) {
        BookingRepository.getIsPro(userEmail) { isPro = it }
    }

    val features = listOf(
        stringResource(R.string.pro_feature_1),
        stringResource(R.string.pro_feature_2),
        stringResource(R.string.pro_feature_3),
        stringResource(R.string.pro_feature_4),
        stringResource(R.string.pro_feature_5)
    )

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
            Text(stringResource(R.string.app_display_name) + " PRO", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SoftGreen.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚡", fontSize = 16.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        stringResource(R.string.pro_promo_badge),
                        color = SoftGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.app_display_name) + " PRO",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    stringResource(R.string.pro_price),
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(24.dp))
                Divider(color = SurfaceVariant, thickness = 1.dp)
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    features.forEach { feature ->
                        Text(
                            text = "· $feature",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            if (isPro) {
                Button(
                    onClick = {
                        BookingRepository.cancelPro(
                            userEmail = userEmail,
                            onSuccess = { onBackClick() },
                            onError = {}
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceVariant,
                        contentColor = AlertRed
                    )
                ) {
                    Text(stringResource(R.string.btn_cancel_subscription), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = {
                        BookingRepository.setPro(
                            userEmail = userEmail,
                            onSuccess = { onBackClick() },
                            onError = {}
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text(stringResource(R.string.btn_buy_now), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(R.string.pro_cancel_note),
                    color = TextTertiary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun ProScreenPreview() {
    CampoTheme {
        ProScreen()
    }
}

