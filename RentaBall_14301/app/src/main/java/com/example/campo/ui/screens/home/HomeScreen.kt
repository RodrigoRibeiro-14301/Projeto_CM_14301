package com.example.campo.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.model.sampleFields
import com.example.campo.ui.components.*
import com.example.campo.ui.theme.CampoTheme
import com.example.campo.ui.theme.DarkBackground
import com.example.campo.ui.theme.SoftGreen
import com.example.campo.ui.theme.TextPrimary
import com.example.campo.ui.theme.TextSecondary
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun HomeScreen(onFieldClick: (String) -> Unit = {}) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    val fields = remember { sampleFields() }

    val filteredFields = remember(selectedFilter, searchQuery, fields) {
        fields.filter { field ->
            val matchesChip = when (selectedFilter) {
                "ALL" -> true
                "TONIGHT" -> true
                else -> field.format == selectedFilter || field.surface == selectedFilter
            }
            val matchesSearch = searchQuery.isBlank() ||
                    field.name.contains(searchQuery, ignoreCase = true) ||
                    field.location.contains(searchQuery, ignoreCase = true)
            matchesChip && matchesSearch
        }
    }
    val isFiltering = selectedFilter != "ALL" || searchQuery.isNotBlank()

    val sectionTitle = when {
        searchQuery.isNotBlank() -> "Resultados para \"$searchQuery\""
        isFiltering -> stringResource(R.string.filter_results)
        else -> stringResource(R.string.near_you)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            CampoTopBar(locationText = "Lisboa, Alameda")
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.home_ready), color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))
                CampoSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = stringResource(R.string.search_placeholder)
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        item {
            FilterChipsRow(selected = selectedFilter, onSelect = { selectedFilter = it })
            Spacer(Modifier.height(24.dp))
        }

        if (!isFiltering) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(stringResource(R.string.home_featured), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(stringResource(R.string.home_featured_subtitle), color = TextSecondary, fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(fields.take(3)) { field ->
                        FeaturedFieldCard(field = field, onClick = { onFieldClick(field.id) })
                    }
                }
                Spacer(Modifier.height(28.dp))
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(sectionTitle, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(stringResource(R.string.fields_found, filteredFields.size), color = TextSecondary, fontSize = 13.sp)
                Spacer(Modifier.height(14.dp))
            }
        }

        items(filteredFields) { field ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                NearbyFieldRow(field = field, onClick = { onFieldClick(field.id) })
            }
        }

        if (filteredFields.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.no_fields_found),
                    color = TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121317)
@Composable
fun HomeScreenPreview() {
    CampoTheme {
        HomeScreen()
    }
}