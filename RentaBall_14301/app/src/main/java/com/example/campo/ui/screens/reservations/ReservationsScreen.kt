package com.example.campo.ui.screens.reservations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.data.BookingRepository
import com.example.campo.model.Booking
import com.example.campo.model.BookingStatus
import com.example.campo.ui.components.TagPill
import com.example.campo.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import com.example.campo.ui.theme.CampoTheme
import androidx.compose.ui.res.stringResource
import com.example.campo.R

private enum class ReservationsTab { UPCOMING, HISTORY }

@Composable
fun ReservationsScreen() {
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    var bookings by remember { mutableStateOf<List<Booking>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(ReservationsTab.UPCOMING) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateFlow.collectAsState()

    // Refetch every time the screen becomes visible (e.g. after returning from booking)
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED && userEmail.isNotEmpty()) {
            isLoading = true
            BookingRepository.getUserBookings(
                userEmail = userEmail,
                onResult = { bookings = it; isLoading = false },
                onError = { isLoading = false }
            )
        }
    }

    val upcoming = bookings.filter { it.isUpcoming }
    val history = bookings.filter { !it.isUpcoming }
    val visibleBookings = if (selectedTab == ReservationsTab.UPCOMING) upcoming else history

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(stringResource(R.string.reservations_title), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(2.dp))
            Text(stringResource(R.string.reservations_total, bookings.size), color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))

            ReservationsTabs(
                selectedTab = selectedTab,
                upcomingCount = upcoming.size,
                historyCount = history.size,
                onTabSelected = { selectedTab = it }
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SoftGreen)
            }
        } else if (visibleBookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (selectedTab == ReservationsTab.UPCOMING) stringResource(R.string.empty_upcoming) else stringResource(R.string.empty_history),
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(visibleBookings) { booking ->
                    BookingCard(booking = booking)
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun ReservationsTabs(
    selectedTab: ReservationsTab,
    upcomingCount: Int,
    historyCount: Int,
    onTabSelected: (ReservationsTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceVariant)
            .padding(4.dp)
    ) {
        TabSegment(
            label = stringResource(R.string.tab_upcoming, upcomingCount),
            selected = selectedTab == ReservationsTab.UPCOMING,
            onClick = { onTabSelected(ReservationsTab.UPCOMING) },
            modifier = Modifier.weight(1f)
        )
        TabSegment(
            label = stringResource(R.string.tab_history, historyCount),
            selected = selectedTab == ReservationsTab.HISTORY,
            onClick = { onTabSelected(ReservationsTab.HISTORY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabSegment(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) SoftGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) Color.Black else TextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun BookingCard(booking: Booking) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(booking.imageRes),
                contentDescription = booking.fieldName,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        booking.fieldName,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    BookingStatusBadge(booking.status)
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(booking.location, color = TextSecondary, fontSize = 13.sp)
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DateRange, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(booking.dateLabel, color = TextSecondary, fontSize = 13.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text("🕐 ${booking.timeLabel} · ${booking.durationHours}h", color = TextSecondary, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TagPill(booking.format)
            Text("€${booking.price}", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun BookingStatusBadge(status: BookingStatus) {
    val text = if (status == BookingStatus.CONFIRMED) stringResource(R.string.status_confirmed) else stringResource(R.string.status_completed)
    val bgColor = if (status == BookingStatus.CONFIRMED) BrightGreen else SurfaceVariant
    val textColor = if (status == BookingStatus.CONFIRMED) Color.Black else TextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun ReservationsScreenPreview() {
    CampoTheme {
        ReservationsScreen()
    }
}
