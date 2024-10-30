package com.github.se.signify.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.navigation.NavigationActions


@Composable
fun QuestScreen(
    navigationActions: NavigationActions,
    questViewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)
) {
  val quests = questViewModel.quest.collectAsState()
  Scaffold(
      modifier = Modifier.fillMaxSize(),
  ) { padding ->
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
      BackButton { navigationActions.goBack()}


      if (quests.value.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(padding)) {
              items(quests.value.size) { n -> QuestBox(quest = quests.value[n]) }
            }
      } else {
        Text(
            modifier = Modifier.padding(padding), text = "You have done your daily quest for today")
      }
    }
  }
}

@Composable
fun QuestBox(quest: Quest) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      elevation = CardDefaults.elevatedCardElevation(4.dp),
      colors = CardDefaults.cardColors(colorResource(R.color.blue))) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
          Text(
              text = "Your Daily Quest",
              color = colorResource(R.color.white),
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              modifier = Modifier
                  .padding(bottom = 8.dp)
                  .fillMaxWidth(),
              textAlign = TextAlign.Center)

          Spacer(modifier = Modifier.height(20.dp))

          Text(
              text = quest.title,
              color = colorResource(R.color.white),
              fontWeight = FontWeight.Bold,
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(text = quest.description, color = colorResource(R.color.white))

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {/*Takes you to your daily quest*/},
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.white),
                    contentColor = colorResource(R.color.blue)), // Button color
                shape = RoundedCornerShape(50),
            ) {
                Text( text = "I am ready")
            }
        }
      }
}
