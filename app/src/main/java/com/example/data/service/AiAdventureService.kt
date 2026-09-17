package com.example.data.service

import com.example.data.model.AdventureRequest
import com.example.data.model.Mission
import com.example.data.model.PhotoScore
import com.example.data.model.PlotTwist

interface AiAdventureService {
    suspend fun generateAdventure(request: AdventureRequest): Mission
    suspend fun generateFunnyReaction(context: String): String
    suspend fun ratePhotoChallenge(photoUri: String?, questTitle: String): PhotoScore
    suspend fun generatePlotTwist(currentMission: Mission): PlotTwist?
}
