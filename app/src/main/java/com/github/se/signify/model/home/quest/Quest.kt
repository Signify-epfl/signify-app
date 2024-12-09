package com.github.se.signify.model.home.quest

data class Quest(
    val index: String, // This will contain the index of the quest
    val title: String, // The title of the quest
    val description: String, // The description of the quest
    val videoPath: String // Local path to the video
)
