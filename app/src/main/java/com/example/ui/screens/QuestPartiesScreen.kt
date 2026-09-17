package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.QuestParty
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MintGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow

@Composable
fun QuestPartiesScreen(
    parties: List<QuestParty>,
    onBack: () -> Unit,
    onJoinParty: (String) -> Unit,
    onLeaveParty: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onCreateParty: ((name: String, city: String, time: String, maxMembers: Int, desc: String, transport: String) -> Unit)? = null
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var partyName by remember { mutableStateOf("") }
    var partyCity by remember { mutableStateOf("Mar Mikhael, Beirut") }
    var maxMembers by remember { mutableIntStateOf(4) }
    var startTimeText by remember { mutableStateOf("Tonight @ 9:00 PM") }
    var partyObjective by remember { mutableStateOf("Hunt best street food & late night vinyl") }
    var selectedTransport by remember { mutableStateOf("Walk") }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👥", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CREATE QUEST PARTY",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextOffWhite
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = partyName,
                        onValueChange = { partyName = it },
                        label = { Text("Party Name") },
                        placeholder = { Text("e.g. Achrafieh Alley Explorers") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )

                    OutlinedTextField(
                        value = partyCity,
                        onValueChange = { partyCity = it },
                        label = { Text("City / Area") },
                        placeholder = { Text("e.g. Gemmayze, Byblos, Batroun") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )

                    OutlinedTextField(
                        value = startTimeText,
                        onValueChange = { startTimeText = it },
                        label = { Text("Meetup Time / Target Start") },
                        placeholder = { Text("e.g. Tonight @ 9:30 PM") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )

                    // Max Members Slider / Selector (2-8)
                    Column {
                        Text(
                            text = "Max Members: $maxMembers",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            (2..8).forEach { count ->
                                val isSelected = maxMembers == count
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ElectricOrange else MidnightElevated)
                                        .clickable { maxMembers = count }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$count",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextOffWhite
                                    )
                                }
                            }
                        }
                    }

                    // Transport Mode Selector
                    Column {
                        Text(
                            text = "Transport Mode",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Walk" to "🚶 Walk", "Car" to "🚗 Car").forEach { (modeKey, modeLabel) ->
                                val isSelected = selectedTransport == modeKey
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ElectricOrange else MidnightElevated)
                                        .border(1.dp, if (isSelected) ElectricOrange else BorderStroke, RoundedCornerShape(10.dp))
                                        .clickable { selectedTransport = modeKey }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = modeLabel,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextOffWhite
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = partyObjective,
                        onValueChange = { partyObjective = it },
                        label = { Text("Quest Objective / Description") },
                        placeholder = { Text("What are you exploring?") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = if (partyName.isBlank()) "Spontaneous $partyCity Squad" else partyName
                        showCreateDialog = false
                        onCreateParty?.invoke(name, partyCity, startTimeText, maxMembers, partyObjective, selectedTransport)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("CREATE & HOST", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("CANCEL", color = TextMuted)
                }
            },
            containerColor = MidnightCard
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("parties_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextOffWhite
                )
            }

            Text(
                text = "QUEST PARTIES",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = ElectricOrange,
                letterSpacing = 1.sp
            )

            Box(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Join a Live Squad",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextOffWhite
                )
                Text(
                    text = "Drop into an active neighborhood mission with other wanderers.",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            if (onCreateParty != null) {
                Button(
                    onClick = { showCreateDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                    modifier = Modifier.testTag("create_party_button")
                ) {
                    Text("+ CREATE", fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(parties, key = { it.id }) { party ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .border(
                            width = if (party.isJoined) 2.dp else 1.dp,
                            color = if (party.isJoined) ElectricOrange else BorderStroke,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .clickable { onOpenChat(party.id) }
                        .testTag("party_card_${party.id}"),
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
                            Text(
                                text = party.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextOffWhite
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (party.isJoined) MintGreen.copy(alpha = 0.2f) else MidnightElevated)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (party.isJoined) "JOINED" else "${party.memberCount}/${party.maxMembers} SLOTS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (party.isJoined) MintGreen else WarmYellow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📍 ${party.location} • ⏱️ ${party.startsInText}",
                            fontSize = 13.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Members list tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            party.members.forEach { m ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MidnightElevated)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = m, fontSize = 11.sp, color = TextOffWhite)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (party.isJoined) {
                                Button(
                                    onClick = { onOpenChat(party.id) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Chat,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("OPEN CHAT (${party.messages.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { onLeaveParty(party.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke)
                                ) {
                                    Text("LEAVE", fontSize = 12.sp, color = TextMuted)
                                }
                            } else {
                                Button(
                                    onClick = { onJoinParty(party.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("JOIN PARTY", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
