package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenTab
import com.example.ui.theme.MityraCardBorder
import com.example.ui.theme.MityraCoral
import com.example.ui.theme.MityraDarkBackground
import com.example.ui.theme.MityraDarkSurface
import com.example.ui.theme.MityraTextMuted
import com.example.ui.theme.MityraTextPrimary

@Composable
fun MityraBottomNav(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    activeBookingsCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = MityraCardBorder)
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar"),
        containerColor = MityraDarkSurface,
        tonalElevation = 8.dp
    ) {
        // Explore Tab
        NavigationBarItem(
            selected = currentTab == ScreenTab.EXPLORE,
            onClick = { onTabSelected(ScreenTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                    contentDescription = "Explore",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Explore",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MityraCoral,
                selectedTextColor = MityraCoral,
                unselectedIconColor = MityraTextMuted,
                unselectedTextColor = MityraTextMuted,
                indicatorColor = MityraCoral.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_explore")
        )

        // Bookings Tab
        NavigationBarItem(
            selected = currentTab == ScreenTab.BOOKINGS,
            onClick = { onTabSelected(ScreenTab.BOOKINGS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (activeBookingsCount > 0) {
                            Badge(
                                containerColor = MityraCoral,
                                contentColor = Color.White
                            ) {
                                Text(activeBookingsCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.BOOKINGS) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                        contentDescription = "Bookings",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "Bookings",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MityraCoral,
                selectedTextColor = MityraCoral,
                unselectedIconColor = MityraTextMuted,
                unselectedTextColor = MityraTextMuted,
                indicatorColor = MityraCoral.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_bookings")
        )

        // Safety Suite Tab
        NavigationBarItem(
            selected = currentTab == ScreenTab.SAFETY,
            onClick = { onTabSelected(ScreenTab.SAFETY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.SAFETY) Icons.Filled.Security else Icons.Outlined.Security,
                    contentDescription = "Safety Center",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Safety",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MityraCoral,
                selectedTextColor = MityraCoral,
                unselectedIconColor = MityraTextMuted,
                unselectedTextColor = MityraTextMuted,
                indicatorColor = MityraCoral.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_safety")
        )

        // Profile Tab
        NavigationBarItem(
            selected = currentTab == ScreenTab.PROFILE,
            onClick = { onTabSelected(ScreenTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MityraCoral,
                selectedTextColor = MityraCoral,
                unselectedIconColor = MityraTextMuted,
                unselectedTextColor = MityraTextMuted,
                indicatorColor = MityraCoral.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
