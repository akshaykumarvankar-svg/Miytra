package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.MembershipPaymentEntity
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraSafetyGreen
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary

// Signature Razorpay Palette
val RazorpayNavy = Color(0xFF0C2340)
val RazorpayBlue = Color(0xFF0C83FF)
val RazorpayDarkBlue = Color(0xFF07489A)
val RazorpayAccent = Color(0xFF528FF0)
val RazorpayBorder = Color(0xFF1E3A5F)
val RazorpaySurface = Color(0xFF132D4E)

@Composable
fun RazorpayCheckoutModal(
    userEmail: String,
    userPhone: String,
    selectedMethod: String,
    onMethodChange: (String) -> Unit,
    selectedUpiApp: String,
    onUpiAppChange: (String) -> Unit,
    customVpa: String,
    onCustomVpaChange: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    cardExpiry: String,
    onCardExpiryChange: (String) -> Unit,
    cardCvv: String,
    onCardCvvChange: (String) -> Unit,
    selectedBank: String,
    onBankChange: (String) -> Unit,
    isProcessing: Boolean,
    successPayment: MembershipPaymentEntity?,
    onConfirmPayment: () -> Unit,
    onDismiss: () -> Unit,
    onDoneSuccess: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            if (!isProcessing) onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isProcessing,
            dismissOnClickOutside = !isProcessing
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, RazorpayBorder, RoundedCornerShape(20.dp)),
                color = RazorpayNavy,
                shadowElevation = 16.dp
            ) {
                if (successPayment != null) {
                    RazorpaySuccessContent(
                        payment = successPayment,
                        onDone = onDoneSuccess
                    )
                } else if (isProcessing) {
                    RazorpayProcessingContent()
                } else {
                    RazorpayCheckoutForm(
                        userEmail = userEmail,
                        userPhone = userPhone,
                        selectedMethod = selectedMethod,
                        onMethodChange = onMethodChange,
                        selectedUpiApp = selectedUpiApp,
                        onUpiAppChange = onUpiAppChange,
                        customVpa = customVpa,
                        onCustomVpaChange = onCustomVpaChange,
                        cardNumber = cardNumber,
                        onCardNumberChange = onCardNumberChange,
                        cardExpiry = cardExpiry,
                        onCardExpiryChange = onCardExpiryChange,
                        cardCvv = cardCvv,
                        onCardCvvChange = onCardCvvChange,
                        selectedBank = selectedBank,
                        onBankChange = onBankChange,
                        onConfirmPayment = onConfirmPayment,
                        onClose = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun RazorpayCheckoutForm(
    userEmail: String,
    userPhone: String,
    selectedMethod: String,
    onMethodChange: (String) -> Unit,
    selectedUpiApp: String,
    onUpiAppChange: (String) -> Unit,
    customVpa: String,
    onCustomVpaChange: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    cardExpiry: String,
    onCardExpiryChange: (String) -> Unit,
    cardCvv: String,
    onCardCvvChange: (String) -> Unit,
    selectedBank: String,
    onBankChange: (String) -> Unit,
    onConfirmPayment: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Razorpay Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(RazorpayDarkBlue, RazorpayNavy)
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(RazorpayBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Razorpay",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF26D07C).copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "TRUSTED",
                                        color = Color(0xFF26D07C),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Text(
                                text = "Mityra Social Technologies Pvt Ltd",
                                color = MityraTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Amount & Plan Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(RazorpaySurface)
                        .border(1.dp, RazorpayBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mityra VIP Club Membership",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Auto-renews monthly • Cancel anytime",
                            color = MityraGold,
                            fontSize = 10.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹49.00",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Incl. all taxes",
                            color = MityraTextMuted,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Perks pill checklist
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF08192E))
                    .border(1.dp, Color(0xFF133257), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "MEMBERSHIP PERKS INCLUDED:",
                    color = RazorpayAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MityraSafetyGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("0% platform commission on all companion bookings", color = MityraTextSecondary, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MityraSafetyGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Verified Host badge & priority search ranking", color = MityraTextSecondary, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MityraSafetyGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Instant 24x7 Emergency SOS Dispatch with high priority", color = MityraTextSecondary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Mode Tabs
            Text(
                text = "SELECT PAYMENT MODE",
                color = MityraTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("UPI", "UPI / QR", Icons.Default.PhoneAndroid),
                    Triple("CARD", "Cards", Icons.Default.CreditCard),
                    Triple("NETBANKING", "NetBanking", Icons.Default.AccountBalance)
                ).forEach { (mode, label, icon) ->
                    val isSelected = selectedMethod == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) RazorpayBlue else RazorpaySurface)
                            .border(1.dp, if (isSelected) Color.White.copy(alpha = 0.4f) else RazorpayBorder, RoundedCornerShape(10.dp))
                            .clickable { onMethodChange(mode) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color.White else MityraTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else MityraTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Method Specific Form Elements
            when (selectedMethod) {
                "UPI" -> {
                    Column {
                        Text(
                            text = "Instant Pay via UPI App",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        val upiApps = listOf("Google Pay", "PhonePe", "Paytm", "BHIM UPI")
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            upiApps.forEach { app ->
                                val isChosen = selectedUpiApp == app
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isChosen) Color(0xFF1C4270) else RazorpaySurface)
                                        .border(1.dp, if (isChosen) RazorpayBlue else RazorpayBorder, RoundedCornerShape(8.dp))
                                        .clickable { onUpiAppChange(app) }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(RazorpayBlue.copy(alpha = 0.25f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PhoneAndroid,
                                                contentDescription = null,
                                                tint = RazorpayBlue,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = app, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    }
                                    if (isChosen) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RazorpayBlue, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Or enter UPI VPA / ID:",
                            color = MityraTextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = customVpa,
                            onValueChange = onCustomVpaChange,
                            placeholder = { Text("username@okhdfcbank", color = MityraTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("razorpay_custom_vpa"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = RazorpayBlue,
                                unfocusedBorderColor = RazorpayBorder,
                                focusedContainerColor = RazorpaySurface,
                                unfocusedContainerColor = RazorpaySurface
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                "CARD" -> {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Debit / Credit Card",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Auto-fill Test Card",
                                color = RazorpayAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    onCardNumberChange("4242 8891 0021 4242")
                                    onCardExpiryChange("12/28")
                                    onCardCvvChange("892")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = onCardNumberChange,
                            label = { Text("Card Number", color = MityraTextSecondary, fontSize = 11.sp) },
                            placeholder = { Text("4242 •••• •••• 4242", color = MityraTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("razorpay_card_number"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = RazorpayBlue,
                                unfocusedBorderColor = RazorpayBorder,
                                focusedContainerColor = RazorpaySurface,
                                unfocusedContainerColor = RazorpaySurface
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = onCardExpiryChange,
                                label = { Text("Expiry", color = MityraTextSecondary, fontSize = 11.sp) },
                                placeholder = { Text("MM/YY", color = MityraTextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = RazorpayBlue,
                                    unfocusedBorderColor = RazorpayBorder,
                                    focusedContainerColor = RazorpaySurface,
                                    unfocusedContainerColor = RazorpaySurface
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            OutlinedTextField(
                                value = cardCvv,
                                onValueChange = onCardCvvChange,
                                label = { Text("CVV", color = MityraTextSecondary, fontSize = 11.sp) },
                                placeholder = { Text("123", color = MityraTextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = RazorpayBlue,
                                    unfocusedBorderColor = RazorpayBorder,
                                    focusedContainerColor = RazorpaySurface,
                                    unfocusedContainerColor = RazorpaySurface
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                "NETBANKING" -> {
                    Column {
                        Text(
                            text = "Popular Indian Banks",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val banks = listOf("HDFC Bank", "ICICI Bank", "State Bank of India", "Axis Bank", "Kotak Mahindra Bank")
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            banks.forEach { bank ->
                                val isBankChosen = selectedBank == bank
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isBankChosen) Color(0xFF1C4270) else RazorpaySurface)
                                        .border(1.dp, if (isBankChosen) RazorpayBlue else RazorpayBorder, RoundedCornerShape(8.dp))
                                        .clickable { onBankChange(bank) }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = bank, color = Color.White, fontSize = 13.sp)
                                    if (isBankChosen) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RazorpayBlue, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pay Button
            Button(
                onClick = onConfirmPayment,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("razorpay_pay_button"),
                colors = ButtonDefaults.buttonColors(containerColor = RazorpayBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure Lock",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pay ₹49.00 via Razorpay",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Security note footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MityraTextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PCI-DSS Level 1 Compliant • 256-bit SSL Encryption",
                    color = MityraTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun RazorpayProcessingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(RazorpayBlue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = RazorpayBlue,
                strokeWidth = 3.dp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Processing Payment with Razorpay...",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Connecting to banking servers to authenticate your ₹49 subscription. Please do not press back or close this window.",
            color = MityraTextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = RazorpayAccent, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Razorpay Secure 3D Authentication", color = RazorpayAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RazorpaySuccessContent(
    payment: MembershipPaymentEntity,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MityraSafetyGreen.copy(alpha = 0.2f))
                .border(2.dp, MityraSafetyGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = MityraSafetyGreen,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Payment Successful!",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        Text(
            text = "Welcome to Mityra VIP Club",
            color = MityraGold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Receipt Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = RazorpaySurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, RazorpayBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Amount Paid", color = MityraTextSecondary, fontSize = 12.sp)
                    Text("₹49.00", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment ID", color = MityraTextSecondary, fontSize = 12.sp)
                    Text(payment.paymentId, color = RazorpayAccent, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Order ID", color = MityraTextSecondary, fontSize = 12.sp)
                    Text(payment.orderId, color = MityraTextPrimary, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment Method", color = MityraTextSecondary, fontSize = 12.sp)
                    Text(payment.paymentMethod, color = MityraTextPrimary, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subscription Validity", color = MityraTextSecondary, fontSize = 12.sp)
                    Text("30 Days (Active)", color = MityraSafetyGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("razorpay_success_done_button"),
            colors = ButtonDefaults.buttonColors(containerColor = RazorpayBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done & Explore VIP Perks", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
