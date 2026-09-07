package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Companion
import com.example.data.model.CompanionGender
import com.example.data.model.EventCategory
import com.example.ui.components.CompanionCard
import com.example.ui.theme.MityraCardBorder
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraDarkSurfaceVariant
import com.example.ui.theme.MityraGold
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary
import com.example.ui.theme.MityraTextSecondary
import com.example.ui.theme.MityraVerifiedTeal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    companions: List<Companion>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: EventCategory,
    onCategorySelected: (EventCategory) -> Unit,
    selectedGender: CompanionGender,
    onGenderSelected: (CompanionGender) -> Unit,
    maxHourlyRate: Int,
    onMaxHourlyRateSelected: (Int) -> Unit,
    onCompanionClick: (Companion) -> Unit,
    onBookClick: (Companion) -> Unit,
    onChatClick: (Companion) -> Unit,
    onVerifyClick: (Companion) -> Unit,
    onSelfRegisterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MityraDarkBackground)
            .testTag("explore_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF2C1338),
                                Color(0xFF1B142E),
                                Color(0xFF131A33)
                            )
                        )
                    )
                    .border(1.dp, MityraCoral.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Hire Verified Companions ✨",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MityraVerifiedTeal.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "100% KYC Verified",
                                color = MityraVerifiedTeal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Book charming, verified companions for dinners, movies, concerts & galas on a paid hourly basis.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MityraTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Self Register Callout Strip inside Hero
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable { onSelfRegisterClick() }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚀", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create profile / Self-register as companion",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Join Now →",
                            color = MityraCoral,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search Bar & Filter Toggle
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = "Search by vibe, interests, or area...",
                            color = MityraTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MityraTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MityraTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("search_text_field"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MityraDarkSurface,
                        unfocusedContainerColor = MityraDarkSurface,
                        focusedBorderColor = MityraCoral,
                        unfocusedBorderColor = MityraCardBorder,
                        focusedTextColor = MityraTextPrimary,
                        unfocusedTextColor = MityraTextPrimary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
                )

                // Quick Filter Pill Toggle
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (showFilterSheet) MityraCoral else MityraDarkSurface)
                        .border(1.dp, MityraCardBorder, RoundedCornerShape(16.dp))
                        .clickable { showFilterSheet = !showFilterSheet }
                        .testTag("filter_toggle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filters",
                        tint = if (showFilterSheet) Color.White else MityraTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Expanded Filter Options (Gender & Hourly Rate)
        if (showFilterSheet) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MityraDarkSurfaceVariant),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MityraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Companion Gender Filter
                        Text(
                            text = "Companion Gender",
                            style = MaterialTheme.typography.labelMedium,
                            color = MityraTextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CompanionGender.values().forEach { gender ->
                                val isSelected = selectedGender == gender
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) MityraCoral else MityraDarkSurface)
                                        .border(
                                            1.dp,
                                            if (isSelected) MityraCoral else MityraCardBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { onGenderSelected(gender) }
                                        .padding(vertical = 8.dp)
                                        .testTag("filter_gender_${gender.name}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = gender.displayName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) Color.White else MityraTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Hourly Rate Filter
                        Text(
                            text = "Max Hourly Rate (Budget)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MityraTextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val rateOptions = listOf(500, 750, 1000, 1500)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rateOptions.forEach { rate ->
                                val isSelected = maxHourlyRate == rate
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) MityraGold else MityraDarkSurface)
                                        .border(
                                            1.dp,
                                            if (isSelected) MityraGold else MityraCardBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { onMaxHourlyRateSelected(rate) }
                                        .padding(vertical = 8.dp)
                                        .testTag("filter_rate_$rate"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (rate >= 1500) "Any" else "≤₹$rate",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) Color.Black else MityraTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Filter Chips
        item {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventCategory.values().forEach { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Brush.linearGradient(listOf(MityraCoral, Color(0xFFE02874)))
                                    else Brush.linearGradient(listOf(MityraDarkSurface, MityraDarkSurface))
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else MityraCardBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { onCategorySelected(category) }
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                                .testTag("category_chip_${category.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = category.emoji,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = category.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (isSelected) Color.White else MityraTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title & Result Count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Verified Companions Near You",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "${companions.size} Available",
                    style = MaterialTheme.typography.labelMedium,
                    color = MityraGold,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Companion Profile Cards Feed
        if (companions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🔍 No Companions Found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MityraTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try adjusting your gender, city, or rate filters.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MityraTextSecondary
                        )
                    }
                }
            }
        } else {
            items(companions, key = { it.id }) { companion ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    CompanionCard(
                        companion = companion,
                        onClick = { onCompanionClick(companion) },
                        onBookClick = { onBookClick(companion) },
                        onChatClick = { onChatClick(companion) },
                        onVerifyClick = { onVerifyClick(companion) }
                    )
                }
            }
        }
    }
}
