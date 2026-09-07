package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionApplicationEntity
import com.example.data.model.MembershipPaymentEntity
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraCoralDark
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraSafetyGreen
import com.example.ui.theme.MityraTeal
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPanelScreen(
    applications: List<CompanionApplicationEntity>,
    payments: List<MembershipPaymentEntity>,
    selectedFilterStatus: String,
    onFilterStatusChange: (String) -> Unit,
    onApproveApplication: (String, String) -> Unit,
    onRejectApplication: (String, String) -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedAppForInspection by remember { mutableStateOf<CompanionApplicationEntity?>(null) }
    var appToReject by remember { mutableStateOf<CompanionApplicationEntity?>(null) }
    var rejectionReasonInput by remember { mutableStateOf("") }

    val pendingCount = applications.count { it.status == "PENDING" }
    val approvedCount = applications.count { it.status == "APPROVED" }
    val totalRevenue = payments.filter { it.status == "SUCCESS" }.sumOf { it.amount }

    val displayedApplications = remember(applications, selectedFilterStatus) {
        when (selectedFilterStatus) {
            "PENDING" -> applications.filter { it.status == "PENDING" }
            "APPROVED" -> applications.filter { it.status == "APPROVED" }
            "REJECTED" -> applications.filter { it.status == "REJECTED" }
            else -> applications
        }
    }

    Scaffold(
        containerColor = MityraDarkBackground,
        topBar = {
            AdminTopBar(
                onClose = onClose
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Metrics Summary
            AdminMetricsHeader(
                totalApplications = applications.size,
                pendingCount = pendingCount,
                approvedCount = approvedCount,
                totalRevenue = totalRevenue
            )

            // Tabs Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MityraDarkSurface,
                contentColor = MityraCoral,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MityraCoral
                    )
                },
                divider = {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.testTag("admin_tab_applications"),
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Companion Reviews (${applications.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            if (pendingCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MityraGold)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "$pendingCount",
                                        color = Color.Black,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.testTag("admin_tab_payments"),
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CurrencyRupee, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("₹49 Razorpay Subs (${payments.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                )
            }

            // Tab Content
            if (selectedTab == 0) {
                // Applications Tab
                Column(modifier = Modifier.fillMaxSize()) {
                    // Filter Chips
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("ALL" to "All Applications", "PENDING" to "Pending Review", "APPROVED" to "Approved Hosts", "REJECTED" to "Rejected")
                        items(filters) { (key, label) ->
                            val isSelected = selectedFilterStatus == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) MityraCoral else MityraDarkSurfaceVariant)
                                    .border(
                                        1.dp,
                                        if (isSelected) MityraCoral else Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onFilterStatusChange(key) }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else MityraTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    if (displayedApplications.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No companion applications found for this filter.",
                                color = MityraTextMuted,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(displayedApplications, key = { it.id }) { app ->
                                CompanionApplicationCard(
                                    application = app,
                                    onApprove = { notes -> onApproveApplication(app.id, notes) },
                                    onReject = { appToReject = app },
                                    onInspect = { selectedAppForInspection = app }
                                )
                            }
                        }
                    }
                }
            } else {
                // ₹49 Razorpay Payments Ledger Tab
                MembershipPaymentsLedger(
                    payments = payments,
                    totalRevenue = totalRevenue
                )
            }
        }
    }

    // Inspect Dossier Dialog
    selectedAppForInspection?.let { app ->
        KycDossierDialog(
            application = app,
            onDismiss = { selectedAppForInspection = null },
            onApprove = {
                onApproveApplication(app.id, "Verified by Admin Operations after reviewing KYC dossier")
                selectedAppForInspection = null
            }
        )
    }

    // Reject Dialog
    appToReject?.let { app ->
        AlertDialog(
            onDismissRequest = { appToReject = null },
            containerColor = MityraDarkSurface,
            title = { Text("Reject Companion Application", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text(
                        "Are you sure you want to reject ${app.name}'s companion application? They will not be discoverable in the public Explore directory.",
                        color = MityraTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = { rejectionReasonInput = it },
                        label = { Text("Rejection Reason", color = MityraTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("e.g., Incomplete ID photo or safety boundary issue", color = MityraTextMuted, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MityraCoral,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedContainerColor = MityraDarkSurfaceVariant,
                            unfocusedContainerColor = MityraDarkSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reason = if (rejectionReasonInput.isNotBlank()) rejectionReasonInput else "Application rejected by Operations Admin."
                        onRejectApplication(app.id, reason)
                        appToReject = null
                        rejectionReasonInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Confirm Rejection", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { appToReject = null }) {
                    Text("Cancel", color = MityraTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun AdminTopBar(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF161922), Color(0xFF0F1117))
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = MityraGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mityra Admin Console",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = "Partner KYC Verification & Subscription Ops",
                        color = MityraTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF26D07C).copy(alpha = 0.15f))
                    .border(1.dp, Color(0xFF26D07C).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LIVE OPS",
                    color = Color(0xFF26D07C),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun AdminMetricsHeader(
    totalApplications: Int,
    pendingCount: Int,
    approvedCount: Int,
    totalRevenue: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MityraDarkSurface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetricCard(
            title = "Pending KYC",
            value = "$pendingCount",
            valueColor = if (pendingCount > 0) MityraGold else MityraTextPrimary,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = "Approved Hosts",
            value = "$approvedCount",
            valueColor = MityraSafetyGreen,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = "₹49 Sub Revenue",
            value = "₹$totalRevenue",
            valueColor = Color(0xFF0C83FF),
            modifier = Modifier.weight(1.2f)
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MityraDarkSurfaceVariant)
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(text = title, color = MityraTextMuted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = valueColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        }
    }
}

@Composable
private fun CompanionApplicationCard(
    application: CompanionApplicationEntity,
    onApprove: (String) -> Unit,
    onReject: () -> Unit,
    onInspect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_app_card_${application.id}"),
        colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when (application.status) {
                "PENDING" -> MityraGold.copy(alpha = 0.4f)
                "APPROVED" -> MityraSafetyGreen.copy(alpha = 0.3f)
                else -> Color.White.copy(alpha = 0.08f)
            }
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Avatar, Name, Age, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(MityraCoral, MityraGold)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = application.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${application.name}, ${application.age}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            if (application.isKycVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified ID",
                                    tint = MityraTeal,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MityraTextMuted, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${application.neighborhood}, ${application.city}",
                                color = MityraTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Status Badge
                val (statusBg, statusFg, statusText) = when (application.status) {
                    "PENDING" -> Triple(MityraGold.copy(alpha = 0.15f), MityraGold, "PENDING")
                    "APPROVED" -> Triple(MityraSafetyGreen.copy(alpha = 0.15f), MityraSafetyGreen, "APPROVED")
                    else -> Triple(Color(0xFFE53935).copy(alpha = 0.15f), Color(0xFFE53935), "REJECTED")
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusFg,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bio snippet
            Text(
                text = application.bio,
                color = MityraTextSecondary,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Info Chips Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MityraDarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Category: ${application.primaryCategory}",
                        color = MityraCoral,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MityraDarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "₹${application.hourlyRate}/hr",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MityraDarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ID: ${application.kycDocumentType}",
                        color = MityraTeal,
                        fontSize = 11.sp
                    )
                }
            }

            if (application.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MityraTextMuted, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Notes: ${application.adminNotes}",
                        color = MityraTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onInspect,
                    modifier = Modifier.testTag("admin_inspect_${application.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MityraTeal),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraTeal.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Dossier", fontSize = 11.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (application.status != "REJECTED") {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.testTag("admin_reject_${application.id}"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reject", fontSize = 11.sp)
                        }
                    }

                    if (application.status != "APPROVED") {
                        Button(
                            onClick = { onApprove("Verified and approved by Operations Admin") },
                            modifier = Modifier.testTag("admin_approve_${application.id}"),
                            colors = ButtonDefaults.buttonColors(containerColor = MityraSafetyGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Approve", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MembershipPaymentsLedger(
    payments: List<MembershipPaymentEntity>,
    totalRevenue: Int
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Razorpay Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2340)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A5F))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0C83FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("R", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Razorpay Subscriptions",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mityra VIP Club • ₹49/month recurring",
                            color = MityraGold,
                            fontSize = 11.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹$totalRevenue",
                            color = Color(0xFF26D07C),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${payments.size} Collected",
                            color = MityraTextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "TRANSACTION HISTORY (${payments.size})",
                color = MityraTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            )
        }

        items(payments, key = { it.paymentId }) { payment ->
            PaymentLedgerCard(payment = payment)
        }
    }
}

@Composable
private fun PaymentLedgerCard(payment: MembershipPaymentEntity) {
    val dateStr = remember(payment.timestamp) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(payment.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_payment_item_${payment.paymentId}"),
        colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0C83FF).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = Color(0xFF0C83FF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = payment.userName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = payment.userPhone,
                            color = MityraTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${payment.amount}.00",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MityraSafetyGreen, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "SUCCESS",
                            color = MityraSafetyGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(8.dp))

            // Payment metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Payment ID: ${payment.paymentId}", color = Color(0xFF528FF0), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    Text("Order: ${payment.orderId}", color = MityraTextMuted, fontSize = 10.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(payment.paymentMethod, color = MityraTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    Text(dateStr, color = MityraTextMuted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun KycDossierDialog(
    application: CompanionApplicationEntity,
    onDismiss: () -> Unit,
    onApprove: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MityraDarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = MityraTeal)
                Spacer(modifier = Modifier.width(8.dp))
                Text("KYC Dossier: ${application.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DossierField(label = "Government ID Type", value = application.kycDocumentType)
                DossierField(label = "Masked Document ID", value = application.kycIdMasked)
                DossierField(label = "Biometric Face Match", value = if (application.isKycVerified) "Verified (100% Match)" else "Pending")
                DossierField(label = "Phone & Contact", value = "${application.phone} • ${application.email}")
                DossierField(label = "Primary Category & Rate", value = "${application.primaryCategory} @ ₹${application.hourlyRate}/hr")
                DossierField(label = "Languages Spoken", value = application.languages)
                DossierField(label = "Safety Boundaries", value = application.boundaries)
                DossierField(label = "Interests & Lifestyle", value = application.interests)
            }
        },
        confirmButton = {
            if (application.status != "APPROVED") {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = MityraSafetyGreen)
                ) {
                    Text("Approve Partner", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = MityraTextSecondary)
            }
        }
    )
}

@Composable
private fun DossierField(label: String, value: String) {
    Column {
        Text(text = label, color = MityraTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
