package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedPost
import com.example.data.model.Quest
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MintGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WarmYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestFeedScreen(
    feedPosts: List<FeedPost>,
    onToggleLike: (String) -> Unit,
    onAddComment: (postId: String, text: String) -> Unit,
    onTryQuest: (Quest) -> Unit,
    onOpenParties: () -> Unit
) {
    var activeCommentPost by remember { mutableStateOf<FeedPost?>(null) }
    var commentInput by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    // Comments Sheet
    if (activeCommentPost != null) {
        val post = activeCommentPost!!
        ModalBottomSheet(
            onDismissRequest = { activeCommentPost = null },
            sheetState = sheetState,
            containerColor = MidnightCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Comments (${post.comments.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOffWhite
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    post.comments.forEach { c ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = c.authorAvatar, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = c.author,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextOffWhite
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = c.timestampText,
                                        fontSize = 11.sp,
                                        color = TextSubtle
                                    )
                                }
                                Text(
                                    text = c.text,
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        placeholder = { Text("Add a comment...", color = TextSubtle) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("feed_comment_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MidnightElevated,
                            unfocusedContainerColor = MidnightElevated,
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (commentInput.isNotBlank()) {
                                onAddComment(post.id, commentInput)
                                commentInput = ""
                                activeCommentPost = null
                            }
                        })
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (commentInput.isNotBlank()) {
                                onAddComment(post.id, commentInput)
                                commentInput = ""
                                activeCommentPost = null
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = ElectricOrange
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(bottom = 80.dp)
    ) {
        // TOP HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = "COMMUNITY QUEST LOG",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = ElectricOrange,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Live From the Streets",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = TextOffWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SOCIAL QUICK LAUNCH BAR: PARTIES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SocialHeaderButton(
                    title = "Quest Parties",
                    icon = Icons.Default.Group,
                    color = ElectricOrange,
                    onClick = onOpenParties
                )
            }
        }

        // FEED LIST
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(feedPosts, key = { it.id }) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.dp, BorderStroke, RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Author header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MidnightElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = post.authorAvatar, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = post.authorName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextOffWhite
                                    )
                                    Text(
                                        text = "${post.location} • ${post.timeAgo}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // AI Drama Score Chip
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2E1710))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "🎭 Drama ${post.dramaScore}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricOrange
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quest Title & XP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = post.questTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextOffWhite
                            )
                            Text(
                                text = "+${post.xpEarned} XP",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Badges: Location & Spending
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MidnightElevated)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "📍 ${post.location}",
                                    fontSize = 11.sp,
                                    color = TextOffWhite
                                )
                            }
                            if (post.totalSpent != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MidnightElevated)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (post.totalSpent <= 0.05) "💸 $0 Spent" else "💵 $${post.totalSpent.toInt()} Spent",
                                        fontSize = 11.sp,
                                        color = WarmYellow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = post.caption,
                            fontSize = 14.sp,
                            color = TextMuted,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Proof Photo Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MidnightElevated)
                                .border(1.dp, BorderStroke, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = post.photoPlaceholderEmoji, fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Verified AI Photo Proof • Score: ${post.totalScore}/100",
                                    fontSize = 11.sp,
                                    color = WarmYellow,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Bar: Likes, Comments, TRY QUEST
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Like
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { onToggleLike(post.id) }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (post.isLiked) CoralRed else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${post.likesCount}",
                                        fontSize = 13.sp,
                                        color = if (post.isLiked) CoralRed else TextMuted
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Comment
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { activeCommentPost = post }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubbleOutline,
                                        contentDescription = "Comments",
                                        tint = TextMuted,
                                        modifier = Modifier.size(19.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${post.commentsCount}",
                                        fontSize = 13.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // "TRY QUEST" CTA
                            Button(
                                onClick = { onTryQuest(post.originalQuest) },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("feed_try_quest_${post.id}")
                            ) {
                                Text(
                                    text = "TRY QUEST",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialHeaderButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MidnightSurface)
            .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextOffWhite
            )
        }
    }
}
