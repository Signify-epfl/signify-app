package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.StatisticsRow
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeHistoryScreen(
    navigationActions: NavigationActions,
    friendsChallengesAchieved: Int,
    challengesCreated: Int
) {
    AnnexScreenScaffold(
        navigationActions = navigationActions,
        testTagColumn = "ChallengeHistoryScreen",
    ) {
        // Number of friends' challenges achieved
        StatisticsRow(
            rowTestTag = "FriendsChallengesRow",
            lineText = "Number of friends challenges achieved",
            lineTextTag = "FriendsChallengesText",
            columnTextList = listOf(listOf("$friendsChallengesAchieved")),
            columnTextSPList = listOf(listOf(20)),
            columnTextTagList = listOf("FriendsChallengesCountBox")
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Number of challenges created
        StatisticsRow(
            rowTestTag = "ChallengesCreatedRow",
            lineText = "Number of challenges created",
            lineTextTag = "ChallengesCreatedText",
            columnTextList = listOf(listOf("$challengesCreated")),
            columnTextSPList = listOf(listOf(20)),
            columnTextTagList = listOf("ChallengesCreatedCountBox")
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Graphs and Stats
        NotImplementedYet(testTag = "GraphsAndStats", text = "Graphs and Stats")
    }
}
