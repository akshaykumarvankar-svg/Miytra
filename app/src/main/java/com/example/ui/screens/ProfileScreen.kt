package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.theme.MityraCardBorder
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraSafetyGreen
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary
import com.example.ui.theme.MityraVerifiedTeal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    onEditOrRegisterClick: () -> Unit,
    onSubscribeMembershipClick: () -> Unit = {},
    onOpenAdminPanelClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isGpsSharingEnabled by remember { mutableStateOf(true) }

    val name = userProfile?.name ?: "Rohan Sharma"
    val isCompanion = userProfile?.role == "COMPANION"
    val city = userProfile?.city ?: "Mumbai"
    val neighborhood = userProfile?.neighborhood ?: "Bandra West"
    val initials = remember(name) {
        name.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.toString() }
            .joinToString("")
            .uppercase()
            .ifBlank { "RS" }
    }

    val gradientStart = Color(userProfile?.avatarGradientStart ?: 0xFFFF5E62)
    val gradientEnd = Color(userProfile?.avatarGradientEnd ?: 0xFF7928CA)

    val interestList = remember(userProfile?.interests) {
        userProfile?.interests?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
            ?: listOf("Fine Dining 🍷", "Modern Art 🎨", "Film Screenings 🎬")
    }

    val languageList = remember(userProfile?.languages) {
        userProfile?.languages?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
            ?: listOf("English", "Hindi")
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        color = MityraDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 90.dp)
        ) {
            // Header Row with Title and Self-Register/Edit Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Button(
                    onClick = onEditOrRegisterClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompanion) MityraGold else MityraCoral
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("self_register_profile_button")
                ) {
                    Icon(
                        imageVector = if (userProfile != null) Icons.Default.Edit else Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = if (isCompanion) Color.Black else Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (userProfile != null) "Edit Profile" else "Self Register",
                        color = if (isCompanion) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Identity Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(gradientStart, gradientEnd))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = if (isCompanion) Icons.Default.Shield else Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = if (isCompanion) MityraGold else MityraVerifiedTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = if (isCompanion) "Mityra Shield Level 3 Verified Companion" else "Aadhaar KYC Verified Member",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isCompanion) MityraGold else MityraVerifiedTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$neighborhood, $city • ${userProfile?.gender ?: "Member"} • Age ${userProfile?.age ?: 25}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraTextMuted,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isCompanion) MityraGold.copy(alpha = 0.2f) else MityraCoral.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isCompanion) "ROLE: HOST" else "ROLE: MEMBER",
                                        color = if (isCompanion) MityraGold else MityraCoral,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = userProfile?.phone ?: "+91 98201 44892",
                                    color = MityraTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = userProfile?.email ?: "rohan.sharma@example.com",
                                color = MityraTeal,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bio display
                    if (!userProfile?.bio.isNullOrBlank()) {
                        Text(
                            text = userProfile!!.bio,
                            style = MaterialTheme.typography.bodySmall,
                            color = MityraTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Interests chips
                    if (interestList.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            interestList.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MityraDarkSurfaceVariant)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = tag, color = MityraTextSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Stats strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MityraDarkSurfaceVariant)
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (isCompanion) "1" else "5", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(if (isCompanion) "Hosting" else "Outings", color = MityraTextMuted, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(MityraCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = "Rating", tint = MityraGold, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("5.0", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Text("Trust Score", color = MityraTextMuted, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(MityraCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isCompanion) "₹${userProfile?.hourlyRate ?: 750}/hr" else "₹4,500",
                                color = MityraGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(if (isCompanion) "Rate Card" else "Escrow Wallet", color = MityraTextMuted, fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Razorpay VIP Membership Card (₹49/month) ---
            val hasActiveSub = userProfile?.hasActiveMembership == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("razorpay_membership_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (hasActiveSub) Color(0xFF14291F) else Color(0xFF0C2340)
                ),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (hasActiveSub) MityraSafetyGreen.copy(alpha = 0.5f) else Color(0xFF0C83FF).copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (hasActiveSub) MityraSafetyGreen.copy(alpha = 0.2f) else Color(0xFF0C83FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (hasActiveSub) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (hasActiveSub) MityraSafetyGreen else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (hasActiveSub) "Mityra VIP Club Active" else "Mityra VIP Membership",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (hasActiveSub) MityraSafetyGreen else MityraGold)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = if (hasActiveSub) "ACTIVE" else "₹49/mo",
                                            color = Color.Black,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                                Text(
                                    text = if (hasActiveSub) "Auto-renewal via Razorpay • 30 Days Valid" else "Powered by Razorpay Payment Gateway",
                                    color = if (hasActiveSub) MityraSafetyGreen else Color(0xFF528FF0),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (hasActiveSub)
                            "• 0% Platform Convenience Fees\n• Verified Shield Host Priority Badge\n• 24x7 Emergency SOS Priority Dispatch"
                        else
                            "Unlock 0% booking fees, direct VIP companion messaging, verified profile badge, and 24x7 priority emergency response for just ₹49/month.",
                        color = MityraTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (hasActiveSub) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.25f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Payment ID: ${userProfile?.membershipPaymentId ?: "pay_RzpMityra"}", color = MityraTextMuted, fontSize = 11.sp)
                            Text("Auto-Renews in 30 Days", color = MityraGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Button(
                            onClick = onSubscribeMembershipClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("profile_subscribe_razorpay_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C83FF)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("R", color = Color(0xFF0C83FF), fontWeight = FontWeight.Black, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Subscribe via Razorpay (₹49/mo)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- Admin & Partner Operations Console Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_admin_console_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26)),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MityraGold.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MityraGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = MityraGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Admin & Ops Console",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MityraCoral.copy(alpha = 0.2f))
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "PORTAL",
                                            color = MityraCoral,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                                Text(
                                    text = "Companion KYC approval & ₹49 revenue tracking",
                                    color = MityraTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Review companion self-registration applications, approve or reject host profiles, inspect government ID dossiers, and monitor live Razorpay ₹49 subscription collections.",
                        color = MityraTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onOpenAdminPanelClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("profile_open_admin_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MityraGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open Admin Panel", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Companion Active Listing Card (if registered as companion)
            if (isCompanion) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MityraVerifiedTeal)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Host Companion Status: Active",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "₹${userProfile?.hourlyRate ?: 750}/hr",
                                color = MityraGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your profile is listed live on Mityra Explore catalog. Verified members can view your profile and request outings.",
                            color = MityraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Safety Settings Section
            Text(
                text = "Personal Safety Configuration",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Emergency contact
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEditOrRegisterClick() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Primary Emergency Contact", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "${userProfile?.emergencyContactName ?: "Priya Sharma"} (${userProfile?.emergencyContactPhone ?: "+91 98201 44892"})",
                                color = MityraTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MityraCoral, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(MityraCardBorder))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Live GPS Tracking during outings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto GPS Guardian", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Shares encrypted live location with Mityra during active bookings", color = MityraTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = isGpsSharingEnabled,
                            onCheckedChange = { isGpsSharingEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MityraCoral)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(MityraCardBorder))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Safe Word Trigger
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Panic Safe Word", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Typing this safe word silently triggers immediate response team", color = MityraTextSecondary, fontSize = 11.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MityraGold.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = userProfile?.safeWord ?: "SUNSHINE",
                                color = MityraGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Self-Register Callout Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditOrRegisterClick() },
                colors = CardDefaults.cardColors(containerColor = MityraDarkSurfaceVariant),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MityraCoral.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = MityraCoral,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Update Self Profile / Switch Role",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Register as a Companion Host or adjust your Member preferences",
                            color = MityraTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = MityraTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // General Settings List
            Text(
                text = "Preferences & Support",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    listOf(
                        "Mityra Escrow Wallet & Invoices",
                        "DigiLocker KYC Identity Certificate",
                        "Community Guidelines & Code of Conduct",
                        "24x7 Grievance & Safety Support Officer"
                    ).forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Item click */ }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item, color = MityraTextPrimary, fontSize = 13.sp)
                            Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = MityraTextMuted, modifier = Modifier.size(18.dp))
                        }
                        if (index < 3) {
                            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(MityraCardBorder))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Active Sign Out Button
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E1520)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.6f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("logout_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out of Account",
                        color = Color(0xFFFF5252),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mityra v2.5.0 • Encrypted Social Companion Network",
                    color = MityraTextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
