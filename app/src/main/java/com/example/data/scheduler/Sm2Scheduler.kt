package com.example.data.scheduler

import kotlin.math.max
import kotlin.math.roundToInt

enum class ReviewQuality(val grade: Int, val label: String) {
    AGAIN(1, "重来"),   // 完全答错或遗忘
    HARD(3, "困难"),    // 犹豫、经过长时间回忆
    GOOD(4, "良好"),    // 正常答对
    EASY(5, "简单")     // 熟稔于心、立即秒杀
}

data class Sm2ScheduleResult(
    val repetitions: Int,
    val intervalDays: Int,
    val easeFactor: Double,
    val nextReviewTimestamp: Long
)

/**
 * SuperMemo 2 (SM-2) Spaced Repetition Scheduling Algorithm.
 * Powers Anki and modern evidence-based cognitive memory workbenches.
 */
object Sm2Scheduler {

    private const val ONE_DAY_MS = 86_400_000L
    private const val TEN_MIN_MS = 600_000L

    fun calculate(
        repetitions: Int,
        intervalDays: Int,
        easeFactor: Double,
        quality: ReviewQuality,
        now: Long = System.currentTimeMillis()
    ): Sm2ScheduleResult {
        val q = quality.grade
        var newReps = repetitions
        var newInterval: Int
        var newEase = easeFactor + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        if (newEase < 1.3) newEase = 1.3

        val nextTimestamp: Long

        when (quality) {
            ReviewQuality.AGAIN -> {
                newReps = 0
                newInterval = 1
                nextTimestamp = now + TEN_MIN_MS // 10 minutes later for instant relapse reinforcement
            }
            ReviewQuality.HARD -> {
                newReps = max(1, newReps)
                newInterval = max(1, (intervalDays * 1.2).roundToInt())
                nextTimestamp = now + newInterval * ONE_DAY_MS
            }
            ReviewQuality.GOOD -> {
                newInterval = when (newReps) {
                    0 -> 1
                    1 -> 6
                    else -> (intervalDays * newEase).roundToInt()
                }
                newReps += 1
                nextTimestamp = now + newInterval * ONE_DAY_MS
            }
            ReviewQuality.EASY -> {
                newInterval = when (newReps) {
                    0 -> 4
                    1 -> 10
                    else -> (intervalDays * newEase * 1.3).roundToInt()
                }
                newReps += 1
                newEase += 0.15
                nextTimestamp = now + newInterval * ONE_DAY_MS
            }
        }

        return Sm2ScheduleResult(
            repetitions = newReps,
            intervalDays = newInterval,
            easeFactor = (newEase * 100).roundToInt() / 100.0,
            nextReviewTimestamp = nextTimestamp
        )
    }
}
