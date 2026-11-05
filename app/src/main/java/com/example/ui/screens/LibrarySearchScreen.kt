package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.data.parser.PinyinMatcher
import com.example.ui.components.EmptyInboxIllustration
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun LibrarySearchScreen(
    viewModel: StudyViewModel,
    banks: List<QuestionBankEntity>,
    questions: List<QuestionEntity>,
    searchQuery: String,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    val focusRequester = remember { FocusRequester() }

    val matchedBanks = remember(banks, searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else banks.filter {
            PinyinMatcher.matches(it.name, searchQuery) ||
                PinyinMatcher.matches(it.subtitle, searchQuery)
        }
    }

    val matchedQuestions = remember(questions, searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else questions.filter {
            PinyinMatcher.matches(it.stem, searchQuery) ||
                PinyinMatcher.matches(it.section, searchQuery)
        }.take(30)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "返回",
                    tint = colors.textPrimary
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setLibrarySearch(it) },
                placeholder = {
                    Text(
                        text = "搜题库、章节或拼音首字母...",
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = colors.primaryAccent
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setLibrarySearch("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "清空",
                                tint = colors.textMuted
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colors.cardSurface,
                    unfocusedContainerColor = colors.cardSurface,
                    focusedBorderColor = colors.primaryAccent,
                    unfocusedBorderColor = colors.borderSubtle
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (searchQuery.isBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EmptyInboxIllustration(size = 110.dp)
                        Text(
                            text = "输入关键字或首字母检索",
                            style = MaterialTheme.typography.titleSmall,
                            color = colors.textMuted
                        )
                    }
                }
            } else if (matchedBanks.isEmpty() && matchedQuestions.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "未找到匹配项",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )
                    }
                }
            } else {
                if (matchedBanks.isNotEmpty()) {
                    item {
                        Text(
                            text = "匹配的题库 (${matchedBanks.size})",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.primaryAccent
                        )
                    }
                    items(matchedBanks) { bank ->
                        SereneCard(onClick = { viewModel.openBankDetail(bank.name) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = bank.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = colors.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = bank.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.textSecondary
                                    )
                                }
                                SereneBadge(text = "${bank.totalTargetCount}题")
                            }
                        }
                    }
                }

                if (matchedQuestions.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "匹配的题目 (${matchedQuestions.size})",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.primaryAccent
                        )
                    }
                    items(matchedQuestions) { q ->
                        SereneCard(onClick = { viewModel.openQuestionEditor(q) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = q.bank,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textMuted
                                )
                                SereneBadge(text = q.typeLabel)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = q.stem,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = colors.textPrimary,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}
