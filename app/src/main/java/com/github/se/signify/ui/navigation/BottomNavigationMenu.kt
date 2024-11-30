package com.github.se.signify.ui.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationMenu(
    onTabSelect: (TopLevelDestination) -> Unit,
    tabList: List<TopLevelDestination>,
    selectedItem: String = Screen.HOME.route, // Provide a default value
) {
  Box(
      modifier =
          Modifier.border(2.dp, MaterialTheme.colorScheme.outlineVariant) // Top blue line
              .padding(bottom = 2.dp)
              .testTag("BottomNavigationMenu")) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            containerColor = MaterialTheme.colorScheme.background // Set background to white
            ) {
              tabList.forEach { tab ->
                NavigationBarItem(
                    icon = {
                      Icon(
                          painterResource(id = tab.icon),
                          contentDescription = null,
                          modifier = Modifier.testTag("TabIcon_${tab.route}"))
                    }, // Load the drawable icons
                    selected = tab.route == selectedItem,
                    onClick = { onTabSelect(tab) },
                    modifier = Modifier.clip(RoundedCornerShape(50.dp)))
              }
            }
      }
}
