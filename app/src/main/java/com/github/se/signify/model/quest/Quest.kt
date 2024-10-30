package com.github.se.signify.model.quest

data class Quest(
    val index: String, // This will contain the index for the quest - from 1 to 26
    val title: String, // The title of the quest
    val description: String, // The description of the quest
)
