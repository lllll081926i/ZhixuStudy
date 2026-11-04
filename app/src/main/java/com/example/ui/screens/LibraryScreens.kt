package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DirectionsRailway
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Traffic
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.listToJson
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.components.SereneFilterChipRow
import com.example.ui.components.SereneLinearProgress
import com.example.ui.components.SereneTopBar
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun LibraryScreen(
    viewModel: StudyViewModel,
    banks: List<QuestionBankEntity>,
    questions: List<QuestionEntity>,
    searchQuery: String,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    var showCreateInline by remember { mutableStateOf(false) }
    var newBankName by remember { mutableStateOf("") }
    var newBankSubtitle by remember { mutableStateOf("") }

    val filteredBanks = remember(banks, searchQuery) {
        if (searchQuery.isBlank()) banks
        else banks.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                it.subtitle.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SereneTopBar(
                title = "题库",
                isTablet = isTablet,
                tabletSplitActive = tabletSplitActive,
                onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(colors.primarySoft)
                            .clickable { showCreateInline = !showCreateInline }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("library_audit_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "新建题库",
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (showCreateInline) "收起" else "新建题库",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.AI_SERVICE) },
                        modifier = Modifier.testTag("library_ai_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = "AI 助手",
                            tint = colors.primaryAccent
                        )
                    }
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                        modifier = Modifier.testTag("library_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "设置",
                            tint = colors.textPrimary
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = if (isTablet) 24.dp else 16.dp,
                    end = if (isTablet) 24.dp else 16.dp,
                    top = 6.dp,
                    bottom = 112.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setLibrarySearch(it) },
                        placeholder = {
                            Text(
                                text = "搜索题库...",
                                color = colors.textMuted,
                                maxLines = 1,
                                softWrap = false
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = colors.textSecondary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("library_search_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.cardSurface,
                            unfocusedContainerColor = colors.cardSurface,
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.borderSubtle
                        )
                    )
                }

                if (showCreateInline) {
                    item {
                        SereneCard(
                            cornerRadius = 22.dp,
                            borderColor = colors.primaryAccent.copy(alpha = 0.4f),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(
                                text = "新建题库",
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = newBankName,
                                onValueChange = { newBankName = it },
                                label = { Text("题库名称", maxLines = 1) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = newBankSubtitle,
                                onValueChange = { newBankSubtitle = it },
                                label = { Text("简介描述（选填）", maxLines = 1) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { showCreateInline = false }) {
                                    Text("取消", maxLines = 1, softWrap = false)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (newBankName.isNotBlank()) {
                                            viewModel.createNewBank(newBankName, newBankSubtitle)
                                            newBankName = ""
                                            newBankSubtitle = ""
                                            showCreateInline = false
                                        }
                                    },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                                ) {
                                    Text("创建", maxLines = 1, softWrap = false, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "共 ${filteredBanks.size} 本题库",
                            color = colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { viewModel.navigateTo(AppScreen.DATA_MGMT) }
                                .testTag("library_backup_link")
                        ) {
                            Text(
                                text = "备份与导出",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                if (isTablet && tabletSplitActive) {
                    val chunked = filteredBanks.chunked(2)
                    items(chunked) { rowBanks ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowBanks.forEach { bank ->
                                val actualCount = questions.count { it.bank == bank.name }.coerceAtLeast(bank.totalTargetCount)
                                LibraryBankCardItem(
                                    bank = bank,
                                    actualTotalCount = actualCount,
                                    onClick = { viewModel.openBankDetail(bank.name) },
                                    onQuickPractice = { viewModel.startQuickFiveSprint(bank.name) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowBanks.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    items(filteredBanks, key = { it.name }) { bank ->
                        val actualCount = questions.count { it.bank == bank.name }.coerceAtLeast(bank.totalTargetCount)
                        LibraryBankCardItem(
                            bank = bank,
                            actualTotalCount = actualCount,
                            onClick = { viewModel.openBankDetail(bank.name) },
                            onQuickPractice = { viewModel.startQuickFiveSprint(bank.name) }
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = { viewModel.navigateTo(AppScreen.IMPORT_SOURCE) },
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            text = {
                Text(
                    text = "导入外部题库",
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            containerColor = colors.primaryBright,
            contentColor = Color.White,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 92.dp)
                .testTag("library_import_fab")
        )
    }
}

@Composable
private fun LibraryBankCardItem(
    bank: QuestionBankEntity,
    actualTotalCount: Int,
    onClick: () -> Unit,
    onQuickPractice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SereneTheme.colors
    val (icon, iconBg, iconTint) = when (bank.domainTag) {
        "highway" -> Triple(Icons.Outlined.Traffic, colors.mintBg, colors.mintText)
        "exam" -> Triple(Icons.Outlined.Assignment, colors.peachBg, colors.peachText)
        "cs_math" -> Triple(Icons.Outlined.Functions, colors.amberBg, colors.amberText)
        else -> Triple(Icons.Outlined.DirectionsRailway, colors.primarySoft, colors.primaryAccent)
    }
    val progressRatio = (bank.practicedCount.toFloat() / actualTotalCount.coerceAtLeast(1)).coerceIn(0f, 1f)
    val percentInt = (progressRatio * 100).toInt()

    SereneCard(
        modifier = modifier.testTag("bank_card_${bank.name}"),
        cornerRadius = 22.dp,
        contentPadding = PaddingValues(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bank.name,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = bank.subtitle,
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(iconBg)
                    .clickable { onQuickPractice() }
                    .padding(horizontal = 11.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "练 5 题",
                        color = iconTint,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "共 $actualTotalCount 题 · 已刷 ${bank.practicedCount} 题 · 正确率 ${bank.accuracyPercent}%",
                color = colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = "$percentInt%",
                color = iconTint,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        SereneLinearProgress(
            progress = progressRatio,
            indicatorColor = iconTint,
            height = 7.dp
        )
    }
}

@Composable
fun BookDetailScreen(
    viewModel: StudyViewModel,
    bankName: String,
    banks: List<QuestionBankEntity>,
    questions: List<QuestionEntity>,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    val bank = banks.find { it.name == bankName } ?: QuestionBankEntity(
        name = bankName,
        subtitle = "专业重点科目题库",
        domainTag = "railway",
        totalTargetCount = 186,
        practicedCount = 64,
        accuracyPercent = 78
    )

    var showManageInline by remember { mutableStateOf(false) }
    var renameText by remember(bankName) { mutableStateOf(bankName.removeSurrounding("《", "》")) }

    val bankQuestions = remember(questions, bankName) {
        questions.filter { it.bank == bankName }
    }
    val chapters = remember(bankQuestions) {
        val grouped = bankQuestions.groupBy { it.chapter }
        if (grouped.isNotEmpty()) {
            grouped.entries.map { (chName, list) ->
                Triple(chName, list.size.coerceAtLeast(18), (list.size / 2).coerceAtLeast(6))
            }
        } else {
            listOf(
                Triple("第一章：车站工作组织", 64, 32),
                Triple("第二章：列车运行图与区间通过能力", 72, 20),
                Triple("第三章：车流组织与编组计划", 50, 12)
            )
        }
    }

    val totalDisplay = bank.totalTargetCount.coerceAtLeast(bankQuestions.size)
    val progressRatio = (bank.practicedCount.toFloat() / totalDisplay.coerceAtLeast(1)).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "题库详情",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet,
            tabletSplitActive = tabletSplitActive,
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
            actions = {
                IconButton(onClick = { showManageInline = !showManageInline }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "管理",
                        tint = colors.textPrimary
                    )
                }
            }
        )

        AnimatedVisibility(visible = showManageInline) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 6.dp)
            ) {
                SereneCard(
                    cornerRadius = 20.dp,
                    borderColor = colors.primaryAccent.copy(alpha = 0.4f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "修改题库名称或删除",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = renameText,
                        onValueChange = { renameText = it },
                        label = { Text("题库名称", maxLines = 1) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                showManageInline = false
                                viewModel.deleteBank(bank.name)
                            }
                        ) {
                            Text("删掉这本题库", color = colors.dangerText, maxLines = 1, softWrap = false)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { showManageInline = false }) {
                                Text("收起", maxLines = 1, softWrap = false)
                            }
                            Button(
                                onClick = {
                                    viewModel.renameBank(bank.name, renameText)
                                    showManageInline = false
                                },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                            ) {
                                Text("保存名字", maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }
            }
        }

        if (isTablet && tabletSplitActive) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(0.95f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        BookOverviewHeroCard(
                            bank = bank,
                            totalDisplay = totalDisplay,
                            progressRatio = progressRatio
                        )
                    }
                    item {
                        BookPrimaryActionRow(
                            onStartPractice = { viewModel.openPracticeConfig(bank.name) },
                            onOpenQuestionList = { viewModel.openQuestionList(bank.name) }
                        )
                    }
                    item {
                        BookToolsSection(
                            onImportUpdate = {
                                viewModel.updateImportTargetBank(bank.name)
                                viewModel.navigateTo(AppScreen.IMPORT_SOURCE)
                            },
                            onAudit = { viewModel.navigateTo(AppScreen.AUDIT) },
                            onManage = { showManageInline = !showManageInline }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1.05f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "按章节看",
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                text = "共 ${chapters.size} 章",
                                color = colors.textSecondary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    items(chapters) { (chapterName, count, practiced) ->
                        ChapterCardItem(
                            chapterName = chapterName,
                            totalCount = count,
                            practicedCount = practiced,
                            onClick = { viewModel.openQuestionList(bank.name, chapter = chapterName) },
                            onQuickPractice = {
                                viewModel.openPracticeConfig(bank.name, presetMode = "顺序练习")
                            }
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    BookOverviewHeroCard(
                        bank = bank,
                        totalDisplay = totalDisplay,
                        progressRatio = progressRatio
                    )
                }

                item {
                    BookPrimaryActionRow(
                        onStartPractice = { viewModel.openPracticeConfig(bank.name) },
                        onOpenQuestionList = { viewModel.openQuestionList(bank.name) }
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "按章节看",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "共 ${chapters.size} 章",
                            color = colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                items(chapters) { (chapterName, count, practiced) ->
                    ChapterCardItem(
                        chapterName = chapterName,
                        totalCount = count,
                        practicedCount = practiced,
                        onClick = { viewModel.openQuestionList(bank.name, chapter = chapterName) },
                        onQuickPractice = {
                            viewModel.openPracticeConfig(bank.name, presetMode = "顺序练习")
                        }
                    )
                }

                item {
                    BookToolsSection(
                        onImportUpdate = {
                            viewModel.updateImportTargetBank(bank.name)
                            viewModel.navigateTo(AppScreen.IMPORT_SOURCE)
                        },
                        onAudit = { viewModel.navigateTo(AppScreen.AUDIT) },
                        onManage = { showManageInline = !showManageInline }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookOverviewHeroCard(
    bank: QuestionBankEntity,
    totalDisplay: Int,
    progressRatio: Float
) {
    val colors = SereneTheme.colors
    SereneCard(cornerRadius = 24.dp, contentPadding = PaddingValues(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(colors.primarySoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = colors.primaryAccent,
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bank.name,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bank.subtitle,
                    color = colors.textSecondary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.subtleSurface.copy(alpha = 0.6f))
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(16.dp))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("总题数", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "$totalDisplay 题",
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(colors.borderStrong)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("已经做过", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${bank.practicedCount} 题",
                    color = colors.primaryAccent,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(colors.borderStrong)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("做对比例", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${bank.accuracyPercent}%",
                    color = colors.mintText,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "整本刷完进度",
                color = colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "已刷 ${(progressRatio * 100).toInt()}%",
                color = colors.primaryAccent,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        SereneLinearProgress(progress = progressRatio, height = 8.dp)
    }
}

@Composable
private fun BookPrimaryActionRow(
    onStartPractice: () -> Unit,
    onOpenQuestionList: () -> Unit
) {
    val colors = SereneTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onStartPractice,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("book_detail_start_practice"),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryBright)
        ) {
            Icon(Icons.Outlined.School, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "开始刷题",
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Button(
            onClick = onOpenQuestionList,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("book_detail_question_list"),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primarySoft,
                contentColor = colors.primaryAccent
            )
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.FormatListBulleted,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "翻看全部题目",
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun ChapterCardItem(
    chapterName: String,
    totalCount: Int,
    practicedCount: Int,
    onClick: () -> Unit,
    onQuickPractice: () -> Unit
) {
    val colors = SereneTheme.colors
    SereneCard(
        cornerRadius = 18.dp,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapterName,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$totalCount 题 · ",
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "已做 $practicedCount 题",
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colors.primarySoft)
                    .clickable { onQuickPractice() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "练这章",
                    color = colors.primaryAccent,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun BookToolsSection(
    onImportUpdate: () -> Unit,
    onAudit: () -> Unit,
    onManage: () -> Unit
) {
    val colors = SereneTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "整理这本题库",
            color = colors.textPrimary,
            maxLines = 1,
            softWrap = false,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
        SereneCard(cornerRadius = 22.dp, contentPadding = PaddingValues(vertical = 6.dp, horizontal = 16.dp)) {
            ToolActionRow(
                icon = Icons.Outlined.FileUpload,
                title = "往这本里继续加题",
                onClick = onImportUpdate
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.borderSubtle)
            )
            ToolActionRow(
                icon = Icons.Outlined.Checklist,
                title = "检查重题和漏写的解析",
                badgeText = "● 很干净",
                onClick = onAudit
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.borderSubtle)
            )
            ToolActionRow(
                icon = Icons.Outlined.Share,
                title = "改名、备份或删除",
                onClick = onManage
            )
        }
    }
}

@Composable
private fun ToolActionRow(
    icon: ImageVector,
    title: String,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    val colors = SereneTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.textPrimary,
                modifier = Modifier.size(21.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = colors.textPrimary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (badgeText != null) {
                SereneBadge(
                    text = badgeText,
                    containerColor = colors.mintBg,
                    contentColor = colors.mintText
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.textSecondary
            )
        }
    }
}

@Composable
fun QuestionListScreen(
    viewModel: StudyViewModel,
    bankName: String,
    chapterFilter: String?,
    questions: List<QuestionEntity>,
    searchQuery: String,
    activeFilter: String,
    isBatchMode: Boolean,
    batchSelectedIds: Set<String>,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    var showBatchEditPanel by remember { mutableStateOf(false) }
    var targetChapterText by remember { mutableStateOf("第一章：车站工作组织") }
    val expandedAnswerMap = remember { mutableStateMapOf<String, Boolean>() }

    val bankQuestions = remember(questions, bankName, chapterFilter) {
        questions.filter {
            (it.bank == bankName || questions.none { q -> q.bank == bankName }) &&
                (chapterFilter == null || it.chapter == chapterFilter)
        }
    }

    val wrongCount = bankQuestions.count { it.wrongCount > 0 }
    val starredCount = bankQuestions.count { it.isStarred }
    val hardCount = bankQuestions.count { it.difficulty == "困难" || it.isFlagged }

    val filteredQuestions = remember(bankQuestions, activeFilter, searchQuery) {
        val byChip = when (activeFilter) {
            "wrong" -> bankQuestions.filter { it.wrongCount > 0 }
            "starred" -> bankQuestions.filter { it.isStarred }
            "hard" -> bankQuestions.filter { it.difficulty == "困难" || it.isFlagged }
            else -> bankQuestions
        }
        if (searchQuery.isBlank()) byChip
        else byChip.filter {
            it.stem.contains(searchQuery, ignoreCase = true) ||
                it.explanation.contains(searchQuery, ignoreCase = true) ||
                it.section.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SereneTopBar(
                title = "题目列表",
                subtitle = chapterFilter ?: bankName.removeSurrounding("《", "》"),
                onBack = { viewModel.navigateBack() },
                isTablet = isTablet,
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(colors.primarySoft)
                            .clickable { viewModel.openQuestionEditor(null) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("question_list_add_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "加一道题",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            )

            AnimatedVisibility(visible = showBatchEditPanel) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 6.dp)
                ) {
                    SereneCard(
                        cornerRadius = 20.dp,
                        borderColor = colors.primaryAccent.copy(alpha = 0.45f),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Text(
                            text = "已勾选 ${batchSelectedIds.size} 道题，要怎么处理？",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = targetChapterText,
                            onValueChange = { targetChapterText = it },
                            label = { Text("挪到哪个章节", maxLines = 1) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("简单", "中等", "困难").forEach { diff ->
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.batchUpdateSelectedDifficulty(diff)
                                            showBatchEditPanel = false
                                        },
                                        shape = RoundedCornerShape(50),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("设为$diff", maxLines = 1, softWrap = false, fontSize = 11.sp)
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                TextButton(
                                    onClick = {
                                        viewModel.batchDeleteSelected()
                                        showBatchEditPanel = false
                                    }
                                ) {
                                    Text("删掉", color = colors.dangerText, maxLines = 1, softWrap = false)
                                }
                                Button(
                                    onClick = {
                                        viewModel.batchUpdateSelectedChapter(targetChapterText)
                                        showBatchEditPanel = false
                                    },
                                    shape = RoundedCornerShape(50),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("确认挪过去", maxLines = 1, softWrap = false, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = if (isTablet) 24.dp else 16.dp,
                    end = if (isTablet) 24.dp else 16.dp,
                    top = 8.dp,
                    bottom = 86.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setQuestionListSearch(it) },
                        placeholder = { Text("搜题干、选项或者解析里的词...", maxLines = 1, softWrap = false) },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                        trailingIcon = {
                            if (chapterFilter != null) {
                                TextButton(onClick = { viewModel.setChapterFilter(null) }) {
                                    Text("看全书", fontSize = 11.sp, maxLines = 1, softWrap = false)
                                }
                            } else {
                                Icon(Icons.Outlined.Tune, contentDescription = null, tint = colors.textSecondary)
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("question_list_search"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.cardSurface,
                            unfocusedContainerColor = colors.cardSurface
                        )
                    )
                }

                item {
                    SereneFilterChipRow(
                        options = listOf(
                            "all" to "全部 ${bankQuestions.size}",
                            "wrong" to "做错过的 $wrongCount",
                            "starred" to "★ 收藏的 $starredCount",
                            "hard" to "觉得难的 $hardCount"
                        ),
                        selectedKey = activeFilter,
                        onSelect = { viewModel.setQuestionListFilter(it) }
                    )
                }

                itemsIndexed(filteredQuestions, key = { _, q -> q.id }) { idx, q ->
                    val indexStr = "#${(idx + 1).toString().padStart(2, '0')}"
                    val isSelectedInBatch = q.id in batchSelectedIds
                    val isAnswerExpanded = expandedAnswerMap[q.id] == true

                    SereneCard(
                        cornerRadius = 22.dp,
                        borderColor = if (isSelectedInBatch) colors.primaryAccent else colors.borderSubtle,
                        onClick = {
                            if (isBatchMode) {
                                viewModel.toggleBatchSelection(q.id)
                            } else {
                                viewModel.openQuestionEditor(q)
                            }
                        },
                        modifier = Modifier.testTag("question_item_card_${q.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (isBatchMode) {
                                    Icon(
                                        imageVector = if (isSelectedInBatch) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                                        contentDescription = "勾选",
                                        tint = if (isSelectedInBatch) colors.primaryAccent else colors.textMuted
                                    )
                                }
                                Text(
                                    text = indexStr,
                                    color = colors.primaryAccent,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                )
                                SereneBadge(
                                    text = q.typeLabel,
                                    containerColor = colors.primarySoft,
                                    contentColor = colors.primaryAccent
                                )
                                val (diffBg, diffFg) = when (q.difficulty) {
                                    "简单" -> colors.mintBg to colors.mintText
                                    "困难" -> colors.peachBg to colors.peachText
                                    else -> colors.subtleSurface to colors.textSecondary
                                }
                                SereneBadge(
                                    text = q.difficulty,
                                    containerColor = diffBg,
                                    contentColor = diffFg
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (q.wrongCount > 0) {
                                    SereneBadge(
                                        text = "错过 ${q.wrongCount} 次",
                                        containerColor = colors.dangerBg,
                                        contentColor = colors.dangerText
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.toggleQuestionStar(q.id) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = if (q.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "收藏",
                                        tint = if (q.isStarred) colors.amberText else colors.textMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.openQuestionEditor(q) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.EditNote,
                                        contentDescription = "修改",
                                        tint = colors.textSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = q.stem,
                            color = colors.textPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (q.wrongCount > 0 && q.trapAnalysis.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(colors.dangerBg.copy(alpha = 0.7f))
                                    .border(1.dp, colors.dangerText.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = null,
                                    tint = colors.dangerText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = q.trapAnalysis,
                                    color = colors.dangerText,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                )
                            }
                        } else if (q.options.size >= 2) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(colors.subtleSurface.copy(alpha = 0.65f))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                q.options.take(2).forEach { opt ->
                                    Text(
                                        text = opt,
                                        color = colors.textSecondary,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = isAnswerExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.mintBg.copy(alpha = 0.65f))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "正确答案：${q.answer.joinToString(", ")}",
                                    color = colors.mintText,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = q.explanation.ifBlank { "按核心概念直接判断即可。" },
                                    color = colors.textPrimary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FolderOpen,
                                    contentDescription = null,
                                    tint = colors.textMuted,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = q.section,
                                    color = colors.textSecondary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { expandedAnswerMap[q.id] = !isAnswerExpanded }
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Visibility,
                                        contentDescription = null,
                                        tint = colors.textSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (isAnswerExpanded) "收起答案" else "看答案",
                                        color = colors.textSecondary,
                                        maxLines = 1,
                                        softWrap = false,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                if (q.wrongCount > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { viewModel.startQuickReviewSession(q) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Refresh,
                                            contentDescription = null,
                                            tint = colors.primaryAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "重做一遍",
                                            color = colors.primaryAccent,
                                            maxLines = 1,
                                            softWrap = false,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = null,
                                            tint = colors.mintText,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "掌握 ${q.masteryPercent}%",
                                            color = colors.mintText,
                                            maxLines = 1,
                                            softWrap = false,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Floating Batch Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF232738),
            shadowElevation = 14.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            viewModel.toggleBatchMode()
                            if (!isBatchMode && filteredQuestions.isNotEmpty()) {
                                viewModel.selectAllBatchQuestions(filteredQuestions.take(2).map { it.id })
                            } else {
                                showBatchEditPanel = false
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("batch_mode_toggle_btn")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Checklist,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBatchMode) "已选 ${batchSelectedIds.size} 题" else "批量勾选",
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.14f))
                            .clickable { viewModel.navigateTo(AppScreen.DATA_MGMT) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.IosShare,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "导出题目",
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(colors.primaryBright)
                            .clickable {
                                if (!isBatchMode) {
                                    viewModel.toggleBatchMode()
                                    viewModel.selectAllBatchQuestions(filteredQuestions.take(2).map { it.id })
                                }
                                showBatchEditPanel = !showBatchEditPanel
                            }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("batch_edit_action_btn")
                    ) {
                        Text(
                            text = if (showBatchEditPanel) "收起面板" else "批量改章节/难度",
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionEditorScreen(
    viewModel: StudyViewModel,
    initialQuestion: QuestionEntity?,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    val baseQ = initialQuestion ?: QuestionEntity(
        id = "Q_NEW",
        bank = "《铁路运输组织基础》",
        chapter = "第一章：车站工作组织",
        type = "single",
        difficulty = "中等",
        stem = "",
        optionsJson = listToJson(listOf("A. 选项 A", "B. 选项 B", "C. 选项 C", "D. 选项 D")),
        answersJson = listToJson(listOf("A")),
        explanation = ""
    )

    var type by remember(baseQ.id) { mutableStateOf(baseQ.type) }
    var difficulty by remember(baseQ.id) { mutableStateOf(baseQ.difficulty) }
    var chapter by remember(baseQ.id) { mutableStateOf(baseQ.chapter) }
    var section by remember(baseQ.id) { mutableStateOf(baseQ.section) }
    var stem by remember(baseQ.id) { mutableStateOf(baseQ.stem) }
    var explanation by remember(baseQ.id) { mutableStateOf(baseQ.explanation) }
    var mnemonic by remember(baseQ.id) { mutableStateOf(baseQ.mnemonic) }
    var trapAnalysis by remember(baseQ.id) { mutableStateOf(baseQ.trapAnalysis) }

    val optionsList = remember(baseQ.id) {
        mutableStateListOf<String>().apply {
            val initial = baseQ.options.ifEmpty {
                listOf("A. 选项 A", "B. 选项 B", "C. 选项 C", "D. 选项 D")
            }
            addAll(initial)
        }
    }
    val selectedAnswers = remember(baseQ.id) {
        mutableStateListOf<String>().apply {
            addAll(baseQ.answer.ifEmpty { listOf("A") })
        }
    }

    fun buildUpdatedEntity(): QuestionEntity {
        return baseQ.copy(
            type = type,
            difficulty = difficulty,
            chapter = chapter,
            section = section,
            stem = stem,
            optionsJson = listToJson(optionsList.toList()),
            answersJson = listToJson(selectedAnswers.toList()),
            explanation = explanation,
            mnemonic = mnemonic,
            trapAnalysis = trapAnalysis
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = if (initialQuestion == null) "加一道新题" else "改这道题",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet,
            tabletSplitActive = tabletSplitActive,
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
            actions = {
                Button(
                    onClick = { viewModel.saveEditedQuestion(buildUpdatedEntity()) },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier.testTag("editor_save_button")
                ) {
                    Text("保存", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                }
            }
        )

        if (isTablet && tabletSplitActive) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    EditorFormContent(
                        type = type,
                        onTypeChange = { newType ->
                            type = newType
                            if (newType == "judge") {
                                optionsList.clear()
                                optionsList.addAll(listOf("正确", "错误"))
                                selectedAnswers.clear()
                                selectedAnswers.add("正确")
                            }
                        },
                        difficulty = difficulty,
                        onDifficultyChange = { difficulty = it },
                        chapter = chapter,
                        onChapterChange = { chapter = it },
                        section = section,
                        onSectionChange = { section = it },
                        stem = stem,
                        onStemChange = { stem = it },
                        optionsList = optionsList,
                        selectedAnswers = selectedAnswers,
                        explanation = explanation,
                        onExplanationChange = { explanation = it },
                        mnemonic = mnemonic,
                        onMnemonicChange = { mnemonic = it },
                        trapAnalysis = trapAnalysis,
                        onTrapChange = { trapAnalysis = it },
                        onAiPolish = {
                            explanation = "这道题主要考「$section」。正确答案选 ${selectedAnswers.joinToString(",")}。$explanation"
                            if (mnemonic.isBlank()) mnemonic = "抓住核心定义就能秒选"
                            viewModel.showToast("已帮你把解析梳理得更通顺了")
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.85f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    EditorLiveStudentPreviewCard(question = buildUpdatedEntity())
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                EditorFormContent(
                    type = type,
                    onTypeChange = { newType ->
                        type = newType
                        if (newType == "judge") {
                            optionsList.clear()
                            optionsList.addAll(listOf("正确", "错误"))
                            selectedAnswers.clear()
                            selectedAnswers.add("正确")
                        }
                    },
                    difficulty = difficulty,
                    onDifficultyChange = { difficulty = it },
                    chapter = chapter,
                    onChapterChange = { chapter = it },
                    section = section,
                    onSectionChange = { section = it },
                    stem = stem,
                    onStemChange = { stem = it },
                    optionsList = optionsList,
                    selectedAnswers = selectedAnswers,
                    explanation = explanation,
                    onExplanationChange = { explanation = it },
                    mnemonic = mnemonic,
                    onMnemonicChange = { mnemonic = it },
                    trapAnalysis = trapAnalysis,
                    onTrapChange = { trapAnalysis = it },
                    onAiPolish = {
                        explanation = "这道题主要考「$section」。正确答案选 ${selectedAnswers.joinToString(",")}。$explanation"
                        if (mnemonic.isBlank()) mnemonic = "抓住核心定义就能秒选"
                        viewModel.showToast("已帮你把解析梳理得更通顺了")
                    }
                )
                EditorLiveStudentPreviewCard(question = buildUpdatedEntity())
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun EditorFormContent(
    type: String,
    onTypeChange: (String) -> Unit,
    difficulty: String,
    onDifficultyChange: (String) -> Unit,
    chapter: String,
    onChapterChange: (String) -> Unit,
    section: String,
    onSectionChange: (String) -> Unit,
    stem: String,
    onStemChange: (String) -> Unit,
    optionsList: MutableList<String>,
    selectedAnswers: MutableList<String>,
    explanation: String,
    onExplanationChange: (String) -> Unit,
    mnemonic: String,
    onMnemonicChange: (String) -> Unit,
    trapAnalysis: String,
    onTrapChange: (String) -> Unit,
    onAiPolish: () -> Unit
) {
    val colors = SereneTheme.colors

    SereneCard(cornerRadius = 22.dp) {
        Text("题型和分类", maxLines = 1, softWrap = false, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(10.dp))
        SereneFilterChipRow(
            options = listOf(
                "single" to "单选题",
                "multiple" to "多选题",
                "judge" to "判断题",
                "fill" to "填空题",
                "calc" to "计算题"
            ),
            selectedKey = type,
            onSelect = onTypeChange
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("难度", maxLines = 1, softWrap = false, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            listOf("简单", "中等", "困难").forEach { diff ->
                val active = difficulty == diff
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (active) colors.primarySoft else colors.subtleSurface)
                        .clickable { onDifficultyChange(diff) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = diff,
                        color = if (active) colors.primaryAccent else colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = chapter,
                onValueChange = onChapterChange,
                label = { Text("属于哪一章", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = section,
                onValueChange = onSectionChange,
                label = { Text("知识点小标签", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(0.8f)
            )
        }
    }

    SereneCard(cornerRadius = 22.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("题目内容", maxLines = 1, softWrap = false, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("∑hw", "τ站", "λ₁=λ₂", "[ 空位 ]").forEach { token ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.subtleSurface)
                            .clickable { onStemChange("$stem $token") }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(token, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall, color = colors.primaryAccent)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = stem,
            onValueChange = onStemChange,
            placeholder = { Text("把题目写在这里...") },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("editor_stem_input")
        )
    }

    SereneCard(cornerRadius = 22.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "选项与正确答案（点圆圈设答案）",
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            if (type in listOf("single", "multiple") && optionsList.size < 6) {
                TextButton(
                    onClick = {
                        val nextLetter = ('A' + optionsList.size).toString()
                        optionsList.add("$nextLetter. 选项 $nextLetter")
                    }
                ) {
                    Text("+ 加个选项", maxLines = 1, softWrap = false)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        optionsList.forEachIndexed { idx, optText ->
            val letter = if (type == "judge") optText else ('A' + idx).toString()
            val isCorrect = letter in selectedAnswers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isCorrect) colors.mintText else colors.subtleSurface)
                        .clickable {
                            if (type == "multiple") {
                                if (isCorrect && selectedAnswers.size > 1) selectedAnswers.remove(letter)
                                else if (!isCorrect) selectedAnswers.add(letter)
                            } else {
                                selectedAnswers.clear()
                                selectedAnswers.add(letter)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (type == "judge") letter.take(1) else letter,
                        color = if (isCorrect) Color.White else colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = optText,
                    onValueChange = { optionsList[idx] = it },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                if (optionsList.size > 2 && type != "judge") {
                    IconButton(onClick = { optionsList.removeAt(idx) }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "删除",
                            tint = colors.textMuted
                        )
                    }
                }
            }
        }
    }

    SereneCard(cornerRadius = 22.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "解析和好记的方法",
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colors.primarySoft)
                    .clickable { onAiPolish() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "帮我润色一下",
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = explanation,
            onValueChange = onExplanationChange,
            label = { Text("为什么选这个答案？") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = mnemonic,
            onValueChange = onMnemonicChange,
            label = { Text("一句话好记口诀") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = trapAnalysis,
            onValueChange = onTrapChange,
            label = { Text("最容易踩的坑") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EditorLiveStudentPreviewCard(question: QuestionEntity) {
    val colors = SereneTheme.colors
    SereneCard(
        cornerRadius = 24.dp,
        containerColor = colors.subtleSurface.copy(alpha = 0.65f),
        borderColor = colors.primaryAccent.copy(alpha = 0.25f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SereneBadge(
                text = "做题时的样子",
                containerColor = colors.primarySoft,
                contentColor = colors.primaryAccent
            )
            Text(
                text = "${question.typeLabel} · ${question.difficulty}",
                color = colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = question.stem.ifBlank { "题目内容会显示在这里..." },
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        question.options.forEachIndexed { idx, opt ->
            val code = if (question.type == "judge") opt else ('A' + idx).toString()
            val isAns = code in question.answer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isAns) colors.mintBg else colors.cardSurface)
                    .border(
                        width = 1.dp,
                        color = if (isAns) colors.mintText else colors.borderSubtle,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = opt,
                    color = if (isAns) colors.mintText else colors.textPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isAns) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
        if (question.explanation.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = question.explanation,
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
