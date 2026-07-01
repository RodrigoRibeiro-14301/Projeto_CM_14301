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
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun HelpScreen(onBackClick: () -> Unit = {}) {

    val topics = listOf(
        stringResource(R.string.help_topic_1_title) to stringResource(R.string.help_topic_1_body),
        stringResource(R.string.help_topic_2_title) to stringResource(R.string.help_topic_2_body),
        stringResource(R.string.help_topic_3_title) to stringResource(R.string.help_topic_3_body),
        stringResource(R.string.help_topic_4_title) to stringResource(R.string.help_topic_4_body),
        stringResource(R.string.help_topic_5_title) to stringResource(R.string.help_topic_5_body),
        stringResource(R.string.help_topic_6_title) to stringResource(R.string.help_topic_6_body)
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
            Text(stringResource(R.string.menu_help), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                stringResource(R.string.help_section_title),
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            topics.forEach { (title, body) ->
                HelpCard(title = title, body = body)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HelpCard(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .padding(16.dp)
    ) {
        Text(title, color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Text(body, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
    }
}

@Preview
@Composable
fun HelpScreenPreview() {
    CampoTheme {
        HelpScreen()
    }
}
