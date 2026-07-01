package com.example.campo.data

import com.example.campo.model.Booking
import com.example.campo.model.BookingStatus
import com.example.campo.model.fieldImageRes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot

object BookingRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveBooking(
        userEmail: String,
        fieldId: String,
        fieldName: String,
        location: String,
        dateLabel: String,
        timeLabel: String,
        format: String,
        price: Int,
        eventTimestamp: Long,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val doc = hashMapOf(
            "userEmail" to userEmail,
            "fieldId" to fieldId,
            "fieldName" to fieldName,
            "location" to location,
            "dateLabel" to dateLabel,
            "timeLabel" to timeLabel,
            "durationHours" to 1,
            "format" to format,
            "price" to price,
            "status" to "CONFIRMED",
            "eventTimestamp" to eventTimestamp
        )
        db.collection("bookings")
            .add(doc)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e: Exception -> onError(e) }
    }

    fun getUserBookings(
        userEmail: String,
        onResult: (List<Booking>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("bookings")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { snapshot: QuerySnapshot ->
                val now = System.currentTimeMillis()
                val bookings = snapshot.documents.mapNotNull { doc: DocumentSnapshot ->
                    val fieldId = doc.getString("fieldId") ?: return@mapNotNull null
                    val eventTimestamp = doc.getLong("eventTimestamp") ?: 0L
                    val isUpcoming = eventTimestamp > now
                    Booking(
                        id = doc.id,
                        fieldId = fieldId,
                        fieldName = doc.getString("fieldName") ?: "",
                        location = doc.getString("location") ?: "",
                        dateLabel = doc.getString("dateLabel") ?: "",
                        timeLabel = doc.getString("timeLabel") ?: "",
                        durationHours = (doc.getLong("durationHours") ?: 1L).toInt(),
                        format = doc.getString("format") ?: "",
                        price = (doc.getLong("price") ?: 0L).toInt(),
                        status = if (isUpcoming) BookingStatus.CONFIRMED else BookingStatus.COMPLETED,
                        isUpcoming = isUpcoming,
                        imageRes = fieldImageRes(fieldId)
                    )
                }.sortedBy { booking -> booking.isUpcoming.not() }
                onResult(bookings)
            }
            .addOnFailureListener { e: Exception -> onError(e) }
    }

    fun setPro(userEmail: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("users").document(userEmail)
            .set(mapOf("isPro" to true))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e: Exception -> onError(e) }
    }

    fun getIsPro(userEmail: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userEmail).get()
            .addOnSuccessListener { doc: DocumentSnapshot -> onResult(doc.getBoolean("isPro") == true) }
            .addOnFailureListener { onResult(false) }
    }

    fun cancelPro(userEmail: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("users").document(userEmail)
            .set(mapOf("isPro" to false))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e: Exception -> onError(e) }
    }

    fun getBookedTimesForField(
        fieldId: String,
        dayCalendar: java.util.Calendar,
        onResult: (Set<String>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val start = dayCalendar.clone() as java.util.Calendar
        start.set(java.util.Calendar.HOUR_OF_DAY, 0)
        start.set(java.util.Calendar.MINUTE, 0)
        start.set(java.util.Calendar.SECOND, 0)
        start.set(java.util.Calendar.MILLISECOND, 0)

        val end = start.clone() as java.util.Calendar
        end.add(java.util.Calendar.DAY_OF_YEAR, 1)

        db.collection("bookings")
            .whereEqualTo("fieldId", fieldId)
            .whereGreaterThanOrEqualTo("eventTimestamp", start.timeInMillis)
            .whereLessThan("eventTimestamp", end.timeInMillis)
            .get()
            .addOnSuccessListener { snapshot: QuerySnapshot ->
                val times = snapshot.documents.mapNotNull { it.getString("timeLabel") }.toSet()
                onResult(times)
            }
            .addOnFailureListener { e: Exception -> onError(e) }
    }
}
