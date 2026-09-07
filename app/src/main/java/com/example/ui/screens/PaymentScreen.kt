package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Companion
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
import com.example.ui.theme.MityraVerifiedTeal

@Composable
fun PaymentScreen(
    companion: Companion,
    eventType: String,
    date: String,
    timeSlot: String,
    durationHours: Int,
    venueAddress: String,
    appliedCoupon: String,
    discountAmount: Int,
    selectedPaymentMethod: String,
    onPaymentMethodChange: (String) -> Unit,
    selectedUpiApp: String,
    onUpiAppChange: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    cardExpiry: String,
    onCardExpiryChange: (String) -> Unit,
    cardCvv: String,
    onCardCvvChange: (String) -> Unit,
    onApplyCoupon: (String) -> Unit,
    isPaymentSuccess: Boolean,
    onCompletePayment: () -> Unit,
    onClose: () -> Unit,
    onGoToChat: () -> Unit,
    onGoToBookings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promoInput by remember { mutableStateOf(appliedCoupon) }

    val baseCost = companion.hourlyRate * durationHours
    val platformFee = 99
    val gst = (baseCost * 0.18f).toInt()
    val totalAmount = (baseCost + platformFee + gst - discountAmount).coerceAtLeast(0)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("payment_screen"),
        color = MityraDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Secure Checkout",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "256-bit SSL",
                        tint = MityraVerifiedTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "256-bit SSL",
                        color = MityraVerifiedTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Outing Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = companion.imageResId),
                            contentDescription = companion.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${companion.name} (${companion.age})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "$eventType • $durationHours hrs",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraCoral,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$date • $timeSlot",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Price Breakdown Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bill Details",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Hourly cost
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Companion Base Fee ($durationHours hrs @ ₹${companion.hourlyRate}/hr)", color = MityraTextSecondary, fontSize = 13.sp)
                            Text("₹$baseCost", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Platform fee
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Platform Safety & Concierge Fee", color = MityraTextSecondary, fontSize = 13.sp)
                            Text("₹$platformFee", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // GST
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Taxes & GST (18%)", color = MityraTextSecondary, fontSize = 13.sp)
                            Text("₹$gst", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }

                        // Promo Discount
                        if (discountAmount > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Coupon Discount ($appliedCoupon)", color = MityraSuccessGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("-₹$discountAmount", color = MityraSuccessGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MityraCardBorder))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Total Payable
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Amount", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("₹$totalAmount", color = MityraGold, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Promo Code Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = { promoInput = it },
                        modifier = Modifier.weight(1f).height(50.dp).testTag("coupon_input"),
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("Enter Coupon (try MITYRA200)", color = MityraTextMuted, fontSize = 12.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Tag, contentDescription = "Coupon", tint = MityraGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MityraDarkSurface,
                            unfocusedContainerColor = MityraDarkSurface,
                            focusedBorderColor = MityraGold,
                            unfocusedBorderColor = MityraCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = { onApplyCoupon(promoInput) },
                        modifier = Modifier.height(50.dp).testTag("apply_coupon_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MityraGold)
                    ) {
                        Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Payment Options Section
                Text(
                    text = "Select Payment Method",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Option 1: UPI
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            if (selectedPaymentMethod == "UPI") MityraCoral else MityraCardBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onPaymentMethodChange("UPI") },
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = "UPI",
                                    tint = MityraCoral,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("UPI (Instant, Zero Surcharge)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Google Pay, PhonePe, Paytm, Any UPI ID", color = MityraTextSecondary, fontSize = 11.sp)
                                }
                            }
                            RadioButton(
                                selected = selectedPaymentMethod == "UPI",
                                onClick = { onPaymentMethodChange("UPI") },
                                colors = RadioButtonDefaults.colors(selectedColor = MityraCoral)
                            )
                        }

                        if (selectedPaymentMethod == "UPI") {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Google Pay", "PhonePe", "Paytm").forEach { app ->
                                    val isAppSelected = selectedUpiApp == app
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isAppSelected) MityraCoral.copy(alpha = 0.2f) else MityraDarkSurfaceVariant)
                                            .border(1.dp, if (isAppSelected) MityraCoral else Color.Transparent, RoundedCornerShape(12.dp))
                                            .clickable { onUpiAppChange(app) }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = app,
                                            color = if (isAppSelected) MityraCoral else MityraTextPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: Credit / Debit Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            if (selectedPaymentMethod == "CARD") MityraCoral else MityraCardBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onPaymentMethodChange("CARD") },
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = "Card",
                                    tint = MityraGold,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Credit / Debit Card", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Visa, MasterCard, RuPay", color = MityraTextSecondary, fontSize = 11.sp)
                                }
                            }
                            RadioButton(
                                selected = selectedPaymentMethod == "CARD",
                                onClick = { onPaymentMethodChange("CARD") },
                                colors = RadioButtonDefaults.colors(selectedColor = MityraCoral)
                            )
                        }

                        if (selectedPaymentMethod == "CARD") {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = onCardNumberChange,
                                label = { Text("Card Number") },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant,
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = onCardExpiryChange,
                                    label = { Text("MM/YY") },
                                    modifier = Modifier.weight(1f).height(54.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MityraDarkSurfaceVariant,
                                        unfocusedContainerColor = MityraDarkSurfaceVariant,
                                        focusedBorderColor = MityraCoral,
                                        unfocusedBorderColor = MityraCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = onCardCvvChange,
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f).height(54.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MityraDarkSurfaceVariant,
                                        unfocusedContainerColor = MityraDarkSurfaceVariant,
                                        focusedBorderColor = MityraCoral,
                                        unfocusedBorderColor = MityraCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 3: Mityra Wallet
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            if (selectedPaymentMethod == "WALLET") MityraCoral else MityraCardBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onPaymentMethodChange("WALLET") },
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = MityraVerifiedTeal,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Mityra Safety Wallet", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Balance: ₹4,500 available", color = MityraVerifiedTeal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        RadioButton(
                            selected = selectedPaymentMethod == "WALLET",
                            onClick = { onPaymentMethodChange("WALLET") },
                            colors = RadioButtonDefaults.colors(selectedColor = MityraCoral)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bottom Pay CTA
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MityraCardBorder)
                    .navigationBarsPadding(),
                color = MityraDarkSurface,
                tonalElevation = 12.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onCompletePayment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("pay_securely_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MityraCoral)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Secure",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pay ₹$totalAmount Securely",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }

    // Success Confirmation Modal Dialog
    if (isPaymentSuccess) {
        Dialog(onDismissRequest = onClose) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .border(1.5.dp, MityraSuccessGreen, RoundedCornerShape(28.dp))
                    .testTag("payment_success_dialog"),
                color = MityraDarkSurface,
                tonalElevation = 24.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MityraSuccessGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MityraSuccessGreen,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Booking Confirmed! 🎉",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your companion outing with ${companion.name} is scheduled for $date ($timeSlot).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MityraTextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MityraDarkSurfaceVariant)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Booking Ref: MIT-89241 • Escrow Active",
                            color = MityraGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onGoToChat,
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("dialog_chat_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MityraCoral)
                    ) {
                        Text("Chat with ${companion.name}", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onGoToBookings,
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("dialog_bookings_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28223B))
                    ) {
                        Text("View My Bookings Pass", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
