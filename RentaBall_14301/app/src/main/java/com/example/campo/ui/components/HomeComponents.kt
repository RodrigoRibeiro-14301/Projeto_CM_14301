package com.example.campo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.R
import com.example.campo.model.SoccerField
import com.example.campo.ui.theme.*

// ---------- Top bar ----------

@Composable
fun CampoTopBar(
    locationText: String,
    onNotificationsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SoftGreen),
            contentAlignment = Alignment.Center
        ) {
            Text("⚽", fontSize = 18.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text("RENTABALL", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(locationText, color = TextSecondary, fontSize = 13.sp)
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ---------- Search bar ----------

@Composable
fun CampoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceVariant)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary)
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(placeholder, color = TextTertiary, fontSize = 14.sp)
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                singleLine = true,
                cursorBrush = SolidColor(SoftGreen),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ---------- Filter chips ----------

@Composable
fun CampoFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) SoftGreen.copy(alpha = 0.15f) else SurfaceVariant)
            .border(1.dp, if (selected) SoftGreen else BorderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (selected) SoftGreen else TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun FilterChipsRow(selected: String, onSelect: (String) -> Unit) {
    // Keys are language-independent; display labels are localized
    val options = listOf(
        "ALL" to stringResource(R.string.filter_all),
        "TONIGHT" to stringResource(R.string.filter_tonight),
        "5v5" to "5v5",
        "7v7" to "7v7",
        "Sintético" to stringResource(R.string.filter_sintetico),
        "Indoor" to "Indoor"
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(options) { (key, displayLabel) ->
            CampoFilterChip(label = displayLabel, selected = key == selected, onClick = { onSelect(key) })
        }
    }
}

// ---------- Small pieces ----------

@Composable
fun TagPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = TextSecondary, fontSize = 11.sp)
    }
}

// ---------- Featured field card (horizontal scroll) ----------

@Composable
fun FeaturedFieldCard(field: SoccerField, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(field.imageRes),
                contentDescription = field.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                field.name,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(field.location, color = TextSecondary, fontSize = 12.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TagPill(field.format)
                TagPill(field.surface)
            }
            Spacer(Modifier.height(8.dp))
            Text("€${field.pricePerHour}/h", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

// ---------- Nearby field row (vertical list) ----------

@Composable
fun NearbyFieldRow(field: SoccerField, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {

            Image(
                painter = painterResource(field.imageRes),
                contentDescription = field.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(field.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(field.location, color = TextSecondary, fontSize = 12.sp)
            }
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TagPill(field.format)
                TagPill(field.surface)
            }
        }
        Spacer(Modifier.width(10.dp))
        Text("€${field.pricePerHour}/h", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// ---------- Bottom navigation ----------

sealed class BottomNavTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    object Inicio : BottomNavTab(R.string.nav_home, Icons.Filled.Home)
    object Explorar : BottomNavTab(R.string.nav_explore, Icons.Filled.Search)
    object Reservas : BottomNavTab(R.string.nav_reservations, Icons.Filled.DateRange)
    object Perfil : BottomNavTab(R.string.nav_profile, Icons.Filled.Person)
}

@Composable
fun CampoBottomBar(
    selectedTab: BottomNavTab,
    reservationBadgeCount: Int = 0,
    onTabSelected: (BottomNavTab) -> Unit
) {
    val tabs = listOf(BottomNavTab.Inicio, BottomNavTab.Explorar, BottomNavTab.Reservas, BottomNavTab.Perfil)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onTabSelected(tab) }
            ) {
                BadgedBox(badge = {
                    if (tab is BottomNavTab.Reservas && reservationBadgeCount > 0) {
                        Badge(containerColor = BrightGreen) {
                            Text("$reservationBadgeCount", color = Color.Black, fontSize = 10.sp)
                        }
                    }
                }) {
                    Icon(
                        tab.icon,
                        contentDescription = stringResource(tab.labelRes),
                        tint = if (isSelected) SoftGreen else TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(stringResource(tab.labelRes), color = if (isSelected) SoftGreen else TextTertiary, fontSize = 11.sp)
            }
        }
    }
}
