package com.github.se.bootcamp.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationMenu(
    onTabSelect: (TopLevelDestination) -> Unit,
    selectedItem: String
) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        backgroundColor = Color(0xFF40E0D0),  // Turquoise blue color
    ) {
        LIST_TOP_LEVEL_DESTINATION.forEach { destination ->
            BottomNavigationItem(
                icon = { Icon(destination.icon, contentDescription = null) },
                selected = destination.route == selectedItem,
                onClick = { onTabSelect(destination) }
            )
        }
    }
}