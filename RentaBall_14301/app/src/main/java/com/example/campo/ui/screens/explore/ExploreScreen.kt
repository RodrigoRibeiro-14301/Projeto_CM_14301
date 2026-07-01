package com.example.campo.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.model.SoccerField
import com.example.campo.model.sampleFields
import com.example.campo.ui.components.CampoSearchBar
import com.example.campo.ui.components.TagPill
import com.example.campo.ui.screens.details.FieldDetailsScreen
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun ExploreScreen(onFieldClick: (String) -> Unit = {}) {
    val fields = remember { sampleFields() }
    var searchQuery by remember { mutableStateOf("") }

    val filteredFields = remember(searchQuery, fields) {
        fields.filter { field ->
            searchQuery.isBlank() ||
                    field.name.contains(searchQuery, ignoreCase = true) ||
                    field.location.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(stringResource(R.string.explore_title), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(2.dp))
            Text(stringResource(R.string.explore_subtitle), color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            CampoSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Pesquisar por nome ou zona..."
            )
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.fields_found, filteredFields.size), color = TextSecondary, fontSize = 13.sp)
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredFields) { field ->
                ExploreFieldCard(field = field, onClick = { onFieldClick(field.id) })
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ExploreFieldCard(field: SoccerField, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = painterResource(field.imageRes),
                contentDescription = field.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TagPill(field.format)
                TagPill(field.surface)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(field.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(field.address, color = TextSecondary, fontSize = 13.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("\u20ac${field.pricePerHour}", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(stringResource(R.string.per_hour), color = TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FieldDetailsScreenPreview() {
    CampoTheme {
        ExploreScreen(
        )
    }
}
