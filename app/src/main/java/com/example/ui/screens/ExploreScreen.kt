package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CityData
import com.example.data.model.AdventureRequest
import com.example.data.model.TransportMode
import com.example.data.repository.CuratedAdventure
import com.example.data.repository.ExploreRepository
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow

@Composable
fun ExploreScreen(
    exploreRepository: ExploreRepository,
    selectedCity: String,
    onCityChanged: (String) -> Unit,
    onLaunchAdventure: (AdventureRequest) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var selectedBudget by remember { mutableStateOf("All") }
    var selectedTime by remember { mutableStateOf("All") }
    var selectedTransport by remember { mutableStateOf<TransportMode?>(null) }

    val categories = exploreRepository.categories
    val adventures = remember(selectedCity, selectedCategoryId, selectedBudget, selectedTime, selectedTransport) {
        exploreRepository.getFilteredAdventures(
            city = selectedCity,
            categoryId = selectedCategoryId,
            budgetFilter = selectedBudget,
            durationFilter = selectedTime,
            transportFilter = selectedTransport
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // TOP BAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "EXPLORE LEBANON",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = ElectricOrange,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Curated Field Missions",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = TextOffWhite
            )
            Text(
                text = "Hand-crafted itineraries across coastlines, souks, and alleyways.",
                fontSize = 13.sp,
                color = TextMuted
            )
        }

        // CITY FILTER CHIPS (Beirut, Jounieh, Byblos, Batroun, Zahle, Tripoli, Sidon, Near Me)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CityData.CITIES.forEach { city ->
                val isSelected = city.equals(selectedCity, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ElectricOrange else MidnightCard)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ElectricOrange else BorderStroke,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onCityChanged(city) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("explore_city_$city")
                ) {
                    Text(
                        text = "📍 $city",
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else TextOffWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CATEGORY PILLS ($0 City, Food Runs, Date Ideas, Chaos Runs, etc.)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // All pill
            val isAll = selectedCategoryId == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isAll) WarmYellow else MidnightSurface)
                    .clickable { selectedCategoryId = null }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "✨ All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAll) MidnightBackground else TextOffWhite
                )
            }

            categories.forEach { cat ->
                val isSelected = selectedCategoryId == cat.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) WarmYellow else MidnightSurface)
                        .clickable { selectedCategoryId = if (isSelected) null else cat.id }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "${cat.emoji} ${cat.title}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MidnightBackground else TextOffWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SECONDARY REFINED FILTERS: Budget, Transport, Duration (Requirement #19)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Budget filters
            listOf("All", "$0", "<$10", "$10+").forEach { b ->
                val isSelected = selectedBudget == b
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ElectricOrange.copy(alpha = 0.25f) else MidnightSurface)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ElectricOrange else BorderStroke.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedBudget = b }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (b == "All") "💵 Any $" else "💵 $b",
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) ElectricOrange else TextOffWhite
                    )
                }
            }

            // Transport filters
            val isWalk = selectedTransport == TransportMode.WALK
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isWalk) ElectricOrange.copy(alpha = 0.25f) else MidnightSurface)
                    .border(
                        width = 1.dp,
                        color = if (isWalk) ElectricOrange else BorderStroke.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedTransport = if (isWalk) null else TransportMode.WALK }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "🚶 Walking",
                    fontSize = 11.sp,
                    fontWeight = if (isWalk) FontWeight.Bold else FontWeight.Normal,
                    color = if (isWalk) ElectricOrange else TextOffWhite
                )
            }

            val isCar = selectedTransport == TransportMode.CAR
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCar) ElectricOrange.copy(alpha = 0.25f) else MidnightSurface)
                    .border(
                        width = 1.dp,
                        color = if (isCar) ElectricOrange else BorderStroke.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedTransport = if (isCar) null else TransportMode.CAR }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "🚗 Car",
                    fontSize = 11.sp,
                    fontWeight = if (isCar) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCar) ElectricOrange else TextOffWhite
                )
            }

            // Duration filter
            listOf("All", "30 min", "1 hr+").forEach { d ->
                val isSelected = selectedTime == d
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ElectricOrange.copy(alpha = 0.25f) else MidnightSurface)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ElectricOrange else BorderStroke.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedTime = d }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (d == "All") "⏱️ Any Time" else "⏱️ $d",
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) ElectricOrange else TextOffWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ADVENTURE CARDS LIST
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Adventures in $selectedCity (${adventures.size})",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextOffWhite
            )

            adventures.forEach { adventure ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, BorderStroke, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MidnightElevated)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "📍 ${adventure.neighborhood}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ElectricOrange
                                )
                            }

                            Text(
                                text = "+${adventure.xpReward} XP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = WarmYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = adventure.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = adventure.subtitle,
                            fontSize = 13.sp,
                            color = TextMuted,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "💰 ${adventure.budgetText}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextOffWhite
                                )
                                Text(
                                    text = "⏱️ ${adventure.durationText}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextOffWhite
                                )
                            }

                            Button(
                                onClick = {
                                    val req = AdventureRequest(
                                        location = "${adventure.neighborhood}, ${adventure.city}",
                                        budget = adventure.budgetText,
                                        duration = adventure.durationText,
                                        mode = adventure.mode,
                                        secondaryMood = adventure.secondaryMood
                                    )
                                    onLaunchAdventure(req)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("START", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
