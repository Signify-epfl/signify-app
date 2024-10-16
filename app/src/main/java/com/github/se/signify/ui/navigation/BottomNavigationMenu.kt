package com.github.se.signify.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationMenu(
    onTabSelect: (TopLevelDestination) -> Unit,
    tabList: List<TopLevelDestination>,
    selectedItem: String = Route.MAIN_AIM, // Provide a default value
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  Box(
      modifier =
          modifier
              .border(2.dp, Color(0xFF05A9FB)) // Top blue line
              .padding(bottom = 2.dp)
              .testTag("BottomNavigationBar")) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            backgroundColor = Color.White // Set background to white
            ) {
              tabList.forEach { tab ->
                BottomNavigationItem(
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
