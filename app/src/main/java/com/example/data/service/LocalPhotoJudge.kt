package com.example.data.service

import com.example.data.model.PhotoScore
import kotlin.random.Random

object LocalPhotoJudge {
    private val FUNNY_COMMENTS = listOf(
        "We have concerns, but we respect the commitment.",
        "The drama is astronomical. 10/10 for raw cinematic courage.",
        "Lighting could be better, but the sheer chaos makes up for it.",
        "This looks like an album cover for an indie Lebanese post-punk band.",
        "Your intensity made the camera autofocus question its life choices.",
        "Aesthetic perfection. The street cat in the background approves.",
        "If Michelin judged street photography, this would earn three tires.",
        "Suspiciously professional. Are you secretly a photographer?"
    )

    fun scorePhoto(photoUri: String?, questTitle: String): PhotoScore {
        val seed = (photoUri?.hashCode() ?: Random.nextInt()) + questTitle.hashCode()
        val rng = Random(seed)

        val drama = rng.nextInt(75, 100)
        val composition = rng.nextInt(60, 96)
        val intensity = rng.nextInt(80, 100)
        val total = (drama * 0.4 + composition * 0.3 + intensity * 0.3).toInt().coerceIn(70, 99)

        val comment = FUNNY_COMMENTS[kotlin.math.abs(seed) % FUNNY_COMMENTS.size]

        return PhotoScore(
            drama = drama,
            composition = composition,
            unnecessaryIntensity = intensity,
            totalScore = total,
            aiComment = comment
        )
    }
}
