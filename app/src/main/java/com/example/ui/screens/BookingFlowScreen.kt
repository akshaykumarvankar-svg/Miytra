package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Companion
import com.example.data.model.EventCategory
import com.example.ui.theme.MityraCardBorder
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraSuccessGreen
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary

@Composable
fun BookingFlowScreen(
    companion: Companion,
    eventType: String,
    onEventTypeChange: (String) -> Unit,
    bookingDate: String,
    onDateChange: (String) -> Unit,
    timeSlot: String,
    onTimeSlotChange: (String) -> Unit,
    durationHours: Int,
    onDurationChange: (Int) -> Unit,
    venueAddress: String,
    onVenueChange: (String) -> Unit,
    specialNotes: String,
    onNotesChange: (String) -> Unit,
    onClose: () -> Unit,
    onProceedToPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateOptions = listOf("Tonight", "Tomorrow", "This Friday", "This Saturday", "Sunday Brunch")
    val timeSlotOptions = listOf("07:00 PM - 09:00 PM", "08:00 PM - 11:00 PM", "09:30 PM - 01:30 AM", "01:00 PM - 04:00 PM")
    val durationOptions = listOf(1, 2, 3, 4, 6)

    val baseCost = companion.hourlyRate * durationHours
    val platformFee = 99
    val gst = (baseCost * 0.18f).toInt()
    val discount = 200
    val totalAmount = (baseCost + platformFee + gst - discount).coerceAtLeast(0)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("booking_flow_screen"),
        color = MityraDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = companion.imageResId),
                        contentDescription = companion.name,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.dp, MityraCoral, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Book ${companion.name}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Rate: ₹${companion.hourlyRate}/hour • ID Verified",
                            style = MaterialTheme.typography.labelSmall,
                            color = MityraGold
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("booking_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MityraTextSecondary
                    )
                }
            }

            // Scrollable Form Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // 1. Event Type Selector
                Text(
                    text = "1. Select Social Event Type",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Dinner Dates 🍷",
                        "Movie Partners 🎬",
                        "Event Companions 🎭",
                        "Party Partners 🪩",
                        "Casual Hangouts ☕"
                    ).forEach { type ->
                        val isSelected = eventType.contains(type.split(" ")[0])
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) MityraCoral else MityraDarkSurface)
                                .border(1.dp, if (isSelected) MityraCoral else MityraCardBorder, RoundedCornerShape(14.dp))
                                .clickable { onEventTypeChange(type) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("booking_event_type_${type.take(4)}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else MityraTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Date Selection
                Text(
                    text = "2. Outing Date",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dateOptions.forEach { date ->
                        val isSelected = bookingDate.contains(date)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) MityraCoral else MityraDarkSurface)
                                .border(1.dp, if (isSelected) MityraCoral else MityraCardBorder, RoundedCornerShape(14.dp))
                                .clickable { onDateChange(date) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("booking_date_$date"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else MityraTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Time Slot Selection
                Text(
                    text = "3. Preferred Time Slot",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    timeSlotOptions.forEach { slot ->
                        val isSelected = timeSlot == slot
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) MityraCoral.copy(alpha = 0.2f) else MityraDarkSurface)
                                .border(1.dp, if (isSelected) MityraCoral else MityraCardBorder, RoundedCornerShape(14.dp))
                                .clickable { onTimeSlotChange(slot) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("booking_timeslot_${slot.take(5)}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Time",
                                tint = if (isSelected) MityraCoral else MityraTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = slot,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MityraTextPrimary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Meeting Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "4. Meeting Duration",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$durationHours hours (₹${companion.hourlyRate * durationHours})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MityraGold,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    durationOptions.forEach { hrs ->
                        val isSelected = durationHours == hrs
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) MityraGold else MityraDarkSurface)
                                .border(1.dp, if (isSelected) MityraGold else MityraCardBorder, RoundedCornerShape(14.dp))
                                .clickable { onDurationChange(hrs) }
                                .padding(vertical = 10.dp)
                                .testTag("booking_duration_${hrs}h"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${hrs}h",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.Black else MityraTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5. Venue / Meeting Location Address
                Text(
                    text = "5. Venue / Meeting Location",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = venueAddress,
                    onValueChange = onVenueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_venue_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MityraDarkSurface,
                        unfocusedContainerColor = MityraDarkSurface,
                        focusedBorderColor = MityraCoral,
                        unfocusedBorderColor = MityraCardBorder,
                        focusedTextColor = MityraTextPrimary,
                        unfocusedTextColor = MityraTextPrimary
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Venue",
                            tint = MityraCoral
                        )
                    },
                    placeholder = { Text("e.g. Bastian, Bandra West, Mumbai", color = MityraTextMuted, fontSize = 13.sp) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 6. Notes / Vibe Preferences
                Text(
                    text = "6. Special Vibe / Notes",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = specialNotes,
                    onValueChange = onNotesChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_notes_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MityraDarkSurface,
                        unfocusedContainerColor = MityraDarkSurface,
                        focusedBorderColor = MityraCoral,
                        unfocusedBorderColor = MityraCardBorder,
                        focusedTextColor = MityraTextPrimary,
                        unfocusedTextColor = MityraTextPrimary
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = "Notes",
                            tint = MityraGold
                        )
                    },
                    placeholder = { Text("Dress code, conversation topics, dietary info...", color = MityraTextMuted, fontSize = 13.sp) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Safety Guarantee Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MityraDarkSurfaceVariant)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Safety",
                        tint = MityraSuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% Escrow Protection: Funds held securely and released only after your outing completes successfully.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MityraTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Sticky Bar with Live Total & "Proceed to Payment"
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MityraCardBorder)
                    .navigationBarsPadding(),
                color = MityraDarkSurface,
                tonalElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Payable",
                            style = MaterialTheme.typography.labelSmall,
                            color = MityraTextMuted
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹$totalAmount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹200 OFF",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraSuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onProceedToPayment,
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("proceed_to_payment_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MityraCoral)
                    ) {
                        Text(
                            text = "Checkout & Pay >",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
