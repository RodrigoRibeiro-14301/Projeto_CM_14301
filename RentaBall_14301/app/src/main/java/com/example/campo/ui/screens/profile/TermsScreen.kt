package com.example.campo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun TermsScreen(onBackClick: () -> Unit = {}) {
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
            Text(stringResource(R.string.menu_terms), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                stringResource(R.string.terms_last_updated),
                color = TextSecondary,
                fontSize = 13.sp
            )

            TermsSection(
                title = stringResource(R.string.terms_s1_title),
                body = stringResource(R.string.terms_s1_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s2_title),
                body = stringResource(R.string.terms_s2_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s3_title),
                body = stringResource(R.string.terms_s3_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s4_title),
                body = stringResource(R.string.terms_s4_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s5_title),
                body = stringResource(R.string.terms_s5_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s6_title),
                body = stringResource(R.string.terms_s6_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s7_title),
                body = stringResource(R.string.terms_s7_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s8_title),
                body = stringResource(R.string.terms_s8_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s9_title),
                body = stringResource(R.string.terms_s9_body)
            )

            TermsSection(
                title = stringResource(R.string.terms_s10_title),
                body = stringResource(R.string.terms_s10_body)
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TermsSection(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(body, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
    }
}

@Preview
@Composable
fun TermsScreenPreview() {
    CampoTheme {
        TermsScreen()
    }
}
