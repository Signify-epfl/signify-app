package com.github.se.signify.model.home.exercise

import com.github.se.signify.model.profile.stats.StatsViewModel

/**
 * Data class that regroup information to track time during exercise
 *
 * @param statsViewModel
 * @param timestamp
 */
data class ExerciseTrackTime(
    val statsViewModel: StatsViewModel,
    var timestamp: Long = System.currentTimeMillis()
) {
  fun updateTrackingAndCallUpdateTimePerLetter() {
    val newTime = System.currentTimeMillis() - timestamp
    statsViewModel.updateTimePerLetter(newTime)
    timestamp = System.currentTimeMillis()
  }
}
