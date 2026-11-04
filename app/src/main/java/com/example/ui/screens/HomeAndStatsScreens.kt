package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PracticeRecordEntity
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.UserSettingsState
import com.example.ui.components.EmptyInboxIllustration
import com.example.ui.components.ReviewCurveIllustration
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.components.SereneFilterChipRow
import com.example.ui.components.SereneLinearProgress
import com.example.ui.components.SereneTopBar
import com.example.ui.components.StudyHeroIllustration
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun HomeScreen(
    viewModel: StudyViewModel,
    banks: List<QuestionBankEntity>,
    questions: List<QuestionEntity>,
    settings: UserSettingsState,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    var selectedBankIndex by remember { mutableIntStateOf(0) }
    val activeBank = banks.getOrNull(selectedBankIndex) ?: banks.firstOrNull() ?: QuestionBankEntity(
        name = "《铁路运输组织基础》",
        subtitle = "车站工作 · 车流组织 · 列车运行图",
        domainTag = "railway",
        totalTargetCount = 186,
        practicedCount = 64,
        accuracyPercent = 78
    )
    val wrongCount = questions.count { it.wrongCount > 0 }

    Column(modifier = Modifier.fillMaxSize()) {
        // Compact Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "知序 Study",
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
                SereneBadge(
                    text = "连续打卡 7 天",
                    containerColor = colors.peachBg,
                    contentColor = colors.peachText
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.AI_SERVICE) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.primarySoft)
                        .testTag("home_ai_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = "AI 助手",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.cardSurface)
                        .border(1.dp, colors.borderSubtle, CircleShape)
                        .testTag("home_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "设置",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
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
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        HomeHeroBannerCard(
                            dailyDone = settings.dailyDoneCount,
                            dailyTarget = settings.dailyTargetCount,
                            onAdjustPlan = { viewModel.navigateTo(AppScreen.REVIEW_PLAN) }
                        )
                    }
                    item {
                        HomeQuickActionGrid(
                            wrongCount = wrongCount,
                            onStartPractice = { viewModel.openPracticeConfig(activeBank.name) },
                            onWrongReview = { viewModel.startQuickReviewSession() },
                            onImport = { viewModel.navigateTo(AppScreen.IMPORT_SOURCE) },
                            onHistory = { viewModel.navigateTo(AppScreen.HISTORY) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        HomeRecentBankCard(
                            banks = banks,
                            selectedBankIndex = selectedBankIndex,
                            onSelectBankIndex = { selectedBankIndex = it },
                            bank = activeBank,
                            onContinue = { viewModel.openPracticeConfig(activeBank.name) },
                            onQuickFive = { viewModel.startQuickFiveSprint(activeBank.name) },
                            onOpenDetail = { viewModel.openBankDetail(activeBank.name) }
                        )
                    }
                    item {
                        HomeAuditShortcutBanner(
                            onOpenAudit = { viewModel.navigateTo(AppScreen.AUDIT) }
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    HomeHeroBannerCard(
                        dailyDone = settings.dailyDoneCount,
                        dailyTarget = settings.dailyTargetCount,
                        onAdjustPlan = { viewModel.navigateTo(AppScreen.REVIEW_PLAN) }
                    )
                }
                item {
                    HomeRecentBankCard(
                        banks = banks,
                        selectedBankIndex = selectedBankIndex,
                        onSelectBankIndex = { selectedBankIndex = it },
                        bank = activeBank,
                        onContinue = { viewModel.openPracticeConfig(activeBank.name) },
                        onQuickFive = { viewModel.startQuickFiveSprint(activeBank.name) },
                        onOpenDetail = { viewModel.openBankDetail(activeBank.name) }
                    )
                }
                item {
                    HomeQuickActionGrid(
                        wrongCount = wrongCount,
                        onStartPractice = { viewModel.openPracticeConfig(activeBank.name) },
                        onWrongReview = { viewModel.startQuickReviewSession() },
                        onImport = { viewModel.navigateTo(AppScreen.IMPORT_SOURCE) },
                        onHistory = { viewModel.navigateTo(AppScreen.HISTORY) }
                    )
                }
                item {
                    HomeAuditShortcutBanner(
                        onOpenAudit = { viewModel.navigateTo(AppScreen.AUDIT) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeroBannerCard(
    dailyDone: Int,
    dailyTarget: Int,
    onAdjustPlan: () -> Unit
) {
    val colors = SereneTheme.colors
    val progress = (dailyDone.toFloat() / dailyTarget.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val remaining = (dailyTarget - dailyDone).coerceAtLeast(0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = if (colors.isDark) {
                        listOf(Color(0xFF252A44), Color(0xFF1D2136))
                    } else {
                        listOf(Color(0xFFE6E5FA), Color(0xFFF3EFFC))
                    }
                )
            )
            .border(1.dp, colors.primaryAccent.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SereneBadge(
                        text = if (remaining == 0) "目标已达成" else "今日剩余 $remaining 题",
                        containerColor = colors.cardSurface.copy(alpha = 0.88f),
                        contentColor = colors.primaryAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "今日学习进度",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                StudyHeroIllustration(size = 84.dp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.cardSurface.copy(alpha = 0.92f))
                    .clickable { onAdjustPlan() }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("home_daily_plan_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "今日进度",
                                color = colors.textSecondary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$dailyDone / $dailyTarget 题",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "改计划",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    SereneLinearProgress(progress = progress, height = 8.dp)
                }
            }
        }
    }
}

@Composable
private fun HomeRecentBankCard(
    banks: List<QuestionBankEntity>,
    selectedBankIndex: Int,
    onSelectBankIndex: (Int) -> Unit,
    bank: QuestionBankEntity,
    onContinue: () -> Unit,
    onQuickFive: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val colors = SereneTheme.colors
    val progress = (bank.practicedCount.toFloat() / bank.totalTargetCount.coerceAtLeast(1)).coerceIn(0f, 1f)

    SereneCard(
        cornerRadius = 22.dp,
        onClick = onOpenDetail,
        modifier = Modifier.testTag("home_recent_bank_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SereneBadge(
                text = "当前题库",
                containerColor = colors.primarySoft,
                contentColor = colors.primaryAccent
            )
            Text(
                text = "详情 >",
                color = colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall
            )
        }

        if (banks.size > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                banks.forEachIndexed { idx, item ->
                    val selected = idx == selectedBankIndex
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (selected) colors.primaryAccent else colors.subtleSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onSelectBankIndex(idx) }
                    ) {
                        Text(
                            text = item.name.removeSurrounding("《", "》"),
                            color = if (selected) Color.White else colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        val (bookBg, bookFg) = when (bank.domainTag) {
            "highway" -> colors.mintBg to colors.mintText
            "exam" -> colors.peachBg to colors.peachText
            "cs_math" -> colors.amberBg to colors.amberText
            else -> colors.primarySoft to colors.primaryAccent
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(bookBg)
                    .border(1.dp, bookFg.copy(alpha = 0.22f), RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = bookFg,
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
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "已做 ${bank.practicedCount} / ${bank.totalTargetCount} 题 · 正确率 ${bank.accuracyPercent}%",
                color = colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                color = colors.primaryAccent,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SereneLinearProgress(progress = progress)
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onQuickFive,
                modifier = Modifier
                    .weight(0.45f)
                    .height(46.dp)
                    .testTag("home_quick_five_button"),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = null,
                    tint = colors.primaryAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "速练 5 题",
                    color = colors.primaryAccent,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .weight(0.55f)
                    .height(46.dp)
                    .testTag("home_continue_practice_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "继续刷题",
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun HomeQuickActionGrid(
    wrongCount: Int,
    onStartPractice: () -> Unit,
    onWrongReview: () -> Unit,
    onImport: () -> Unit,
    onHistory: () -> Unit
) {
    val colors = SereneTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionTile(
                modifier = Modifier.weight(1f),
                title = "选模式刷题",
                subtitle = "顺序 · 随机 · 模考",
                icon = Icons.Outlined.PlayCircleOutline,
                iconBg = colors.primarySoft,
                iconTint = colors.primaryAccent,
                tag = "quick_start_practice",
                onClick = onStartPractice
            )
            QuickActionTile(
                modifier = Modifier.weight(1f),
                title = "消灭错题",
                subtitle = "还有 $wrongCount 道错题",
                icon = Icons.Outlined.Refresh,
                iconBg = colors.peachBg,
                iconTint = colors.peachText,
                tag = "quick_wrong_review",
                onClick = onWrongReview
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionTile(
                modifier = Modifier.weight(1f),
                title = "导入新题",
                subtitle = "支持表格与文本",
                icon = Icons.Outlined.CloudUpload,
                iconBg = colors.mintBg,
                iconTint = colors.mintText,
                tag = "quick_import_bank",
                onClick = onImport
            )
            QuickActionTile(
                modifier = Modifier.weight(1f),
                title = "做题记录",
                subtitle = "每次练习的成绩",
                icon = Icons.Outlined.History,
                iconBg = colors.amberBg,
                iconTint = colors.amberText,
                tag = "quick_history",
                onClick = onHistory
            )
        }
    }
}

@Composable
private fun QuickActionTile(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    tag: String,
    onClick: () -> Unit
) {
    val colors = SereneTheme.colors
    SereneCard(
        modifier = modifier.testTag(tag),
        cornerRadius = 18.dp,
        contentPadding = PaddingValues(14.dp),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = colors.textSecondary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun HomeAuditShortcutBanner(onOpenAudit: () -> Unit) {
    val colors = SereneTheme.colors
    SereneCard(
        cornerRadius = 18.dp,
        containerColor = colors.subtleSurface,
        onClick = onOpenAudit,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.testTag("home_audit_shortcut")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(colors.mintBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VerifiedUser,
                        contentDescription = null,
                        tint = colors.mintText,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "题库查重与答案核对",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "发现 2 道题在不同题库里答案不一致，点此处理",
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
fun ReviewScreen(
    viewModel: StudyViewModel,
    questions: List<QuestionEntity>,
    activeFilter: String,
    searchQuery: String,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    var showSearchBar by remember { mutableStateOf(false) }
    var peekedQuestionIds by remember { mutableStateOf(setOf<String>()) }

    val wrongList = questions.filter { it.wrongCount > 0 }
    val starredList = questions.filter { it.isStarred }
    val hardList = questions.filter { it.difficulty == "困难" || it.isFlagged || it.wrongCount >= 2 }
    val todayDueList = questions.filter { it.wrongCount > 0 || it.isStarred }

    val displayedList = remember(questions, activeFilter, searchQuery) {
        val base = when (activeFilter) {
            "wrong" -> wrongList
            "starred" -> starredList
            "hard" -> hardList
            else -> todayDueList
        }
        if (searchQuery.isBlank()) base
        else base.filter {
            it.stem.contains(searchQuery, ignoreCase = true) ||
                it.section.contains(searchQuery, ignoreCase = true) ||
                it.chapter.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SereneTopBar(
                title = "复习",
                isTablet = isTablet,
                actions = {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        val targets = if (displayedList.isNotEmpty()) displayedList else wrongList
                        if (targets.isEmpty()) {
                            viewModel.showToast("当前暂无错题可导出")
                        } else {
                            WrongBookPrintHelper.printWrongQuestions(context, targets)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Print,
                            contentDescription = "打印或导出PDF",
                            tint = colors.primaryAccent
                        )
                    }
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "搜索",
                            tint = colors.textPrimary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.REVIEW_PLAN) },
                        modifier = Modifier.testTag("review_plan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "复习计划",
                            tint = colors.primaryAccent
                        )
                    }
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                        modifier = Modifier.testTag("review_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "设置",
                            tint = colors.textPrimary
                        )
                    }
                }
            )

            AnimatedVisibility(visible = showSearchBar) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setReviewSearch(it) },
                    placeholder = { Text("搜错题、章节或自己的笔记...", maxLines = 1, softWrap = false) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.cardSurface,
                        unfocusedContainerColor = colors.cardSurface
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = if (isTablet) 24.dp else 16.dp,
                    end = if (isTablet) 24.dp else 16.dp,
                    top = 6.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (colors.isDark) {
                                        listOf(Color(0xFF33221E), Color(0xFF221E30))
                                    } else {
                                        listOf(Color(0xFFFAEDE8), Color(0xFFF3EEFA))
                                    }
                                )
                            )
                            .border(1.dp, colors.peachText.copy(alpha = 0.16f), RoundedCornerShape(22.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SereneBadge(
                                    text = "● 趁热打铁不遗忘",
                                    containerColor = colors.cardSurface.copy(alpha = 0.9f),
                                    contentColor = colors.peachText
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "温故，而知新",
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "今天有 ${todayDueList.size} 道容易忘的题等你过一遍",
                                    color = colors.textSecondary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "调整每日题量 →",
                                    color = colors.primaryAccent,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.REVIEW_PLAN) }
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            ReviewCurveIllustration(size = 78.dp)
                        }
                    }
                }

                // Compact 4-Metric Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReviewMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "今天该看",
                            count = todayDueList.size.toString(),
                            countColor = colors.peachText,
                            isSelected = activeFilter == "today",
                            onClick = { viewModel.setReviewFilter("today") }
                        )
                        ReviewMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "错题本",
                            count = wrongList.size.toString(),
                            countColor = colors.dangerText,
                            isSelected = activeFilter == "wrong",
                            onClick = { viewModel.setReviewFilter("wrong") }
                        )
                        ReviewMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "收藏夹",
                            count = starredList.size.toString(),
                            countColor = colors.primaryAccent,
                            isSelected = activeFilter == "starred",
                            onClick = { viewModel.setReviewFilter("starred") }
                        )
                        ReviewMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "拿不准",
                            count = hardList.size.toString(),
                            countColor = colors.amberText,
                            isSelected = activeFilter == "hard",
                            onClick = { viewModel.setReviewFilter("hard") }
                        )
                    }
                }

                item {
                    SereneFilterChipRow(
                        options = listOf(
                            "today" to "今天该看 (${todayDueList.size})",
                            "wrong" to "常错的题 (${wrongList.size})",
                            "starred" to "收藏的题 (${starredList.size})",
                            "hard" to "拿不准的 (${hardList.size})"
                        ),
                        selectedKey = activeFilter,
                        onSelect = { viewModel.setReviewFilter(it) }
                    )
                }

                if (displayedList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            EmptyInboxIllustration(size = 130.dp)
                            Text(
                                text = "暂无待复习错题",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = colors.textPrimary
                            )
                            Text(
                                text = "题本干净清爽，重点知识掌握牢固",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }
                    }
                }

                items(displayedList, key = { it.id }) { q ->
                    val isPeeked = q.id in peekedQuestionIds
                    SereneCard(
                        cornerRadius = 20.dp,
                        onClick = { viewModel.startQuickReviewSession(q) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                SereneBadge(
                                    text = q.bank.removeSurrounding("《", "》").take(6),
                                    containerColor = colors.primarySoft,
                                    contentColor = colors.primaryAccent
                                )
                                SereneBadge(
                                    text = q.section,
                                    containerColor = colors.subtleSurface,
                                    contentColor = colors.textSecondary
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (q.wrongCount > 0) {
                                    SereneBadge(
                                        text = "错 ${q.wrongCount} 次",
                                        containerColor = colors.peachBg,
                                        contentColor = colors.peachText
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.toggleQuestionStar(q.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (q.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "收藏",
                                        tint = if (q.isStarred) colors.amberText else colors.textMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = q.stem,
                            color = colors.textPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (q.mnemonic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(colors.subtleSurface)
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = colors.amberText,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "巧记：${q.mnemonic}",
                                    color = colors.textSecondary,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Inline Answer Peek Area
                        AnimatedVisibility(visible = isPeeked) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = colors.mintBg.copy(alpha = 0.65f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "正确答案：${q.answer.joinToString("、")}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.mintText,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = q.explanation.ifBlank { "记住核心条件即可快速选出。" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = colors.subtleSurface,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            peekedQuestionIds = if (isPeeked) {
                                                peekedQuestionIds - q.id
                                            } else {
                                                peekedQuestionIds + q.id
                                            }
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Visibility,
                                            contentDescription = null,
                                            tint = colors.textSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (isPeeked) "收起答案" else "看一眼答案",
                                            color = colors.textSecondary,
                                            maxLines = 1,
                                            softWrap = false,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }

                                if (q.wrongCount > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = colors.mintBg,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .clickable { viewModel.markQuestionMastered(q.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.CheckCircleOutline,
                                                contentDescription = null,
                                                tint = colors.mintText,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = "学会了",
                                                color = colors.mintText,
                                                maxLines = 1,
                                                softWrap = false,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(colors.primarySoft)
                                    .clickable { viewModel.startQuickReviewSession(q) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "去重做 ▷",
                                    color = colors.primaryAccent,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { viewModel.startQuickReviewSession() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_today_review_button"),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                    ) {
                        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "集中重做这 ${todayDueList.size} 道题",
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    countColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = SereneTheme.colors
    SereneCard(
        modifier = modifier,
        cornerRadius = 16.dp,
        borderColor = if (isSelected) colors.primaryAccent else colors.borderSubtle,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
        onClick = onClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = count,
                color = countColor,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = if (isSelected) colors.primaryAccent else colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun StatsScreen(
    viewModel: StudyViewModel,
    questions: List<QuestionEntity>,
    records: List<PracticeRecordEntity>,
    statsRange: String,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    var selectedDayIdx by remember { mutableIntStateOf(6) }

    val multiplier = when (statsRange) {
        "30d" -> 3
        "all" -> 6
        else -> 1
    }
    val practicedTotal = 148 * multiplier
    val accuracyAvg = if (statsRange == "7d") 82 else if (statsRange == "30d") 79 else 81
    val studyHours = if (statsRange == "7d") "6.4 小时" else if (statsRange == "30d") "24.8 小时" else "68.5 小时"
    val streakDays = if (statsRange == "7d") "7 天" else "19 天"

    val chartValues = when (statsRange) {
        "30d" -> listOf(28, 35, 22, 44, 38, 50, 42)
        "all" -> listOf(45, 52, 38, 64, 58, 72, 66)
        else -> listOf(16, 24, 18, 28, 22, 32, 20)
    }
    val dayLabels = listOf("周一", "周二", "周三", "周四", "周五", "周六", "今天")

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "学习统计",
            isTablet = isTablet,
            actions = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(colors.primarySoft)
                        .clickable { viewModel.navigateTo(AppScreen.HISTORY) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("stats_history_button")
                ) {
                    Text(
                        text = "做题记录",
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    modifier = Modifier.testTag("stats_settings_button")
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
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                SereneFilterChipRow(
                    options = listOf(
                        "7d" to "最近 7 天",
                        "30d" to "最近 30 天",
                        "all" to "全部累计"
                    ),
                    selectedKey = statsRange,
                    onSelect = { viewModel.setStatsRange(it) }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatsKpiCard(
                            modifier = Modifier.weight(1f),
                            label = "做过的题",
                            value = "$practicedTotal 题",
                            trend = "比上周多做 18%",
                            accentColor = colors.primaryAccent
                        )
                        StatsKpiCard(
                            modifier = Modifier.weight(1f),
                            label = "做对比例",
                            value = "$accuracyAvg%",
                            trend = "越来越稳",
                            accentColor = colors.mintText
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatsKpiCard(
                            modifier = Modifier.weight(1f),
                            label = "投入时间",
                            value = studyHours,
                            trend = "每天约 52 分钟",
                            accentColor = colors.amberText
                        )
                        StatsKpiCard(
                            modifier = Modifier.weight(1f),
                            label = "连续坚持",
                            value = streakDays,
                            trend = "继续保持",
                            accentColor = colors.peachText
                        )
                    }
                }
            }

            item {
                SereneCard(cornerRadius = 22.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "每天做了多少题",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        SereneBadge(
                            text = "${dayLabels[selectedDayIdx]}：${chartValues[selectedDayIdx]} 题",
                            containerColor = colors.primarySoft,
                            contentColor = colors.primaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    val maxVal = (chartValues.maxOrNull() ?: 30).coerceAtLeast(1)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        val barCount = chartValues.size
                        val slotWidth = size.width / barCount
                        val barWidth = (slotWidth * 0.44f).coerceAtMost(42f)
                        chartValues.forEachIndexed { idx, value ->
                            val ratio = value.toFloat() / maxVal.toFloat()
                            val barHeight = (size.height * 0.82f * ratio).coerceAtLeast(14f)
                            val left = idx * slotWidth + (slotWidth - barWidth) / 2f
                            val top = size.height - barHeight
                            drawRoundRect(
                                color = colors.subtleSurface,
                                topLeft = Offset(left, size.height * 0.12f),
                                size = Size(barWidth, size.height * 0.88f),
                                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                            )
                            drawRoundRect(
                                color = if (idx == selectedDayIdx) colors.primaryAccent else colors.primaryBright.copy(alpha = 0.48f),
                                topLeft = Offset(left, top),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        dayLabels.forEachIndexed { idx, day ->
                            val isSelected = idx == selectedDayIdx
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedDayIdx = idx }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${chartValues[idx]}",
                                    color = if (isSelected) colors.primaryAccent else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = day,
                                    color = if (isSelected) colors.primaryAccent else colors.textMuted,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                SereneCard(cornerRadius = 22.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "各章节熟悉程度（点一下直接练）",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Icon(
                            imageVector = Icons.Outlined.Psychology,
                            contentDescription = null,
                            tint = colors.peachText
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    val masteryItems = listOf(
                        Triple("驼峰溜放与减速器制动", 45, colors.dangerText),
                        Triple("区间通过能力与运行图", 58, colors.peachText),
                        Triple("高等数学 · 泰勒展开式", 64, colors.amberText),
                        Triple("处理器流水线冲突处理", 72, colors.primaryAccent),
                        Triple("车站接发列车安全规程", 86, colors.mintText)
                    )

                    masteryItems.forEachIndexed { index, (topic, pct, barColor) ->
                        if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.startQuickReviewSession() }
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = topic,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (pct < 60) "$pct% · 建议多练" else "$pct%",
                                    color = barColor,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            SereneLinearProgress(
                                progress = pct / 100f,
                                indicatorColor = barColor,
                                height = 7.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsKpiCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    trend: String,
    accentColor: Color
) {
    val colors = SereneTheme.colors
    SereneCard(
        modifier = modifier,
        cornerRadius = 18.dp,
        contentPadding = PaddingValues(14.dp)
    ) {
        Text(
            text = label,
            color = colors.textSecondary,
            maxLines = 1,
            softWrap = false,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            maxLines = 1,
            softWrap = false,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = trend,
            color = accentColor,
            maxLines = 1,
            softWrap = false,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}
