package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EventCategory
import com.example.data.model.UserProfileEntity
import com.example.data.model.UserRole
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AvatarGradientOption(
    val name: String,
    val startColor: Long,
    val endColor: Long
)

val AVATAR_GRADIENTS = listOf(
    AvatarGradientOption("Sunset Coral", 0xFFFF5E62, 0xFF7928CA),
    AvatarGradientOption("Cyber Teal", 0xFF06D6A0, 0xFF1B9AAA),
    AvatarGradientOption("Royal Indigo", 0xFF6366F1, 0xFF9333EA),
    AvatarGradientOption("Golden Amber", 0xFFFFD166, 0xFFFF7B00),
    AvatarGradientOption("Emerald Mint", 0xFF10B981, 0xFF047857),
    AvatarGradientOption("Rose Velvet", 0xFFF43F5E, 0xFF881337)
)

val AVAILABLE_INTERESTS = listOf(
    "Fine Dining 🍷", "Modern Art 🎨", "Live Concerts 🎷", "Techno & EDM 🪩",
    "Film Screenings 🎬", "Coffee Hangouts ☕", "Standup Comedy 🎤", "Fitness 🏋️",
    "Book Clubs 📚", "Board Games 🎲", "Rooftop Lounges 🍸", "Road Trips 🚗"
)

val AVAILABLE_LANGUAGES = listOf(
    "English", "Hindi", "Marathi", "Punjabi", "Bengali", "Gujarati", "Tamil", "Telugu", "French"
)

