package com.example.petshield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

enum class BottomTab {
    PROFILE, HOME, CITAS
}

@Composable
fun PetShieldBottomBar(
    currentTab: BottomTab,
    onNavigateToTab: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Perfil
            if (currentTab == BottomTab.PROFILE) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onNavigateToTab(BottomTab.PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_user),
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(onClick = { onNavigateToTab(BottomTab.PROFILE) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_user),
                        contentDescription = "Perfil",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Tab 2: Home
            if (currentTab == BottomTab.HOME) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onNavigateToTab(BottomTab.HOME) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_active),
                        contentDescription = "Home",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(onClick = { onNavigateToTab(BottomTab.HOME) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_active),
                        contentDescription = "Home",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Tab 3: Citas / Calendario
            if (currentTab == BottomTab.CITAS) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onNavigateToTab(BottomTab.CITAS) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_nav),
                        contentDescription = "Calendario",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(onClick = { onNavigateToTab(BottomTab.CITAS) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_nav),
                        contentDescription = "Calendario",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
