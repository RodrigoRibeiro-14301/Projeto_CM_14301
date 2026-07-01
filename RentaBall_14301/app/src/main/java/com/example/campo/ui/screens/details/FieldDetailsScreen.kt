package com.example.campo.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.data.BookingRepository
import com.example.campo.model.SoccerField
import com.example.campo.model.sampleFields
import com.example.campo.ui.components.TagPill
import com.example.campo.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.campo.R

private data class TimeSlot(val time: String, val isAvailable: Boolean)

private val ALL_TIMES = listOf(
    "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
    "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
    "20:00", "21:00", "22:00"
)

private fun buildTimeSlots(bookedTimes: Set<String>, isToday: Boolean): List<TimeSlot> {
    val currentHour = if (isToday) java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) else -1
    return ALL_TIMES.map { time ->
        val hour = time.substringBefore(":").toInt()
        val isPast = isToday && hour <= currentHour
        TimeSlot(time, isAvailable = time !in bookedTimes && !isPast)
    }
}

private fun generateUpcomingDays(todayLabel: String): List<Pair<String, String>> {
    val dayNames = java.text.DateFormatSymbols.getInstance().shortWeekdays  // auto-localizes
    val monthNames = java.text.DateFormatSymbols.getInstance().shortMonths  // auto-localizes
    val cal = java.util.Calendar.getInstance()
    return (0 until 7).map { offset ->
        if (offset > 0) cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
        val dayLabel = if (offset == 0) todayLabel else dayNames[cal.get(java.util.Calendar.DAY_OF_WEEK)]
        val dateLabel = "${cal.get(java.util.Calendar.DAY_OF_MONTH)} ${monthNames[cal.get(java.util.Calendar.MONTH)]}"
        dayLabel to dateLabel
    }
}

@Composable
fun FieldDetailsScreen(fieldId: String, onBackClick: () -> Unit = {}) {
    val fields = remember { sampleFields() }
    val field = remember(fieldId) { fields.find { it.id == fieldId } }

    if (field == null) {
        Text(stringResource(R.string.field_not_found), color = TextPrimary, modifier = Modifier.padding(20.dp))
        return
    }

    var selectedDayIndex by remember { mutableStateOf(0) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var bookedTimes by remember { mutableStateOf(emptySet<String>()) }
    var slotsLoading by remember { mutableStateOf(true) }
    val todayLabel = stringResource(R.string.today)
    val upcomingDays = remember { generateUpcomingDays(todayLabel) }
    var isSaving by remember { mutableStateOf(false) }

    // Busca ao Firestore os horários já reservados para este campo neste dia
    LaunchedEffect(field.id, selectedDayIndex) {
        slotsLoading = true
        selectedTime = null  // limpa selecção ao mudar de dia
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, selectedDayIndex)
        BookingRepository.getBookedTimesForField(
            fieldId = field.id,
            dayCalendar = cal,
            onResult = { times ->
                bookedTimes = times
                slotsLoading = false
            },
            onError = {
                bookedTimes = emptySet()
                slotsLoading = false
            }
        )
    }

    val timeSlots = remember(bookedTimes, selectedDayIndex) { buildTimeSlots(bookedTimes, selectedDayIndex == 0) }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            ReserveBar(
                price = field.pricePerHour,
                enabled = selectedTime != null && !isSaving,
                isSaving = isSaving,
                onReserveClick = {
                    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@ReserveBar
                    val time = selectedTime ?: return@ReserveBar

                    // Compute the actual event timestamp from the selected day + time
                    val cal = java.util.Calendar.getInstance()
                    cal.add(java.util.Calendar.DAY_OF_YEAR, selectedDayIndex)
                    val timeParts = time.split(":")
                    cal.set(java.util.Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                    cal.set(java.util.Calendar.MINUTE, timeParts[1].toInt())
                    cal.set(java.util.Calendar.SECOND, 0)

                    val day = upcomingDays[selectedDayIndex]
                    val dateLabel = "${day.first}, ${day.second}"

                    isSaving = true
                    BookingRepository.saveBooking(
                        userEmail = userEmail,
                        fieldId = field.id,
                        fieldName = field.name,
                        location = field.location,
                        dateLabel = dateLabel,
                        timeLabel = time,
                        format = field.format,
                        price = field.pricePerHour,
                        eventTimestamp = cal.timeInMillis,
                        onSuccess = { isSaving = false; selectedTime = null },
                        onError = { isSaving = false }
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            item { FieldPhotoHeader(field = field, onBackClick = onBackClick) }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(Modifier.height(16.dp))

                    Text(field.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(field.address, color = TextSecondary, fontSize = 13.sp)

                    Spacer(Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TagPill(field.format)
                        TagPill(field.surface)
                        TagPill(field.playersInfo)
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(stringResource(R.string.available_times), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(12.dp))
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(upcomingDays.size) { index ->
                        DayChip(
                            dayLabel = upcomingDays[index].first,
                            dateLabel = upcomingDays[index].second,
                            selected = index == selectedDayIndex,
                            onClick = { selectedDayIndex = index }
                        )
                    }
                }
                Spacer(Modifier.height(30.dp))
            }

            // Time slots, 3 per row — dados reais do Firestore
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (slotsLoading) {
                        CircularProgressIndicator(
                            color = SoftGreen,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    } else {
                    timeSlots.chunked(3).forEach { rowOfThree ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowOfThree.forEach { slot ->
                                TimeSlotButton(
                                    slot = slot,
                                    selected = slot.time == selectedTime,
                                    onClick = { if (slot.isAvailable) selectedTime = slot.time },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    } // end else
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun FieldPhotoHeader(field: SoccerField, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(DarkBackground)
    ) {
        Image(
            painter = painterResource(field.imageRes),
            contentDescription = field.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent
                            ),
                            startY = size.height * 0.1f,
                            endY = size.height
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        )
        CircleIconButton(
            icon = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.btn_back),
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun DayChip(dayLabel: String, dateLabel: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) SoftGreen.copy(alpha = 0.15f) else SurfaceVariant)
            .border(
                width = 1.5.dp,
                color = if (selected) SoftGreen else BorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(dayLabel, color = if (selected) SoftGreen else TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(dateLabel, color = if (selected) SoftGreen else TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun TimeSlotButton(slot: TimeSlot, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = when {
        selected -> SoftGreen
        else -> BorderColor
    }
    val textColor = if (slot.isAvailable) TextPrimary else TextTertiary

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) SoftGreen.copy(alpha = 0.15f) else SurfaceVariant)
            .border(
                1.dp,
                borderColor,
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = slot.isAvailable) { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(slot.time, color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(if (slot.isAvailable) stringResource(R.string.slot_free) else stringResource(R.string.slot_occupied), color = textColor, fontSize = 11.sp)
    }
}

@Composable
private fun ReserveBar(price: Int, enabled: Boolean, isSaving: Boolean = false, onReserveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("€$price", color = SoftGreen, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(stringResource(R.string.per_hour_short), color = TextSecondary, fontSize = 12.sp)
        }
        Button(
            onClick = onReserveClick,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftGreen, contentColor = Color.Black),
            modifier = Modifier.height(48.dp)
        ) {
            Text(if (isSaving) stringResource(R.string.booking_loading) else stringResource(R.string.btn_book_now), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FieldDetailsScreenPreview() {
    CampoTheme {
        FieldDetailsScreen(
            fieldId = sampleFields().first().id
        )
    }
}