val COMPANION_BOUNDARIES_PRESETS = listOf(
    "Public & licensed venues only",
    "Strictly platonic companion",
    "No home or private visits",
    "Safe travel coordination",
    "Respect personal space",
    "Alcohol-free outings welcome"
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelfRegisterScreen(
    existingProfile: UserProfileEntity?,
    onSaveProfile: (UserProfileEntity, Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // Form state initialized with existing profile if editing/registering
    var selectedRole by remember {
        mutableStateOf(
            if (existingProfile?.role == "COMPANION") UserRole.COMPANION else UserRole.MEMBER
        )
    }
    var name by remember { mutableStateOf(existingProfile?.name ?: "") }
    var ageText by remember { mutableStateOf(existingProfile?.age?.toString() ?: "25") }
    var selectedGender by remember { mutableStateOf(existingProfile?.gender ?: "Male") }
    var city by remember { mutableStateOf(existingProfile?.city ?: "Mumbai") }
    var neighborhood by remember { mutableStateOf(existingProfile?.neighborhood ?: "Bandra West") }
    var phone by remember { mutableStateOf(existingProfile?.phone ?: "+91 98201 44892") }
    var email by remember { mutableStateOf(existingProfile?.email ?: "user@mityra.app") }
    var bio by remember {
        mutableStateOf(
            existingProfile?.bio
                ?: "Social enthusiast looking forward to great company, memorable conversations, and exciting city events."
        )
    }

    // Interests
    val initialInterests = remember {
        existingProfile?.interests?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }?.toSet()
            ?: setOf("Fine Dining 🍷", "Modern Art 🎨", "Film Screenings 🎬")
    }
    var selectedInterests by remember { mutableStateOf(initialInterests) }

    // Languages
    val initialLanguages = remember {
        existingProfile?.languages?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }?.toSet()
            ?: setOf("English", "Hindi")
    }
    var selectedLanguages by remember { mutableStateOf(initialLanguages) }

    // Avatar Gradient
    var selectedGradientIndex by remember { mutableStateOf(0) }

    // Companion-specific fields
    var companionCategory by remember { mutableStateOf(EventCategory.DINNER_DATE) }
    var hourlyRateSlider by remember {
        mutableFloatStateOf(existingProfile?.hourlyRate?.toFloat() ?: 750f)
    }
    val initialBoundaries = remember {
        existingProfile?.boundaries?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }?.toSet()
            ?: setOf("Public & licensed venues only", "Strictly platonic companion", "Safe travel coordination")
    }
    var selectedBoundaries by remember { mutableStateOf(initialBoundaries) }

    // Safety & Emergency Contact
    var emergencyContactName by remember { mutableStateOf(existingProfile?.emergencyContactName ?: "Priya Sharma") }
    var emergencyContactPhone by remember { mutableStateOf(existingProfile?.emergencyContactPhone ?: "+91 98201 44892") }
    var safeWord by remember { mutableStateOf(existingProfile?.safeWord ?: "SUNSHINE") }

    // KYC Instant Verification Simulation
    var kycDocType by remember { mutableStateOf(existingProfile?.kycDocumentType ?: "Aadhaar Card") }
    var kycDocNumber by remember { mutableStateOf(existingProfile?.kycIdMasked ?: "XXXX-XXXX-8921") }
    var isKycVerified by remember { mutableStateOf(existingProfile?.isKycVerified ?: true) }
    var isKycVerifying by remember { mutableStateOf(false) }

    // Validation error
    var validationError by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("self_register_screen"),
        color = MityraDarkBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MityraDarkSurface)
                            .testTag("close_register_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (existingProfile != null) "Edit Self Profile" else "Self Registration",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Create your personal Mityra identity",
                            style = MaterialTheme.typography.bodySmall,
                            color = MityraTextSecondary
                        )
                    }
                }
            }

            // Scrollable Form
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Section 1: Role Selection Card
                Text(
                    text = "1. Select Registration Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose whether you are booking companions or hosting outings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MityraTextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Member Option Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = UserRole.MEMBER }
                            .testTag("role_member_option"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == UserRole.MEMBER) MityraDarkSurfaceVariant else MityraDarkSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (selectedRole == UserRole.MEMBER) 2.dp else 1.dp,
                            color = if (selectedRole == UserRole.MEMBER) MityraCoral else MityraCardBorder
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🥂", fontSize = 24.sp)
                                if (selectedRole == UserRole.MEMBER) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MityraCoral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Guest / Member",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Book verified companions for dinner, movies & parties",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Companion Option Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = UserRole.COMPANION }
                            .testTag("role_companion_option"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == UserRole.COMPANION) MityraDarkSurfaceVariant else MityraDarkSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (selectedRole == UserRole.COMPANION) 2.dp else 1.dp,
                            color = if (selectedRole == UserRole.COMPANION) MityraGold else MityraCardBorder
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "✨", fontSize = 24.sp)
                                if (selectedRole == UserRole.COMPANION) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MityraGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Host Companion",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Get listed on Explore, set hourly fee & accompany members",
                                style = MaterialTheme.typography.labelSmall,
                                color = MityraTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Avatar & Basic Information
                Text(
                    text = "2. Basic Identity Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Avatar preview and color palette
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val activeGradient = AVATAR_GRADIENTS[selectedGradientIndex]
                            val initials = remember(name) {
                                if (name.isNotBlank()) {
                                    name.split(" ")
                                        .take(2)
                                        .mapNotNull { it.firstOrNull()?.toString() }
                                        .joinToString("")
                                        .uppercase()
                                } else "U"
                            }

                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(activeGradient.startColor),
                                                Color(activeGradient.endColor)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Profile Avatar Theme",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Choose your profile badge gradient",
                                    color = MityraTextMuted,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    AVATAR_GRADIENTS.forEachIndexed { index, grad ->
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.linearGradient(
                                                        listOf(Color(grad.startColor), Color(grad.endColor))
                                                    )
                                                )
                                                .border(
                                                    width = if (selectedGradientIndex == index) 2.dp else 0.dp,
                                                    color = Color.White,
                                                    shape = CircleShape
                                                )
                                                .clickable { selectedGradientIndex = index }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Full Name
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; validationError = null },
                            label = { Text("Full Name (Display Name)") },
                            placeholder = { Text("e.g. Akshay Vankar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_name_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MityraCoral,
                                unfocusedBorderColor = MityraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = MityraCoral,
                                unfocusedLabelColor = MityraTextMuted
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Age & Gender Row
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = ageText,
                                onValueChange = { if (it.length <= 2 && it.all { char -> char.isDigit() }) ageText = it },
                                label = { Text("Age") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("register_age_input"),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = MityraCoral,
                                    unfocusedLabelColor = MityraTextMuted
                                )
                            )

                            Column(modifier = Modifier.weight(2f)) {
                                Text(
                                    text = "Gender",
                                    color = MityraTextMuted,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("Female", "Male", "Non-binary").forEach { g ->
                                        val isSelected = selectedGender.equals(g, ignoreCase = true)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) MityraCoral else MityraDarkSurfaceVariant)
                                                .clickable { selectedGender = g }
                                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = g,
                                                color = if (isSelected) Color.White else MityraTextSecondary,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // City Selection Chips
                        Text(
                            text = "City of Residence",
                            color = MityraTextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            listOf("Mumbai", "Bengaluru", "Delhi NCR", "Pune", "Hyderabad", "Kolkata", "Goa").forEach { c ->
                                val isSelected = city.equals(c, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) MityraGold.copy(alpha = 0.25f) else MityraDarkSurfaceVariant)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) MityraGold else MityraCardBorder,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { city = c }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = c,
                                        color = if (isSelected) MityraGold else MityraTextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Neighborhood
                        OutlinedTextField(
                            value = neighborhood,
                            onValueChange = { neighborhood = it },
                            label = { Text("Neighborhood / Locality") },
                            placeholder = { Text("e.g. Bandra West, Indiranagar, Cyber Hub") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MityraCoral,
                                unfocusedBorderColor = MityraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = MityraCoral,
                                unfocusedLabelColor = MityraTextMuted
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone & Email
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Mobile Number") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = MityraCoral,
                                    unfocusedLabelColor = MityraTextMuted
                                )
                            )

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Address") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = MityraCoral,
                                    unfocusedLabelColor = MityraTextMuted
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Bio & Personality Vibe
                Text(
                    text = "3. Bio & Lifestyle Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("About Me / Social Bio") },
                            placeholder = { Text("Share what you enjoy doing, conversational style, and outing vibes...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("register_bio_input"),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MityraCoral,
                                unfocusedBorderColor = MityraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = MityraCoral,
                                unfocusedLabelColor = MityraTextMuted
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interests Tags Multi-select
                        Text(
                            text = "Interests & Outing Vibes (Tap to select)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AVAILABLE_INTERESTS.forEach { interest ->
                                val isSelected = selectedInterests.contains(interest)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) MityraCoral.copy(alpha = 0.25f)
                                            else MityraDarkSurfaceVariant
                                        )
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) MityraCoral else MityraCardBorder,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            selectedInterests = if (isSelected) {
                                                selectedInterests - interest
                                            } else {
                                                selectedInterests + interest
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MityraCoral,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text(
                                            text = interest,
                                            color = if (isSelected) Color.White else MityraTextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Languages Multi-select
                        Text(
                            text = "Languages You Speak",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AVAILABLE_LANGUAGES.forEach { lang ->
                                val isSelected = selectedLanguages.contains(lang)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) MityraVerifiedTeal.copy(alpha = 0.2f)
                                            else MityraDarkSurfaceVariant
                                        )
                                        .border(
                                            width = if (isSelected) 1.dp else 0.5.dp,
                                            color = if (isSelected) MityraVerifiedTeal else MityraCardBorder,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            selectedLanguages = if (isSelected) {
                                                selectedLanguages - lang
                                            } else {
                                                selectedLanguages + lang
                                            }
                                        }
                                        .padding(horizontal = 9.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = lang,
                                        color = if (isSelected) MityraVerifiedTeal else MityraTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 4: If Companion, Show Rates and Boundaries
                AnimatedVisibility(visible = selectedRole == UserRole.COMPANION) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "4. Companion Outing Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MityraGold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                            shape = RoundedCornerShape(18.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MityraGold.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Primary Outing Specialization",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    listOf(
                                        EventCategory.DINNER_DATE,
                                        EventCategory.MOVIE_PARTNER,
                                        EventCategory.EVENT_COMPANION,
                                        EventCategory.PARTY_PARTNER,
                                        EventCategory.CASUAL_HANGOUT
                                    ).forEach { cat ->
                                        val isSelected = companionCategory == cat
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) MityraGold.copy(alpha = 0.25f) else MityraDarkSurfaceVariant)
                                                .border(
                                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                                    color = if (isSelected) MityraGold else MityraCardBorder,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { companionCategory = cat }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = cat.emoji, fontSize = 14.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = cat.title,
                                                    color = if (isSelected) MityraGold else MityraTextSecondary,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Hourly Rate
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Hourly Rate",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "₹${hourlyRateSlider.toInt()} / hour",
                                        color = MityraGold,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                                Slider(
                                    value = hourlyRateSlider,
                                    onValueChange = { hourlyRateSlider = it },
                                    valueRange = 400f..2500f,
                                    steps = 20,
                                    colors = SliderDefaults.colors(
                                        thumbColor = MityraGold,
                                        activeTrackColor = MityraGold,
                                        inactiveTrackColor = MityraDarkSurfaceVariant
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Boundaries
                                Text(
                                    text = "Your Companion Boundaries & Rules",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    COMPANION_BOUNDARIES_PRESETS.forEach { rule ->
                                        val isSelected = selectedBoundaries.contains(rule)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) MityraCoral.copy(alpha = 0.2f)
                                                    else MityraDarkSurfaceVariant
                                                )
                                                .border(
                                                    width = if (isSelected) 1.dp else 0.5.dp,
                                                    color = if (isSelected) MityraCoral else MityraCardBorder,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    selectedBoundaries = if (isSelected) {
                                                        selectedBoundaries - rule
                                                    } else {
                                                        selectedBoundaries + rule
                                                    }
                                                }
                                                .padding(horizontal = 9.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = rule,
                                                color = if (isSelected) Color.White else MityraTextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 5: Instant KYC Verification & Safety Guardian
                Text(
                    text = if (selectedRole == UserRole.COMPANION) "5. Instant KYC & Guardian Safety" else "4. Instant KYC & Guardian Safety",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // DigiLocker / Aadhaar Verification Simulation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = if (isKycVerified) MityraVerifiedTeal else MityraGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "DigiLocker ID Authentication",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Text(
                                    text = if (isKycVerified) "Verified: Aadhaar Identity Authenticated" else "Requires government ID verification",
                                    color = if (isKycVerified) MityraVerifiedTeal else MityraTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = {
                                    if (!isKycVerifying) {
                                        isKycVerifying = true
                                        coroutineScope.launch {
                                            delay(900)
                                            isKycVerified = true
                                            isKycVerifying = false
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isKycVerified) MityraVerifiedTeal.copy(alpha = 0.2f) else MityraCoral
                                ),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !isKycVerifying
                            ) {
                                if (isKycVerifying) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = if (isKycVerified) "✓ Verified" else "Verify Now",
                                        color = if (isKycVerified) MityraVerifiedTeal else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(MityraCardBorder))
                        Spacer(modifier = Modifier.height(14.dp))

                        // Emergency Contact
                        Text(
                            text = "Emergency Contact (Family / Close Friend)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = emergencyContactName,
                                onValueChange = { emergencyContactName = it },
                                label = { Text("Contact Name") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = MityraCoral,
                                    unfocusedLabelColor = MityraTextMuted
                                )
                            )

                            OutlinedTextField(
                                value = emergencyContactPhone,
                                onValueChange = { emergencyContactPhone = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = MityraCoral,
                                    unfocusedLabelColor = MityraTextMuted
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Safe Word
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Panic Safe Word",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Typing this word silently triggers SOS assistance",
                                    color = MityraTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            OutlinedTextField(
                                value = safeWord,
                                onValueChange = { safeWord = it.uppercase() },
                                modifier = Modifier.width(130.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraGold,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = MityraGold,
                                    unfocusedTextColor = MityraGold
                                )
                            )
                        }
                    }
                }

                // Validation error display
                validationError?.let { err ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MityraCoral.copy(alpha = 0.2f))
                            .border(1.dp, MityraCoral, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(text = err, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // Bottom Submit Button Bar
            Surface(
                color = MityraDarkSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (selectedRole == UserRole.COMPANION) "Companion Registration" else "Member Registration",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (selectedRole == UserRole.COMPANION) "Will be listed on Explore" else "Instant verified status",
                            color = MityraVerifiedTeal,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                validationError = "Please enter your full name."
                                return@Button
                            }
                            val parsedAge = ageText.toIntOrNull() ?: 24
                            if (parsedAge < 18) {
                                validationError = "You must be 18 or older to register on Mityra."
                                return@Button
                            }

                            val activeGrad = AVATAR_GRADIENTS[selectedGradientIndex]
                            val newProfile = UserProfileEntity(
                                id = "primary_user",
                                name = name.trim(),
                                role = selectedRole.name,
                                age = parsedAge,
                                gender = selectedGender,
                                city = city,
                                neighborhood = neighborhood.ifBlank { "Downtown" },
                                phone = phone,
                                email = email,
                                bio = bio.trim(),
                                interests = selectedInterests.joinToString(", "),
                                languages = selectedLanguages.joinToString(", "),
                                emergencyContactName = emergencyContactName,
                                emergencyContactPhone = emergencyContactPhone,
                                safeWord = safeWord.ifBlank { "SUNSHINE" },
                                isKycVerified = isKycVerified,
                                kycDocumentType = kycDocType,
                                kycIdMasked = kycDocNumber,
                                avatarGradientStart = activeGrad.startColor,
                                avatarGradientEnd = activeGrad.endColor,
                                primaryCategory = companionCategory.id,
                                hourlyRate = hourlyRateSlider.toInt(),
                                boundaries = selectedBoundaries.joinToString(", ")
                            )

                            onSaveProfile(newProfile, selectedRole == UserRole.COMPANION)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == UserRole.COMPANION) MityraGold else MityraCoral
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("submit_register_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (selectedRole == UserRole.COMPANION) Color.Black else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (existingProfile != null) "Save & Update Profile" else "Complete Registration",
                            color = if (selectedRole == UserRole.COMPANION) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
