package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.MityraCardBorder
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraPurple
import com.example.ui.theme.MityraSafetyGreen
import com.example.ui.theme.MityraSosRed
import com.example.ui.theme.MityraTeal
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary

@Composable
fun AuthScreen(
    authScreenMode: String, // "LOGIN", "SIGNUP"
    onAuthModeChange: (String) -> Unit,
    loginMethod: String, // "MOBILE", "EMAIL"
    onLoginMethodChange: (String) -> Unit,
    selectedRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    phoneInput: String,
    onPhoneInputChange: (String) -> Unit,
    otpInput: String,
    onOtpInputChange: (String) -> Unit,
    isOtpSent: Boolean,
    onSendOtp: () -> Unit,
    onAutoFillOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    emailInput: String,
    onEmailInputChange: (String) -> Unit,
    passwordInput: String,
    onPasswordInputChange: (String) -> Unit,
    nameInput: String,
    onNameInputChange: (String) -> Unit,
    cityInput: String,
    onCityInputChange: (String) -> Unit,
    errorMessage: String?,
    successMessage: String?,
    onLoginWithEmail: () -> Unit,
    onSignupSubmit: () -> Unit,
    onQuickFillAdmin: () -> Unit,
    onQuickDemoLogin: (UserRole) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MityraDarkBackground),
        color = MityraDarkBackground
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand Header & Logo
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(MityraCoral, MityraPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "MITYRA",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "Verified Social & Event Companions",
                    color = MityraTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF132034))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shield",
                        tint = MityraSafetyGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Govt ID Verified • Platonic Only • 24/7 SOS",
                        color = MityraSafetyGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Error or Success Banner
            if (errorMessage != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MityraSosRed.copy(alpha = 0.15f))
                            .border(1.dp, MityraSosRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error",
                            tint = MityraSosRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFFFFB4BC),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (successMessage != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MityraSafetyGreen.copy(alpha = 0.15f))
                            .border(1.dp, MityraSafetyGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MityraSafetyGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = successMessage,
                            color = Color(0xFFB4FCE3),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Segmented Switch: Sign In vs Sign Up
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MityraDarkSurfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (authScreenMode == "LOGIN") MityraCoral else Color.Transparent
                            )
                            .clickable { onAuthModeChange("LOGIN") }
                            .padding(vertical = 10.dp)
                            .testTag("tab_login"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign In",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (authScreenMode == "SIGNUP") MityraCoral else Color.Transparent
                            )
                            .clickable { onAuthModeChange("SIGNUP") }
                            .padding(vertical = 10.dp)
                            .testTag("tab_signup"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Create Account",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Role Selector Cards
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "SELECT YOUR ACCOUNT ROLE",
                        color = MityraTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // User / Member Role
                        RoleCard(
                            modifier = Modifier.weight(1f),
                            role = UserRole.MEMBER,
                            icon = Icons.Default.Person,
                            title = "User",
                            subtitle = "Event Guest",
                            isSelected = selectedRole == UserRole.MEMBER,
                            badgeColor = MityraCoral,
                            onClick = { onRoleSelect(UserRole.MEMBER) },
                            testTag = "role_member"
                        )

                        // Companion Role
                        RoleCard(
                            modifier = Modifier.weight(1f),
                            role = UserRole.COMPANION,
                            icon = Icons.Default.Star,
                            title = "Companion",
                            subtitle = "Verified Host",
                            isSelected = selectedRole == UserRole.COMPANION,
                            badgeColor = MityraGold,
                            onClick = { onRoleSelect(UserRole.COMPANION) },
                            testTag = "role_companion"
                        )

                        // Admin Role
                        RoleCard(
                            modifier = Modifier.weight(1f),
                            role = UserRole.ADMIN,
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Admin",
                            subtitle = "Operations",
                            isSelected = selectedRole == UserRole.ADMIN,
                            badgeColor = Color(0xFF38BDF8),
                            onClick = { onRoleSelect(UserRole.ADMIN) },
                            testTag = "role_admin"
                        )
                    }
                }
            }

            // Form Content: LOGIN vs SIGNUP
            if (authScreenMode == "LOGIN") {
                // Admin Protected Banner if Admin role selected
                if (selectedRole == UserRole.ADMIN) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2340)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0C83FF)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🛡️", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Protected Admin Portal",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF0C83FF).copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "RESTRICTED",
                                            color = Color(0xFF38BDF8),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Platform administrative console for companion application approvals, user records, and ₹49 payments auditing.",
                                    color = MityraTextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = onQuickFillAdmin,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C83FF)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quick_fill_admin_button")
                                ) {
                                    Text(
                                        text = "⚡ Quick-fill Demo: admin@mityra.com / admin123",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Login Method Tabs (Mobile OTP vs Email Password)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (loginMethod == "MOBILE") Color(0xFF2A2340) else MityraDarkSurface
                                )
                                .border(
                                    1.dp,
                                    if (loginMethod == "MOBILE") MityraCoral else MityraCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onLoginMethodChange("MOBILE") }
                                .padding(vertical = 10.dp)
                                .testTag("login_method_mobile"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Mobile",
                                    tint = if (loginMethod == "MOBILE") MityraCoral else MityraTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Mobile & OTP",
                                    color = if (loginMethod == "MOBILE") Color.White else MityraTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (loginMethod == "EMAIL") Color(0xFF2A2340) else MityraDarkSurface
                                )
                                .border(
                                    1.dp,
                                    if (loginMethod == "EMAIL") MityraCoral else MityraCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onLoginMethodChange("EMAIL") }
                                .padding(vertical = 10.dp)
                                .testTag("login_method_email"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = if (loginMethod == "EMAIL") MityraCoral else MityraTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Email & Password",
                                    color = if (loginMethod == "EMAIL") Color.White else MityraTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Method 1: Mobile & OTP Flow
                if (loginMethod == "MOBILE") {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Mobile Verification",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = onPhoneInputChange,
                                    label = { Text("Mobile Number") },
                                    placeholder = { Text("+91 98201 44892") },
                                    leadingIcon = {
                                        Text(
                                            text = "🇮🇳 +91",
                                            color = MityraTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_phone_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MityraCoral,
                                        unfocusedBorderColor = MityraCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = MityraDarkSurfaceVariant,
                                        unfocusedContainerColor = MityraDarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                if (!isOtpSent) {
                                    Button(
                                        onClick = onSendOtp,
                                        colors = ButtonDefaults.buttonColors(containerColor = MityraCoral),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("send_otp_button")
                                    ) {
                                        Text(
                                            text = "Send 6-Digit OTP →",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    // OTP Sent view
                                    Text(
                                        text = "Enter 6-digit OTP code",
                                        color = MityraTextSecondary,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )

                                    OutlinedTextField(
                                        value = otpInput,
                                        onValueChange = onOtpInputChange,
                                        label = { Text("6-Digit OTP") },
                                        placeholder = { Text("123456") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("auth_otp_input"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MityraCoral,
                                            unfocusedBorderColor = MityraCardBorder,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedContainerColor = MityraDarkSurfaceVariant,
                                            unfocusedContainerColor = MityraDarkSurfaceVariant
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Auto-fill demo code?",
                                            color = MityraTextMuted,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "Auto-fill 123456",
                                            color = MityraTeal,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { onAutoFillOtp() }
                                                .testTag("autofill_otp_button")
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = onVerifyOtp,
                                        colors = ButtonDefaults.buttonColors(containerColor = MityraSafetyGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("verify_otp_login_button")
                                    ) {
                                        Text(
                                            text = "Verify OTP & Continue",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Method 2: Email & Password Flow
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (selectedRole == UserRole.ADMIN) "Admin Sign In" else "Sign In with Password",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = emailInput,
                                    onValueChange = onEmailInputChange,
                                    label = { Text("Email Address") },
                                    placeholder = { Text("your.email@example.com") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email",
                                            tint = MityraTextSecondary
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_email_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MityraCoral,
                                        unfocusedBorderColor = MityraCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = MityraDarkSurfaceVariant,
                                        unfocusedContainerColor = MityraDarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = passwordInput,
                                    onValueChange = onPasswordInputChange,
                                    label = { Text("Password") },
                                    placeholder = { Text("••••••••") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Password",
                                            tint = MityraTextSecondary
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                            Icon(
                                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Toggle Password",
                                                tint = MityraTextSecondary
                                            )
                                        }
                                    },
                                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("auth_password_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MityraCoral,
                                        unfocusedBorderColor = MityraCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = MityraDarkSurfaceVariant,
                                        unfocusedContainerColor = MityraDarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = onLoginWithEmail,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedRole == UserRole.ADMIN) Color(0xFF0C83FF) else MityraCoral
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("login_submit_button")
                                    ) {
                                    Text(
                                        text = if (selectedRole == UserRole.ADMIN) "Authenticate Admin Console →" else "Sign In to Mityra →",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // SIGN UP FORM
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MityraDarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Create Your ${if (selectedRole == UserRole.COMPANION) "Host" else "Member"} Account",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = onNameInputChange,
                                label = { Text("Full Legal Name") },
                                placeholder = { Text("e.g. Aryan Varma") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Name",
                                        tint = MityraTextSecondary
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_name_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = onPhoneInputChange,
                                label = { Text("Mobile Number") },
                                placeholder = { Text("+91 98201 44892") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Phone",
                                        tint = MityraTextSecondary
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_phone_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = onEmailInputChange,
                                label = { Text("Email Address") },
                                placeholder = { Text("aryan.varma@example.com") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = MityraTextSecondary
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_email_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = onPasswordInputChange,
                                label = { Text("Create Password") },
                                placeholder = { Text("Minimum 4 characters") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Password",
                                        tint = MityraTextSecondary
                                    )
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_password_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = cityInput,
                                onValueChange = onCityInputChange,
                                label = { Text("Operating City") },
                                placeholder = { Text("Mumbai") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_city_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MityraCoral,
                                    unfocusedBorderColor = MityraCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = MityraDarkSurfaceVariant,
                                    unfocusedContainerColor = MityraDarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Checked",
                                    tint = MityraSafetyGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Includes 30-day VIP Membership (₹49 waived on onboarding)",
                                    color = MityraSafetyGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onSignupSubmit,
                                colors = ButtonDefaults.buttonColors(containerColor = MityraCoral),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signup_submit_button")
                            ) {
                                Text(
                                    text = "Agree & Create Account →",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Quick Demo Instant Logins
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MityraDarkSurface)
                        .border(1.dp, MityraCardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "⚡ INSTANT DEMO EVALUATION",
                        color = MityraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to switch role immediately without entering credentials:",
                        color = MityraTextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onQuickDemoLogin(UserRole.MEMBER) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF231E34)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MityraCoral.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_login_member")
                        ) {
                            Text("👤 User", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onQuickDemoLogin(UserRole.COMPANION) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF231E34)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MityraGold.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_login_companion")
                        ) {
                            Text("🌟 Host", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onQuickDemoLogin(UserRole.ADMIN) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2340)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0C83FF)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_login_admin")
                        ) {
                            Text("🛡️ Admin", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Legal Footer
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mityra adheres strictly to social, platonic, and public safety guidelines. All companions undergo DigiLocker ID verification and background vetting.",
                    color = MityraTextMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    role: UserRole,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    badgeColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF2A223E) else MityraDarkSurface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) badgeColor else MityraCardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) badgeColor.copy(alpha = 0.2f) else Color(0xFF231E34)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) badgeColor else MityraTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else MityraTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = if (isSelected) badgeColor else MityraTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
