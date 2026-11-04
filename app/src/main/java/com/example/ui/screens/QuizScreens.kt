package com.example.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import com.example.ui.components.bounceClick
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PracticeRecordEntity
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.ui.components.CompletionVectorIllustration
import com.example.ui.components.MilestoneMedalIllustration
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.components.SereneFilterChipRow
import com.example.ui.components.SereneFormulaCard
import com.example.ui.components.SereneLinearProgress
import com.example.ui.components.SereneTopBar
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.ActiveQuizSession
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizConfigState
import com.example.ui.viewmodel.ScratchpadStroke
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun PracticeConfigScreen(
    viewModel: StudyViewModel,
    config: QuizConfigState,
    banks: List<QuestionBankEntity>,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "练习配置",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = if (isTablet) 28.dp else 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                SereneCard(cornerRadius = 22.dp) {
                    Text(
                        text = "目标题库",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        banks.forEach { bank ->
                            val selected = bank.name == config.bankName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (selected) colors.primaryAccent else colors.subtleSurface)
                                    .clickable {
                                        viewModel.updateQuizConfig { it.copy(bankName = bank.name) }
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = bank.name,
                                    color = if (selected) Color.White else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            item {
                SereneCard(cornerRadius = 22.dp) {
                    Text(
                        text = "做题模式",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val modes = listOf("顺序练习", "随机练习", "模拟考试", "背题模式")
                    modes.chunked(2).forEach { rowModes ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowModes.forEach { modeTitle ->
                                val active = config.mode == modeTitle
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (active) colors.primarySoft else colors.subtleSurface)
                                        .border(
                                            width = 1.5.dp,
                                            color = if (active) colors.primaryAccent else Color.Transparent,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            viewModel.updateQuizConfig { it.copy(mode = modeTitle) }
                                        }
                                        .padding(vertical = 12.dp, horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = modeTitle,
                                        color = if (active) colors.primaryAccent else colors.textPrimary,
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

            item {
                SereneCard(cornerRadius = 22.dp) {
                    Text(
                        text = "题量与范围",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SereneFilterChipRow(
                        options = listOf(
                            "全部题目" to "全部题目",
                            "错题" to "错题本",
                            "收藏" to "重点收藏"
                        ),
                        selectedKey = config.range,
                        onSelect = { r -> viewModel.updateQuizConfig { it.copy(range = r) } }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("题目数量", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${config.count} 题",
                            color = colors.primaryAccent,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                    Slider(
                        value = config.count.toFloat(),
                        onValueChange = { v -> viewModel.updateQuizConfig { it.copy(count = v.toInt().coerceAtLeast(1)) } },
                        valueRange = 5f..30f
                    )
                }
            }

            item {
                SereneCard(cornerRadius = 22.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("即时显示解析", fontWeight = FontWeight.SemiBold, color = colors.textPrimary, maxLines = 1, softWrap = false)
                        Switch(
                            checked = config.instantExplanation,
                            onCheckedChange = { v -> viewModel.updateQuizConfig { it.copy(instantExplanation = v) } }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("选项随机乱序", fontWeight = FontWeight.SemiBold, color = colors.textPrimary, maxLines = 1, softWrap = false)
                        Switch(
                            checked = config.shuffleOptions,
                            onCheckedChange = { v -> viewModel.updateQuizConfig { it.copy(shuffleOptions = v) } }
                        )
                    }
                }
            }
        }

        Surface(color = colors.cardSurface, shadowElevation = 10.dp) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { viewModel.startQuizFromConfig() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("start_configured_quiz_btn"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                ) {
                    Icon(Icons.Outlined.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("开始做题", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    viewModel: StudyViewModel,
    session: ActiveQuizSession,
    hapticEnabled: Boolean,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    val localView = LocalView.current
    val questions = session.questions
    val currentQ = questions.getOrNull(session.currentIndex)

    if (currentQ == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { viewModel.navigateBack() }) {
                Text("返回", maxLines = 1, softWrap = false)
            }
        }
        return
    }

    val isSubmitted = session.submittedMap[currentQ.id] == true
    val userSelected = session.userSelections[currentQ.id] ?: emptyList()
    val answeredCount = session.submittedMap.count { it.value }
    val minutes = (session.elapsedSeconds / 60).toString().padStart(2, '0')
    val seconds = (session.elapsedSeconds % 60).toString().padStart(2, '0')
    val excludedOptionsMap = remember { mutableStateMapOf<String, Set<String>>() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SereneTopBar(
                title = "第 ${session.currentIndex + 1} / ${questions.size} 题",
                subtitle = "已用时 $minutes:$seconds",
                onBack = { viewModel.navigateBack() },
                isTablet = isTablet,
                tabletSplitActive = tabletSplitActive,
                onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleScratchpad() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (session.showScratchpad) colors.primarySoft else colors.subtleSurface)
                            .testTag("quiz_scratchpad_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Draw,
                            contentDescription = "草稿纸",
                            tint = if (session.showScratchpad) colors.primaryAccent else colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.toggleQuestionStar(currentQ.id) },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("quiz_star_toggle")
                    ) {
                        Icon(
                            imageVector = if (currentQ.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "收藏",
                            tint = if (currentQ.isStarred) colors.amberText else colors.textPrimary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.toggleQuestionFlag(currentQ.id) },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("quiz_flag_toggle")
                    ) {
                        Icon(
                            imageVector = if (currentQ.isFlagged) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "拿不准",
                            tint = if (currentQ.isFlagged) colors.peachText else colors.textPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(colors.primarySoft)
                            .clickable { viewModel.toggleAnswerSheet() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("quiz_answer_sheet_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.GridView,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "题号卡 $answeredCount/${questions.size}",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            )

            SereneLinearProgress(
                progress = (session.currentIndex + 1).toFloat() / questions.size.coerceAtLeast(1),
                height = 4.dp,
                modifier = Modifier.padding(horizontal = if (isTablet) 24.dp else 16.dp)
            )

            val currentExcluded = excludedOptionsMap[currentQ.id] ?: emptySet()
            val toggleExclude: (String) -> Unit = { code ->
                val next = if (code in currentExcluded) currentExcluded - code else currentExcluded + code
                excludedOptionsMap[currentQ.id] = next
            }

            if (isTablet && tabletSplitActive) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    SereneCard(
                        modifier = Modifier
                            .weight(0.34f)
                            .fillMaxHeight(),
                        cornerRadius = 24.dp,
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "这组题列表",
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                text = "共 ${questions.size} 题",
                                color = colors.textSecondary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            itemsIndexed(questions, key = { _, q -> q.id }) { idx, q ->
                                val isCurrent = idx == session.currentIndex
                                val qSub = session.submittedMap[q.id] == true
                                val qUserAns = session.userSelections[q.id] ?: emptyList()
                                val qCorrect = qSub && viewModel.evaluateQuestionCorrectness(q, qUserAns)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isCurrent) colors.primarySoft.copy(alpha = 0.55f) else colors.subtleSurface.copy(alpha = 0.5f))
                                        .border(
                                            width = if (isCurrent) 2.dp else 1.dp,
                                            color = if (isCurrent) colors.primaryAccent else colors.borderSubtle,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { viewModel.jumpToQuestion(idx) }
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "第 ${(idx + 1).toString().padStart(2, '0')} 题",
                                                    color = colors.textPrimary,
                                                    maxLines = 1,
                                                    softWrap = false,
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                                )
                                                SereneBadge(
                                                    text = q.typeLabel.take(2),
                                                    containerColor = colors.primarySoft,
                                                    contentColor = colors.primaryAccent
                                                )
                                            }
                                            Text(
                                                text = when {
                                                    isCurrent -> "● 正在做"
                                                    qSub && qCorrect -> "✓ 答对了"
                                                    qSub && !qCorrect -> "✕ 答错了"
                                                    else -> "还没做"
                                                },
                                                color = when {
                                                    isCurrent -> colors.primaryAccent
                                                    qSub && qCorrect -> colors.mintText
                                                    qSub && !qCorrect -> colors.dangerText
                                                    else -> colors.textMuted
                                                },
                                                maxLines = 1,
                                                softWrap = false,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = q.stem,
                                            color = colors.textSecondary,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.66f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        QuizActiveQuestionContent(
                            viewModel = viewModel,
                            session = session,
                            currentQ = currentQ,
                            userSelected = userSelected,
                            excludedOptions = currentExcluded,
                            onToggleExclude = toggleExclude,
                            isSubmitted = isSubmitted,
                            isTabletGridOptions = true,
                            onOptionClick = { code ->
                                if (hapticEnabled) {
                                    localView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                }
                                viewModel.selectQuizOption(code)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    QuizActiveQuestionContent(
                        viewModel = viewModel,
                        session = session,
                        currentQ = currentQ,
                        userSelected = userSelected,
                        excludedOptions = currentExcluded,
                        onToggleExclude = toggleExclude,
                        isSubmitted = isSubmitted,
                        isTabletGridOptions = false,
                        onOptionClick = { code ->
                            if (hapticEnabled) {
                                localView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            }
                            viewModel.selectQuizOption(code)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom Navigation & Submit Bar
            Surface(
                color = colors.cardSurface,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.jumpToQuestion(session.currentIndex - 1) },
                            enabled = session.currentIndex > 0,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("quiz_prev_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("上一题", maxLines = 1, softWrap = false)
                        }
                        OutlinedButton(
                            onClick = { viewModel.jumpToQuestion(session.currentIndex + 1) },
                            enabled = session.currentIndex < questions.lastIndex,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("quiz_next_btn")
                        ) {
                            Text("下一题", maxLines = 1, softWrap = false)
                            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }

                    if (!isSubmitted) {
                        Button(
                            onClick = { viewModel.submitCurrentQuestion() },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                            modifier = Modifier.testTag("quiz_submit_current_btn")
                        ) {
                            Text("选好了，看对错", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null, modifier = Modifier.size(15.dp))
                        }
                    } else if (session.currentIndex < questions.lastIndex) {
                        Button(
                            onClick = { viewModel.jumpToQuestion(session.currentIndex + 1) },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                        ) {
                            Text("接着做下一题 →", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.finishQuizSession() },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.mintText),
                            modifier = Modifier.testTag("quiz_finish_btn")
                        ) {
                            Text("做完了，看总分", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }

        if (session.showAnswerSheet) {
            AlertDialog(
                onDismissRequest = { viewModel.toggleAnswerSheet(false) },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("点击题号直接跳过去", maxLines = 1, softWrap = false, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                        IconButton(onClick = { viewModel.toggleAnswerSheet(false) }) {
                            Icon(Icons.Outlined.Close, contentDescription = "关闭")
                        }
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        val rows = questions.chunked(5)
                        rows.forEachIndexed { rIdx, rowQs ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowQs.forEachIndexed { cIdx, q ->
                                    val globalIdx = rIdx * 5 + cIdx
                                    val isCurrent = globalIdx == session.currentIndex
                                    val sub = session.submittedMap[q.id] == true
                                    val uAns = session.userSelections[q.id] ?: emptyList()
                                    val corr = sub && viewModel.evaluateQuestionCorrectness(q, uAns)

                                    val cellBg = when {
                                        isCurrent -> colors.primaryAccent
                                        sub && corr -> colors.mintBg
                                        sub && !corr -> colors.dangerBg
                                        else -> colors.subtleSurface
                                    }
                                    val cellFg = when {
                                        isCurrent -> Color.White
                                        sub && corr -> colors.mintText
                                        sub && !corr -> colors.dangerText
                                        else -> colors.textSecondary
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(cellBg)
                                            .clickable { viewModel.jumpToQuestion(globalIdx) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${globalIdx + 1}",
                                            color = cellFg,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                                repeat(5 - rowQs.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text("● 正在做", color = colors.primaryAccent, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                            Text("● 答对了", color = colors.mintText, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                            Text("● 答错了", color = colors.dangerText, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                            Text("● 还没做", color = colors.textMuted, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.finishQuizSession() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                    ) {
                        Text("现在交卷看总分", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                    }
                }
            )
        }
    }
}

@Composable
private fun QuizActiveQuestionContent(
    viewModel: StudyViewModel,
    session: ActiveQuizSession,
    currentQ: QuestionEntity,
    userSelected: List<String>,
    excludedOptions: Set<String>,
    onToggleExclude: (String) -> Unit,
    isSubmitted: Boolean,
    isTabletGridOptions: Boolean,
    onOptionClick: (String) -> Unit
) {
    val colors = SereneTheme.colors
    var showNoteEditor by remember(currentQ.id) { mutableStateOf(false) }
    var noteInput by remember(currentQ.id) { mutableStateOf(currentQ.mnemonic) }

    SereneCard(cornerRadius = 24.dp, contentPadding = PaddingValues(18.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(colors.primaryAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${session.currentIndex + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Text(
                    text = currentQ.typeLabel,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "${currentQ.score} 分",
                    color = colors.textSecondary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            SereneBadge(
                text = currentQ.difficulty,
                containerColor = colors.primarySoft,
                contentColor = colors.primaryAccent
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = currentQ.stem,
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )
        )

        if (currentQ.formulaHint.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            SereneFormulaCard(
                formula = currentQ.formulaHint,
                label = "核心计算公式与推演模型"
            )
        }

        if (currentQ.referenceTableJson.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.subtleSurface.copy(alpha = 0.7f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "题目给出的已知条件",
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                currentQ.referenceTableJson.split("|").chunked(2).forEach { rowParams ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowParams.forEach { param ->
                            Text(
                                text = "• $param",
                                color = colors.textSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    when (currentQ.type) {
        "fill", "calc" -> {
            SereneCard(cornerRadius = 22.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentQ.type == "calc") "计算结果" else "作答填空",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { viewModel.toggleScratchpad(true) }) {
                        Icon(Icons.Outlined.Draw, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("草稿纸", maxLines = 1, softWrap = false)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                currentQ.answer.forEachIndexed { slotIdx, _ ->
                    val label = currentQ.options.getOrNull(slotIdx) ?: "第 ${slotIdx + 1} 个空"
                    val currentVal = userSelected.getOrNull(slotIdx) ?: ""
                    OutlinedTextField(
                        value = currentVal,
                        onValueChange = { viewModel.updateFillOrCalcInput(slotIdx, it) },
                        label = { Text(label, maxLines = 1) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("quiz_fill_input_$slotIdx")
                    )
                }
            }
        }

        "judge" -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                listOf("正确" to "✓", "错误" to "✕").forEach { (label, symbol) ->
                    val isSelected = label in userSelected
                    val isCorrectAns = label in currentQ.answer
                    val targetJudgeBg = when {
                        isSubmitted && isCorrectAns -> colors.mintBg
                        isSubmitted && isSelected && !isCorrectAns -> colors.dangerBg
                        isSelected -> colors.primarySoft
                        else -> colors.cardSurface
                    }
                    val targetJudgeBorder = when {
                        isSubmitted && isCorrectAns -> colors.mintText
                        isSubmitted && isSelected && !isCorrectAns -> colors.dangerText
                        isSelected -> colors.primaryAccent
                        else -> colors.borderSubtle
                    }
                    val animatedJudgeBg by animateColorAsState(targetValue = targetJudgeBg, animationSpec = tween(180), label = "judge_bg")
                    val animatedJudgeBorder by animateColorAsState(targetValue = targetJudgeBorder, animationSpec = tween(180), label = "judge_border")

                    SereneCard(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick(scaleDown = 0.97f) { onOptionClick(label) }
                            .testTag("quiz_judge_option_$label"),
                        cornerRadius = 22.dp,
                        containerColor = animatedJudgeBg,
                        borderColor = animatedJudgeBorder
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(animatedJudgeBorder.copy(alpha = 0.16f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = symbol,
                                    color = animatedJudgeBorder,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = label,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                    }
                }
            }
        }

        else -> {
            if (isTabletGridOptions && currentQ.options.size >= 4) {
                val rows = currentQ.options.mapIndexed { i, t -> i to t }.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    rows.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { (idx, optText) ->
                                val code = ('A' + idx).toString()
                                QuizOptionCard(
                                    modifier = Modifier.weight(1f),
                                    code = code,
                                    optText = optText,
                                    isSelected = code in userSelected,
                                    isExcluded = code in excludedOptions,
                                    isSubmitted = isSubmitted,
                                    isCorrectAnswer = code in currentQ.answer,
                                    onClick = { onOptionClick(code) },
                                    onToggleExclude = { onToggleExclude(code) }
                                )
                            }
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    currentQ.options.forEachIndexed { idx, optText ->
                        val code = ('A' + idx).toString()
                        QuizOptionCard(
                            modifier = Modifier.fillMaxWidth(),
                            code = code,
                            optText = optText,
                            isSelected = code in userSelected,
                            isExcluded = code in excludedOptions,
                            isSubmitted = isSubmitted,
                            isCorrectAnswer = code in currentQ.answer,
                            onClick = { onOptionClick(code) },
                            onToggleExclude = { onToggleExclude(code) }
                        )
                    }
                }
            }
        }
    }

    if (session.showScratchpad || currentQ.type == "calc") {
        InteractiveScratchpadCard(
            questionId = currentQ.id,
            strokes = session.scratchpadStrokes[currentQ.id] ?: emptyList(),
            onAddStroke = { viewModel.addScratchpadStroke(currentQ.id, it) },
            onUndo = { viewModel.undoScratchpadStroke(currentQ.id) },
            onClear = { viewModel.clearScratchpad(currentQ.id) }
        )
    }

    if (isSubmitted) {
        val isCorrect = viewModel.evaluateQuestionCorrectness(currentQ, userSelected)
        val generatedVariant = session.generatedVariantMap[currentQ.id]

        SereneCard(
            cornerRadius = 24.dp,
            borderColor = if (isCorrect) colors.mintText.copy(alpha = 0.4f) else colors.dangerText.copy(alpha = 0.4f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isCorrect) colors.mintBg else colors.dangerBg)
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Filled.CheckCircle else Icons.Outlined.Cancel,
                        contentDescription = null,
                        tint = if (isCorrect) colors.mintText else colors.dangerText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isCorrect) "回答正确" else "回答错误",
                            color = if (isCorrect) colors.mintText else colors.dangerText,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "正确答案：${currentQ.answer.joinToString(", ")} · 已选：${userSelected.joinToString(", ").ifBlank { "未作答" }}",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                SereneBadge(
                    text = "熟练度 ${currentQ.masteryPercent}%",
                    containerColor = colors.cardSurface,
                    contentColor = colors.primaryAccent
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Psychology,
                        contentDescription = null,
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "为什么选这个 & 怎么记最快",
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
                TextButton(onClick = { showNoteEditor = !showNoteEditor }) {
                    Icon(Icons.Outlined.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (showNoteEditor) "收起笔记" else "记条自己的心得",
                        maxLines = 1,
                        softWrap = false,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = currentQ.explanation.ifBlank { "记住这个核心概念，下次遇到就能稳拿分。" },
                color = colors.textPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 23.sp)
            )

            AnimatedVisibility(visible = showNoteEditor) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        label = { Text("写一句你自己最好懂的笔记或口诀", maxLines = 1) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.saveQuestionNote(currentQ.id, noteInput)
                                showNoteEditor = false
                            },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("存进这道题里", maxLines = 1, softWrap = false, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (currentQ.trapAnalysis.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.subtleSurface)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "最容易踩的坑",
                            color = colors.primaryAccent,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQ.trapAnalysis,
                            color = colors.textSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (currentQ.mnemonic.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.primarySoft.copy(alpha = 0.55f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "一句话巧记",
                            color = colors.primaryAccent,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQ.mnemonic,
                            color = colors.textPrimary,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { viewModel.generateAiVariantQuestion(currentQ) },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("再出一道类似的题练练手", fontSize = 12.sp, maxLines = 1, softWrap = false)
                }
            }

            if (generatedVariant != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.primarySoft.copy(alpha = 0.45f))
                        .border(1.dp, colors.primaryAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = generatedVariant.stem,
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    generatedVariant.options.forEach { opt ->
                        Text(
                            text = opt,
                            color = colors.textSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "答案：A  ·  ${generatedVariant.explanation}",
                        color = colors.primaryAccent,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizOptionCard(
    modifier: Modifier = Modifier,
    code: String,
    optText: String,
    isSelected: Boolean,
    isExcluded: Boolean,
    isSubmitted: Boolean,
    isCorrectAnswer: Boolean,
    onClick: () -> Unit,
    onToggleExclude: () -> Unit
) {
    val colors = SereneTheme.colors
    val cleanText = optText.replace(Regex("^[A-F][.、)\\s]+"), "").trim()

    val targetBg = when {
        isSubmitted && isCorrectAnswer -> colors.mintBg
        isSubmitted && isSelected && !isCorrectAnswer -> colors.dangerBg
        isSelected -> colors.primarySoft.copy(alpha = 0.75f)
        else -> colors.cardSurface
    }
    val targetBorder = when {
        isSubmitted && isCorrectAnswer -> colors.mintText
        isSubmitted && isSelected && !isCorrectAnswer -> colors.dangerText
        isSelected -> colors.primaryAccent
        else -> colors.borderSubtle
    }
    val targetBadgeBg = when {
        isSubmitted && isCorrectAnswer -> colors.mintText
        isSubmitted && isSelected && !isCorrectAnswer -> colors.dangerText
        isSelected -> colors.primaryAccent
        else -> colors.subtleSurface
    }

    val animatedBg by animateColorAsState(targetValue = targetBg, animationSpec = tween(180), label = "opt_bg")
    val animatedBorder by animateColorAsState(targetValue = targetBorder, animationSpec = tween(180), label = "opt_border")
    val animatedBadgeBg by animateColorAsState(targetValue = targetBadgeBg, animationSpec = tween(180), label = "badge_bg")
    val badgeFg = if (isSelected || (isSubmitted && isCorrectAnswer)) Color.White else colors.textPrimary

    Surface(
        modifier = modifier
            .alpha(if (isExcluded && !isSelected && !isSubmitted) 0.48f else 1f)
            .clip(RoundedCornerShape(20.dp))
            .bounceClick(scaleDown = 0.97f, onClick = onClick)
            .testTag("quiz_option_$code"),
        shape = RoundedCornerShape(20.dp),
        color = animatedBg,
        border = androidx.compose.foundation.BorderStroke(if (isSelected || (isSubmitted && isCorrectAnswer)) 2.dp else 1.dp, animatedBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(animatedBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = code,
                    color = badgeFg,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = cleanText,
                color = colors.textPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected || (isSubmitted && isCorrectAnswer)) FontWeight.Bold else FontWeight.Normal,
                    textDecoration = if (isExcluded && !isSelected && !isSubmitted) TextDecoration.LineThrough else TextDecoration.None
                ),
                modifier = Modifier.weight(1f)
            )
            if (!isSubmitted) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(colors.subtleSurface)
                        .clickable { onToggleExclude() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isExcluded) "恢复" else "排除",
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractiveScratchpadCard(
    questionId: String,
    strokes: List<ScratchpadStroke>,
    onAddStroke: (ScratchpadStroke) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit
) {
    val colors = SereneTheme.colors
    var selectedColorHex by remember { mutableLongStateOf(0xFF4950AD) }
    val currentPoints = remember { mutableStateListOf<Offset>() }

    SereneCard(
        cornerRadius = 22.dp,
        borderColor = colors.primaryAccent.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Draw,
                    contentDescription = null,
                    tint = colors.primaryAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "随手打草稿",
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0xFF4950AD, 0xFF216C4B, 0xFFC23934).forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(hex))
                            .border(
                                width = if (selectedColorHex == hex) 2.5.dp else 0.dp,
                                color = colors.textPrimary,
                                shape = CircleShape
                            )
                            .clickable { selectedColorHex = hex }
                    )
                }
                IconButton(onClick = onUndo, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Outlined.Undo, contentDescription = "撤销一笔", modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onClear, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Outlined.DeleteSweep, contentDescription = "擦干净", modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.subtleSurface.copy(alpha = 0.65f))
                .border(1.dp, colors.borderStrong, RoundedCornerShape(16.dp))
                .pointerInput(questionId, selectedColorHex) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPoints.clear()
                            currentPoints.add(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentPoints.add(change.position)
                        },
                        onDragEnd = {
                            if (currentPoints.size > 1) {
                                onAddStroke(
                                    ScratchpadStroke(
                                        points = currentPoints.toList(),
                                        colorHex = selectedColorHex
                                    )
                                )
                            }
                            currentPoints.clear()
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 28.dp.toPx()
                var x = step
                while (x < size.width) {
                    var y = step
                    while (y < size.height) {
                        drawCircle(
                            color = colors.textMuted.copy(alpha = 0.25f),
                            radius = 1.3f,
                            center = Offset(x, y)
                        )
                        y += step
                    }
                    x += step
                }

                strokes.forEach { stroke ->
                    if (stroke.points.size > 1) {
                        val path = Path().apply {
                            moveTo(stroke.points.first().x, stroke.points.first().y)
                            stroke.points.drop(1).forEach { pt -> lineTo(pt.x, pt.y) }
                        }
                        drawPath(
                            path = path,
                            color = Color(stroke.colorHex),
                            style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                }

                if (currentPoints.size > 1) {
                    val activePath = Path().apply {
                        moveTo(currentPoints.first().x, currentPoints.first().y)
                        currentPoints.drop(1).forEach { pt -> lineTo(pt.x, pt.y) }
                    }
                    drawPath(
                        path = activePath,
                        color = Color(selectedColorHex),
                        style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultScreen(
    viewModel: StudyViewModel,
    session: ActiveQuizSession,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    val total = session.questions.size.coerceAtLeast(1)
    val correct = session.questions.count { q ->
        val uAns = session.userSelections[q.id] ?: emptyList()
        viewModel.evaluateQuestionCorrectness(q, uAns)
    }
    val wrong = total - correct
    val scorePct = ((correct * 100.0) / total).toInt()

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "练习结果",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = if (isTablet) 32.dp else 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SereneCard(
                    cornerRadius = 28.dp,
                    contentPadding = PaddingValues(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (scorePct >= 80) {
                            MilestoneMedalIllustration(size = 96.dp)
                        } else {
                            CompletionVectorIllustration(size = 96.dp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "$scorePct%",
                            color = colors.primaryAccent,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = if (scorePct >= 80) "表现优异 · 已记录至今日学情" else "已保存错题至重点巩固本",
                            color = colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("正确", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "$correct 题",
                                    color = colors.mintText,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("错误", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "$wrong 题",
                                    color = if (wrong > 0) colors.dangerText else colors.textMuted,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("耗时", color = colors.textSecondary, maxLines = 1, softWrap = false, style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "${session.elapsedSeconds.coerceAtLeast(18)} 秒",
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.RECAP) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("result_open_recap_btn"),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                        ) {
                            Text("查看答题复盘", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { viewModel.startQuizFromConfig() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("再来一组", maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecapScreen(
    viewModel: StudyViewModel,
    session: ActiveQuizSession,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "回头看解析",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(session.questions, key = { _, q -> q.id }) { idx, q ->
                val userAns = session.userSelections[q.id] ?: emptyList()
                val isCorrect = viewModel.evaluateQuestionCorrectness(q, userAns)
                SereneCard(cornerRadius = 22.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "#${(idx + 1).toString().padStart(2, '0')}",
                                color = colors.primaryAccent,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                softWrap = false
                            )
                            SereneBadge(
                                text = q.typeLabel,
                                containerColor = colors.primarySoft,
                                contentColor = colors.primaryAccent
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!isCorrect) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(colors.mintBg)
                                        .clickable { viewModel.markQuestionMastered(q.id) }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "✓ 看懂了",
                                        color = colors.mintText,
                                        maxLines = 1,
                                        softWrap = false,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                            SereneBadge(
                                text = if (isCorrect) "✓ 做对了" else "✕ 做错了",
                                containerColor = if (isCorrect) colors.mintBg else colors.dangerBg,
                                contentColor = if (isCorrect) colors.mintText else colors.dangerText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = q.stem,
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "你选了：${userAns.joinToString(", ").ifBlank { "没填" }}   ·   正确答案：${q.answer.joinToString(", ")}",
                        color = if (isCorrect) colors.mintText else colors.dangerText,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = q.explanation,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    viewModel: StudyViewModel,
    records: List<PracticeRecordEntity>,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "做题记录",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(records, key = { it.id }) { rec ->
                SereneCard(
                    cornerRadius = 22.dp,
                    onClick = { viewModel.openPracticeConfig(rec.bankName, presetMode = rec.modeLabel) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SereneBadge(
                                    text = rec.modeLabel,
                                    containerColor = colors.primarySoft,
                                    contentColor = colors.primaryAccent
                                )
                                Text(
                                    text = rec.dateLabel,
                                    color = colors.textMuted,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = rec.title,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "做了 ${rec.totalCount} 题 · 对了 ${rec.correctCount} 题",
                                color = colors.textSecondary,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${rec.scorePercent}%",
                                color = if (rec.scorePercent >= 80) colors.mintText else colors.amberText,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                text = "再刷一遍 >",
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
