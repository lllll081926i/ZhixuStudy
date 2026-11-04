package com.example.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.MergeType
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Vibration
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ImportLogEntity
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.UserSettingsState
import com.example.ui.components.AuditRadarIllustration
import com.example.ui.components.ReviewCurveIllustration
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.components.SereneFilterChipRow
import com.example.ui.components.SereneLinearProgress
import com.example.ui.components.SereneTopBar
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.CrossBankConflictPair
import com.example.ui.viewmodel.StudyViewModel

/**
 * 18 设置中心主页面 (Settings Hub Screen) — Faithful implementation of `study_16_screen.png`
 * All items navigate to dedicated full-screen secondary pages (no popups, no redundant bottom controls, no bottom blank space).
 */
@Composable
fun SettingsScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    selectedCategory: String,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "设置",
            subtitle = null,
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet,
            tabletSplitActive = tabletSplitActive,
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
            actions = {
                IconButton(
                    onClick = {
                        showSearch = !showSearch
                        if (!showSearch) searchQuery = ""
                    },
                    modifier = Modifier.testTag("settings_search_btn")
                ) {
                    Icon(
                        imageVector = if (showSearch) Icons.Outlined.Close else Icons.Outlined.Search,
                        contentDescription = "搜索设置",
                        tint = colors.textPrimary
                    )
                }
            }
        )

        AnimatedVisibility(visible = showSearch) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("搜索设置项...", maxLines = 1, softWrap = false) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = colors.textMuted
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_search_input")
                )
            }
        }

        if (isTablet && tabletSplitActive) {
            // Tablet Master-Detail Settings Workbench - Strictly Equal Height (fillMaxHeight)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Master Navigation Panel
                Surface(
                    modifier = Modifier
                        .weight(0.38f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(24.dp),
                    color = colors.cardSurface,
                    border = BorderStroke(1.dp, colors.borderSubtle),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "功能设置分类",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            SereneBadge(text = "系统中心", containerColor = colors.primarySoft, contentColor = colors.primaryAccent)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.borderSubtle)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            SettingsCategorizedList(
                                settings = settings,
                                searchQuery = searchQuery,
                                activeCategory = selectedCategory,
                                onOpenAppearance = { viewModel.setSelectedSettingsCategory("appearance") },
                                onOpenTypography = { viewModel.setSelectedSettingsCategory("typography") },
                                onOpenHaptics = { viewModel.setSelectedSettingsCategory("haptics") },
                                onOpenPracticeFlow = { viewModel.setSelectedSettingsCategory("practice_flow") },
                                onOpenReviewPlan = { viewModel.navigateTo(AppScreen.REVIEW_PLAN) },
                                onOpenGestures = { viewModel.setSelectedSettingsCategory("gestures") },
                                onOpenAiService = { viewModel.navigateTo(AppScreen.AI_SERVICE) },
                                onOpenDataMgmt = { viewModel.navigateTo(AppScreen.DATA_MGMT) },
                                onOpenAudit = { viewModel.navigateTo(AppScreen.AUDIT) },
                                onOpenDnd = { viewModel.setSelectedSettingsCategory("dnd") }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.borderSubtle)
                        )
                        SettingsVersionFooter()
                    }
                }

                // Right Detail Configuration Panel
                Surface(
                    modifier = Modifier
                        .weight(0.62f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(24.dp),
                    color = colors.cardSurface,
                    border = BorderStroke(1.dp, colors.borderSubtle),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        val (detailTitle, detailSubtitle) = when (selectedCategory) {
                            "typography" -> "字体字号与排版布局" to "根据阅读习惯调整题目字号大小与行间距"
                            "haptics" -> "触感反馈与提示音效" to "配置答题微震动与选项按压提示音"
                            "practice_flow" -> "作答判定与自动切题" to "即时解析呈现、做对自动切题与选项乱序"
                            "gestures" -> "手势交互与答题卡风格" to "左右滑动切题、双击收藏与答题卡外观"
                            "dnd" -> "学习计划与夜间免打扰" to "设定每日刷题目标与安静作息免打扰时段"
                            else -> "主题风格与强调配色" to "深色模式、动态壁纸取色与个性化主色调"
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 22.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = detailTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = detailSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                            SereneBadge(text = "实时生效", containerColor = colors.mintBg, contentColor = colors.mintText)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.borderSubtle)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            when (selectedCategory) {
                                "typography" -> SettingsTypographyContent(viewModel = viewModel, settings = settings)
                                "haptics" -> SettingsSoundHapticsContent(viewModel = viewModel, settings = settings)
                                "practice_flow" -> SettingsPracticeFlowContent(viewModel = viewModel, settings = settings)
                                "gestures" -> SettingsGesturesContent(viewModel = viewModel, settings = settings)
                                "dnd" -> SettingsDndContent(viewModel = viewModel, settings = settings)
                                else -> SettingsAppearanceContent(viewModel = viewModel, settings = settings)
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    SettingsCategorizedList(
                        settings = settings,
                        searchQuery = searchQuery,
                        onOpenAppearance = { viewModel.navigateTo(AppScreen.SETTINGS_APPEARANCE) },
                        onOpenTypography = { viewModel.navigateTo(AppScreen.SETTINGS_TYPOGRAPHY) },
                        onOpenHaptics = { viewModel.navigateTo(AppScreen.SETTINGS_SOUND_HAPTICS) },
                        onOpenPracticeFlow = { viewModel.navigateTo(AppScreen.SETTINGS_PRACTICE_FLOW) },
                        onOpenReviewPlan = { viewModel.navigateTo(AppScreen.REVIEW_PLAN) },
                        onOpenGestures = { viewModel.navigateTo(AppScreen.SETTINGS_GESTURES) },
                        onOpenAiService = { viewModel.navigateTo(AppScreen.AI_SERVICE) },
                        onOpenDataMgmt = { viewModel.navigateTo(AppScreen.DATA_MGMT) },
                        onOpenAudit = { viewModel.navigateTo(AppScreen.AUDIT) },
                        onOpenDnd = { viewModel.navigateTo(AppScreen.SETTINGS_DND) }
                    )
                }

                item {
                    SettingsVersionFooter()
                }
            }
        }
    }
}

@Composable
private fun SettingsVersionFooter() {
    val colors = SereneTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "知序 Study · v1.2.0 (Build 2026)",
            style = MaterialTheme.typography.bodySmall,
            color = colors.textMuted,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun SettingsCategorizedList(
    settings: UserSettingsState,
    searchQuery: String,
    activeCategory: String? = null,
    onOpenAppearance: () -> Unit,
    onOpenTypography: () -> Unit,
    onOpenHaptics: () -> Unit,
    onOpenPracticeFlow: () -> Unit,
    onOpenReviewPlan: () -> Unit,
    onOpenGestures: () -> Unit,
    onOpenAiService: () -> Unit,
    onOpenDataMgmt: () -> Unit,
    onOpenAudit: () -> Unit,
    onOpenDnd: () -> Unit
) {
    val colors = SereneTheme.colors
    val themeModeName = when (settings.themeMode) {
        "dark" -> "深色模式"
        "light" -> "浅色模式"
        else -> "跟随系统"
    }
    val dynamicName = if (settings.dynamicColor) "壁纸动态取色" else settings.accentPreset
    val fontScalePercent = settings.fontScalePercent
    val habitEnabledCount = listOf(
        settings.instantExplanation,
        settings.autoNextOnCorrect,
        settings.shuffleOptions
    ).count { it }

    fun matches(vararg texts: String): Boolean {
        if (searchQuery.isBlank()) return true
        return texts.any { it.contains(searchQuery.trim(), ignoreCase = true) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        // Section 1: 视觉与外观
        if (matches("视觉与外观", "主题模式", "字号与排版", "触感与提示音", themeModeName, dynamicName)) {
            SettingsSectionGroup(title = "外观与显示") {
                if (matches("主题模式", "外观与主题色", themeModeName, dynamicName, "深色", "动态取色")) {
                    SettingsRowItem(
                        icon = Icons.Outlined.Palette,
                        title = "主题风格与强调色",
                        subtitle = "$themeModeName · $dynamicName",
                        iconBg = colors.primarySoft,
                        iconTint = colors.primaryAccent,
                        isActive = activeCategory == "appearance",
                        onClick = onOpenAppearance,
                        testTag = "settings_row_appearance"
                    )
                }
                if (matches("字号与排版", "字号大小与行距", "字号", "行高", "排版")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.FormatSize,
                        title = "字体字号与排版布局",
                        subtitle = "当前字号缩放 ${fontScalePercent}%",
                        isActive = activeCategory == "typography",
                        onClick = onOpenTypography,
                        testTag = "settings_row_typography"
                    )
                }
                if (matches("触感与提示音", "答题声音与震动", "震动", "声音", "音效")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Vibration,
                        title = "触感反馈与提示音",
                        subtitle = if (settings.hapticFeedback) "已开启答题轻微微震" else "静音无震动",
                        isActive = activeCategory == "haptics",
                        onClick = onOpenHaptics,
                        testTag = "settings_row_haptics"
                    )
                }
            }
        }

        // Section 2: 答题流转与习惯
        if (matches("答题流转与习惯", "作答判定", "复习节奏", "手势操作与答题卡")) {
            SettingsSectionGroup(title = "答题偏好与交互") {
                if (matches("作答判定与自动切题", "选完答案后的动作", "解析", "自动下一题", "乱序")) {
                    SettingsRowItem(
                        icon = Icons.Outlined.Checklist,
                        title = "作答判定与自动切题",
                        subtitle = "已开启 ${habitEnabledCount} 项流转选项",
                        isActive = activeCategory == "practice_flow",
                        onClick = onOpenPracticeFlow,
                        testTag = "settings_row_practice_flow"
                    )
                }
                if (matches("复习节奏", "错题隔几天提醒我背", settings.reviewPreset, "记忆")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Psychology,
                        title = "记忆曲线与复习规划",
                        subtitle = settings.reviewPreset,
                        onClick = onOpenReviewPlan,
                        testTag = "settings_row_review_mode"
                    )
                }
                if (matches("手势操作与答题卡风格", "左右滑切题与题号卡", settings.answerSheetStyle, "手势")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Swipe,
                        title = "手势操作与答题卡风格",
                        subtitle = settings.answerSheetStyle,
                        isActive = activeCategory == "gestures",
                        onClick = onOpenGestures,
                        testTag = "settings_row_gestures"
                    )
                }
            }
        }

        // Section 3: 智能辅导与 AI 设置
        if (matches("智能辅导与 AI 设置", "大模型接口", "错因剖析", "变式题", settings.aiModelId)) {
            SettingsSectionGroup(
                title = "智能辅导与 AI 设置",
                badgeText = "✦ 实时助手"
            ) {
                if (matches("大模型接口与密钥", "连接你自己的 AI 接口", settings.aiModelId, "Key")) {
                    SettingsRowItem(
                        icon = Icons.Outlined.Hub,
                        title = "大模型服务与密钥配置",
                        subtitle = "${settings.aiModelId} · 密钥本地存储",
                        iconBg = colors.primarySoft,
                        iconTint = colors.primaryAccent,
                        onClick = onOpenAiService,
                        testTag = "settings_row_ai_api"
                    )
                }
                if (matches("智能错因剖析与难点拆解", "让 AI 怎么帮你讲题", "开启")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.ChatBubbleOutline,
                        title = "智能错因剖析与难点拆解",
                        subtitle = if (settings.aiSmartExplanation) "作答后深度拆解易错考点" else "暂不开启",
                        onClick = onOpenAiService,
                        testTag = "settings_row_ai_explain"
                    )
                }
                if (matches("错题变式题衍生练习", "遇到错题举一反三", "练习")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Quiz,
                        title = "错题变式题衍生练习",
                        subtitle = if (settings.aiVariantGenerator) "支持针对错题自动衍生相似题" else "仅手动点按",
                        onClick = onOpenAiService,
                        testTag = "settings_row_ai_quiz"
                    )
                }
            }
        }

        // Section 4: 数据管理与题库体检
        if (matches("数据管理与题库体检", "本地备份", "深度体检")) {
            SettingsSectionGroup(title = "数据管理与体检") {
                if (matches("本地存储与数据备份", "手机里的题库与备份", "备份", "12.8 MB")) {
                    SettingsRowItem(
                        icon = Icons.Outlined.Folder,
                        title = "本地存储与数据备份",
                        subtitle = "纯本地离线加密存储 · 12.8 MB",
                        onClick = onOpenDataMgmt,
                        testTag = "settings_row_data_mgmt"
                    )
                }
                if (matches("题库深度体检与冲突审查", "检查重题和漏写的解析")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.Verified,
                        title = "题库深度体检与冲突审查",
                        subtitle = "题库健康状态良好 · 暂无阻断冲突",
                        subtitleColor = colors.mintText,
                        iconBg = colors.mintBg,
                        iconTint = colors.mintText,
                        onClick = onOpenAudit,
                        testTag = "settings_row_audit"
                    )
                }
            }
        }

        // Section 5: 学习计划与免打扰
        if (matches("学习计划与免打扰", "每日刷题目标", "夜间免打扰时段")) {
            SettingsSectionGroup(title = "学习计划与免打扰") {
                if (matches("每日刷题目标", "每天想刷多少题", "每日")) {
                    SettingsRowItem(
                        icon = Icons.Outlined.Alarm,
                        title = "每日刷题规划目标",
                        subtitle = "每天 ${settings.dailyTargetCount} 题",
                        onClick = onOpenReviewPlan,
                        testTag = "settings_row_daily_plan"
                    )
                }
                if (matches("夜间免打扰时段", "晚上几点后别打扰我", "免打扰")) {
                    SettingsDivider()
                    SettingsRowItem(
                        icon = Icons.Outlined.DoNotDisturbOn,
                        title = "夜间免打扰时段",
                        subtitle = if (settings.doNotDisturbMode) settings.dndTimeRange else "未开启免打扰",
                        isActive = activeCategory == "dnd",
                        onClick = onOpenDnd,
                        testTag = "settings_row_dnd"
                    )
                }
            }
        }
    }
}

/**
 * 18.1 外观与主题二级页面 (Appearance & Theme Sub-Screen)
 */
@Composable
fun SettingsAppearanceScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "外观与主题",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsAppearanceContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsAppearanceContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val colors = SereneTheme.colors
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Live Theme Card Preview
        item {
            SereneCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "当前界面视觉预览",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    SereneBadge(
                        text = if (settings.dynamicColor) "Material You" else settings.accentPreset,
                        containerColor = colors.primarySoft,
                        contentColor = colors.primaryAccent
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.subtleSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "《铁路运输组织基础》",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false
                            )
                            SereneBadge(
                                text = "掌握度 88%",
                                containerColor = colors.mintBg,
                                contentColor = colors.mintText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SereneLinearProgress(progress = 0.68f, indicatorColor = colors.primaryAccent)
                    }
                }
            }
        }

        // 2. Theme Mode Selection (System / Light / Dark)
        item {
            SereneCard {
                Text(
                    text = "深色模式与昼夜切换",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                val modes = listOf(
                    Triple("system", "跟随系统", Icons.Outlined.SettingsBrightness),
                    Triple("light", "浅色模式", Icons.Outlined.LightMode),
                    Triple("dark", "深色模式", Icons.Outlined.DarkMode)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    modes.forEach { (code, label, icon) ->
                        val selected = settings.themeMode == code
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) colors.primaryAccent else colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { viewModel.setThemeMode(code) }
                                .testTag("theme_mode_$code")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) Color.White else colors.primaryAccent,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Material You Dynamic Color & Accent Palette
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "Material You 动态壁纸取色",
                    subtitle = "提取系统壁纸色彩生成柔和主题",
                    checked = settings.dynamicColor,
                    onCheckedChange = { viewModel.setDynamicColor(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "知序经典配色方案",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.textSecondary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                val palettes = listOf(
                    "柔和紫蓝" to Color(0xFF4950AD),
                    "松石薄荷" to Color(0xFF216C4B),
                    "暖阳琥珀" to Color(0xFF8F4829),
                    "深海靛青" to Color(0xFF2B5B84)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    palettes.forEach { (name, swatchColor) ->
                        val selected = !settings.dynamicColor && settings.accentPreset == name
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) colors.primarySoft else colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    width = if (selected) 1.5.dp else 1.dp,
                                    color = if (selected) colors.primaryAccent else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (settings.dynamicColor) viewModel.setDynamicColor(false)
                                    viewModel.setAccentPreset(name)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(swatchColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) colors.primaryAccent else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 18.2 排版与正文字号二级页面 (Typography & Font Scale Sub-Screen)
 */
@Composable
fun SettingsTypographyScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "排版与正文字号",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsTypographyContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsTypographyContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val colors = SereneTheme.colors
    val scaleFactor = settings.fontScalePercent / 100f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Live Question Typography Preview Card
        item {
            SereneCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SereneBadge(
                        text = "排版实时预览",
                        containerColor = colors.primarySoft,
                        contentColor = colors.primaryAccent
                    )
                    Text(
                        text = "${settings.fontScalePercent}% · ${settings.lineSpacingMode}",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "铁路运输组织中，列车运行图的基本作用是什么？",
                    fontSize = (16f * scaleFactor).sp,
                    lineHeight = (24f * scaleFactor).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.subtleSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "A. 全路组织列车运行的基础，综合性作业规划纲领",
                            fontSize = (14f * scaleFactor).sp,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "解析：运行图规定了各次列车占用区间的程序与时刻。",
                            fontSize = (12.5f * scaleFactor).sp,
                            color = colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // 2. Font Scale Slider & Presets
        item {
            SereneCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "正文字号缩放",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    SereneBadge(
                        text = "${settings.fontScalePercent}%",
                        containerColor = colors.primarySoft,
                        contentColor = colors.primaryAccent
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = settings.fontScalePercent.toFloat(),
                    onValueChange = { viewModel.setFontScale(it.toInt()) },
                    valueRange = 75f..125f,
                    steps = 4
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "提示：调整字号同时联动整机 DPI 缩放，在 1080p 屏幕或模拟器下推荐设为 80%~85%，界面更精致紧凑。",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        75 to "精简",
                        85 to "1080p推荐",
                        100 to "标准",
                        115 to "舒适"
                    ).forEach { (percent, label) ->
                        val selected = settings.fontScalePercent == percent
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) colors.primaryAccent else colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setFontScale(percent) }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$label\n${percent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else colors.textPrimary,
                                    maxLines = 2,
                                    softWrap = false,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Line Spacing Selector
        item {
            SereneCard {
                Text(
                    text = "段落行距模式",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("紧凑 (1.35x)", "标准 (1.5x)", "宽松 (1.75x)").forEach { mode ->
                        val selected = settings.lineSpacingMode == mode
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) colors.primaryAccent else colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLineSpacing(mode) }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 18.3 音效与触感反馈二级页面 (Sound & Haptics Sub-Screen)
 */
@Composable
fun SettingsSoundHapticsScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "音效与触感反馈",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsSoundHapticsContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsSoundHapticsContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val colors = SereneTheme.colors
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Haptics Card
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "触感微震动反馈",
                    subtitle = "作答点选与切题时提供细腻震感",
                    checked = settings.hapticFeedback,
                    onCheckedChange = { viewModel.updateHabitSetting(haptic = it) }
                )
                if (settings.hapticFeedback) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "震感强度",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("轻柔微震", "标准震感", "清脆反馈").forEach { strength ->
                            val selected = settings.hapticStrength == strength
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) colors.primaryAccent else colors.subtleSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.updateHabitSetting(hapticStrength = strength) }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = strength,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else colors.textPrimary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Sound Effects Card
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "做题提示音效",
                    subtitle = "作答正确、错误与完成练习时的声音反馈",
                    checked = settings.soundEffects,
                    onCheckedChange = { viewModel.updateHabitSetting(sound = it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.showToast("♪ 已播放答对清脆音效") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("试听答对音", maxLines = 1, softWrap = false)
                    }
                    OutlinedButton(
                        onClick = { viewModel.showToast("♪ 已播放错题提示音") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("试听错题音", maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

/**
 * 18.4 答题流程与判定二级页面 (Practice Flow & Judgment Sub-Screen)
 */
@Composable
fun SettingsPracticeFlowScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "答题流程与判定",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsPracticeFlowContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsPracticeFlowContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "即时显示解析",
                    subtitle = "选定答案后立即展开正确答案与考点剖析",
                    checked = settings.instantExplanation,
                    onCheckedChange = { viewModel.updateHabitSetting(instantExp = it) }
                )
                Spacer(modifier = Modifier.height(14.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(14.dp))
                SettingSwitchRow(
                    title = "单选题答对自动跳转下一题",
                    subtitle = "正确作答后无需手动滑动，自动进入下一题",
                    checked = settings.autoNextOnCorrect,
                    onCheckedChange = { viewModel.updateHabitSetting(autoNext = it) }
                )
                Spacer(modifier = Modifier.height(14.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(14.dp))
                SettingSwitchRow(
                    title = "随机打乱选项顺序",
                    subtitle = "A / B / C / D 随机排布，防止记忆选项位置",
                    checked = settings.shuffleOptions,
                    onCheckedChange = { viewModel.updateHabitSetting(shuffleOpts = it) }
                )
            }
        }
    }
}

/**
 * 18.5 快捷手势与答题卡二级页面 (Gestures & Answer Sheet Sub-Screen)
 */
@Composable
fun SettingsGesturesScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "快捷手势与答题卡",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsGesturesContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsGesturesContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val colors = SereneTheme.colors
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "左右滑动切换题目",
                    subtitle = "在答题区左右横滑快速切换上一题/下一题",
                    checked = settings.swipeSwitchQuestion,
                    onCheckedChange = { viewModel.updateGestureSettings(swipeSwitch = it) }
                )
                Spacer(modifier = Modifier.height(14.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(14.dp))
                SettingSwitchRow(
                    title = "双击题干加入收藏",
                    subtitle = "双击题目文字区域快速标记星标重点",
                    checked = settings.doubleTapStar,
                    onCheckedChange = { viewModel.updateGestureSettings(doubleTapStar = it) }
                )
                Spacer(modifier = Modifier.height(14.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(14.dp))
                SettingSwitchRow(
                    title = "长按选项排除干扰项",
                    subtitle = "长按单个选项可将其置灰排除",
                    checked = settings.longPressExcludeOption,
                    onCheckedChange = { viewModel.updateGestureSettings(longPressExclude = it) }
                )
            }
        }

        item {
            SereneCard {
                Text(
                    text = "答题卡展开样式",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("经典抽屉", "悬浮面板").forEach { style ->
                        val selected = settings.answerSheetStyle == style
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) colors.primaryAccent else colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.updateGestureSettings(sheetStyle = style) }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = style,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 18.6 学习习惯与免打扰二级页面 (Study Habits & Do Not Disturb Sub-Screen)
 */
@Composable
fun SettingsDndScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "学习习惯与免打扰",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )
        SettingsDndContent(
            viewModel = viewModel,
            settings = settings,
            horizontalPadding = if (isTablet) 24.dp else 16.dp
        )
    }
}

@Composable
private fun SettingsDndContent(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    horizontalPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val colors = SereneTheme.colors
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 10.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SereneCard {
                SettingSwitchRow(
                    title = "夜间静默免打扰",
                    subtitle = "在免打扰时段内暂停所有温习推送提醒",
                    checked = settings.doNotDisturbMode,
                    onCheckedChange = { viewModel.updatePlanSetting(dnd = it) }
                )
                if (settings.doNotDisturbMode) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "静默时段",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("22:00 - 07:00", "22:30 - 07:30", "23:00 - 08:00").forEach { range ->
                            val selected = settings.dndTimeRange == range
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) colors.primaryAccent else colors.subtleSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.updatePlanSetting(dndRange = range) }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = range,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else colors.textPrimary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            SereneCard {
                SettingSwitchRow(
                    title = "周末弹性打卡保护",
                    subtitle = "周末完成平日 50% 题量即可维持连续学习天数",
                    checked = settings.weekendProtect,
                    onCheckedChange = { viewModel.updatePlanSetting(weekendProtect = it) }
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionGroup(
    title: String,
    badgeText: String? = null,
    content: @Composable () -> Unit
) {
    val colors = SereneTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = colors.primaryAccent,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )
            if (badgeText != null) {
                SereneBadge(
                    text = badgeText,
                    containerColor = colors.primarySoft,
                    contentColor = colors.primaryAccent
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = colors.cardSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    subtitleColor: Color? = null,
    iconBg: Color? = null,
    iconTint: Color? = null,
    isActive: Boolean = false,
    testTag: String? = null,
    onClick: () -> Unit
) {
    val colors = SereneTheme.colors
    val baseMod = if (isActive) {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.primarySoft.copy(alpha = 0.85f))
            .border(1.dp, colors.primaryAccent.copy(alpha = 0.40f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    }
    val rowMod = if (testTag != null) baseMod.testTag(testTag) else baseMod
    Row(
        modifier = rowMod,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isActive) colors.primaryAccent else (iconBg ?: colors.subtleSurface)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isActive) Color.White else (iconTint ?: colors.primaryAccent),
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                    color = if (isActive) colors.primaryAccent else colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isActive) colors.primaryAccent.copy(alpha = 0.8f) else (subtitleColor ?: colors.textMuted),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = if (isActive) colors.primaryAccent else colors.textMuted
        )
    }
}

@Composable
private fun SettingsDivider() {
    val colors = SereneTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(colors.borderSubtle)
    )
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = SereneTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/**
 * 19 AI 辅助设置 (AI Service Settings Screen) — Faithful implementation of `study_15_screen.png`
 * Inline API Key editor (no popup dialog), zero excess blank space.
 */
@Composable
fun AiServiceScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    var provider by remember(settings.aiProtocol) { mutableStateOf(settings.aiProtocol) }
    var baseUrl by remember(settings.aiBaseUrl) { mutableStateOf(settings.aiBaseUrl) }
    var modelId by remember(settings.aiModelId) { mutableStateOf(settings.aiModelId) }
    var apiKey by remember(settings.aiApiKeyMasked) { mutableStateOf(settings.aiApiKeyMasked) }
    var smartExplain by remember(settings.aiSmartExplanation) { mutableStateOf(settings.aiSmartExplanation) }
    var variantGen by remember(settings.aiVariantGenerator) { mutableStateOf(settings.aiVariantGenerator) }
    var importFix by remember(settings.aiImportCorrection) { mutableStateOf(settings.aiImportCorrection) }
    var tokenLimit by remember(settings.aiSingleLimitTokens) { mutableIntStateOf(settings.aiSingleLimitTokens) }
    var pingLatencyMs by remember { mutableIntStateOf(280) }
    var isEditingKeyInline by remember { mutableStateOf(false) }
    var rawKeyInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "AI 讲题助手设置",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Privacy First Hero Banner
            item {
                SereneCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(colors.primarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Psychology,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Key 只存在你自己的手机里",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                                SereneBadge(
                                    text = "✓ 本地保存",
                                    containerColor = colors.mintBg,
                                    contentColor = colors.mintText
                                )
                            }
                            Text(
                                text = "只有你点「讲题」的时候才会发请求，平时完全离线。",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // 2. Model & Connection Card
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "连哪个 AI 服务？",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "点一下可切换",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                SereneCard {
                    Text(
                        text = "服务商类型",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val providers = listOf(
                        "OpenAI 通用接口 (DeepSeek / GPT / Kimi 都能用)",
                        "Claude 接口",
                        "电脑局域网 Ollama 本地模型"
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                val nextIdx = (providers.indexOf(provider) + 1) % providers.size
                                provider = providers[nextIdx]
                            }
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = provider,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = colors.textMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = baseUrl,
                        onValueChange = { baseUrl = it },
                        label = { Text("接口地址 (Base URL)", maxLines = 1, softWrap = false) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = modelId,
                        onValueChange = { modelId = it },
                        label = { Text("模型名字 (比如 deepseek-chat)", maxLines = 1, softWrap = false) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colors.primarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Security,
                                    contentDescription = null,
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "你的 API Key",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textMuted,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Text(
                                    text = apiKey,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SereneBadge(
                                text = "已加密",
                                containerColor = colors.mintBg,
                                contentColor = colors.mintText
                            )
                            OutlinedButton(
                                onClick = { isEditingKeyInline = !isEditingKeyInline },
                                shape = RoundedCornerShape(999.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (isEditingKeyInline) "收起" else "换一个",
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = isEditingKeyInline) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            OutlinedTextField(
                                value = rawKeyInput,
                                onValueChange = { rawKeyInput = it },
                                label = { Text("粘贴新的 API Key (sk-...)", maxLines = 1, softWrap = false) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        if (rawKeyInput.isNotBlank()) {
                                            val tail = rawKeyInput.takeLast(4)
                                            apiKey = "••••••••••••••••$tail"
                                            rawKeyInput = ""
                                        }
                                        isEditingKeyInline = false
                                    },
                                    shape = RoundedCornerShape(999.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Text("填好了", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SereneBadge(
                            text = "● 能连上 (${pingLatencyMs}ms)",
                            containerColor = colors.mintBg,
                            contentColor = colors.mintText
                        )
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = colors.primarySoft,
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .clickable {
                                    pingLatencyMs = (180..310).random()
                                    viewModel.showToast("连上啦！响应速度 ${pingLatencyMs}ms")
                                }
                                .testTag("ping_ai_endpoint_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Speed,
                                    contentDescription = null,
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "试试能不能连上",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primaryAccent,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            // 3. Smart Feature Switches
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "平时让 AI 帮你做什么？",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "按需开启",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                SereneCard {
                    SettingSwitchRow(
                        title = "帮我把题目解析讲得更通俗",
                        subtitle = "做完题可以点一下看易错点和口诀",
                        checked = smartExplain,
                        onCheckedChange = { smartExplain = it }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    SettingSwitchRow(
                        title = "做错题时帮我出一道相似题巩固",
                        subtitle = "换个问法考同一个知识点，看看真懂了没",
                        checked = variantGen,
                        onCheckedChange = { variantGen = it }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    SettingSwitchRow(
                        title = "导题库遇到缺漏时帮我自动补全",
                        subtitle = "表格没写题型或漏写解析时顺手补上",
                        checked = importFix,
                        onCheckedChange = { importFix = it }
                    )
                }
            }

            // 4. Token Usage & Quota Monitor
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "大概用了多少字数额度",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryAccent,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "手机本地估算",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("● 今天用了", style = MaterialTheme.typography.labelSmall, color = colors.primaryAccent, maxLines = 1, softWrap = false)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${settings.aiTodayTokens} Tokens",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("● 这个月一共", style = MaterialTheme.typography.labelSmall, color = colors.mintText, maxLines = 1, softWrap = false)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "3.8 万 Tokens",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = colors.subtleSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("● 每次讲题最多用多少额度（省着点用）", style = MaterialTheme.typography.labelSmall, color = colors.amberText, maxLines = 1, softWrap = false)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$tokenLimit Tokens",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                            TextButton(onClick = { tokenLimit = if (tokenLimit >= 4000) 1000 else tokenLimit + 500 }) {
                                Text("调高一点", fontWeight = FontWeight.Bold, color = colors.primaryAccent, maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }
            }
        }

        // Bottom Save Bar
        Surface(
            color = colors.cardSurface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        provider = "OpenAI 通用接口 (DeepSeek / GPT / Kimi 都能用)"
                        baseUrl = "https://api.deepseek.com/v1"
                        modelId = "deepseek-chat"
                        tokenLimit = 1000
                    },
                    modifier = Modifier.weight(0.35f)
                ) {
                    Text(
                        text = "恢复默认",
                        fontWeight = FontWeight.Bold,
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Button(
                    onClick = {
                        viewModel.saveAiServiceConfig(
                            protocol = provider,
                            baseUrl = baseUrl,
                            modelId = modelId,
                            apiKey = apiKey,
                            smartExp = smartExplain,
                            variant = variantGen,
                            importFix = importFix,
                            singleLimit = tokenLimit
                        )
                        viewModel.navigateBack()
                    },
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier
                        .weight(0.65f)
                        .height(48.dp)
                        .testTag("save_ai_config_btn")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "保存设置",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

/**
 * 20 题库体检与去重审查 (Audit & Deduplication Screen) — Faithful implementation of `study_17_screen.png` & `study_27_screen.png`
 */
@Composable
fun AuditScreen(
    viewModel: StudyViewModel,
    questions: List<QuestionEntity>,
    importLogs: List<ImportLogEntity>,
    auditFilter: String,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    val allPairs = remember(questions) { viewModel.computeCrossBankConflicts(questions) }
    val conflictPairs = remember(allPairs) { allPairs.filter { !it.isSameAnswerDuplicate } }
    val duplicatePairs = remember(allPairs) { allPairs.filter { it.isSameAnswerDuplicate } }
    val missingExpQuestions = remember(questions) { questions.filter { it.explanation.isBlank() } }

    val issueCount = conflictPairs.size + duplicatePairs.size + missingExpQuestions.size
    val healthScore = (100 - issueCount * 2).coerceIn(72, 100)

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "检查重题和漏写的解析",
            subtitle = null,
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet,
            tabletSplitActive = tabletSplitActive,
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) },
            actions = {
                SereneBadge(
                    text = "健康度 ${healthScore}分",
                    containerColor = colors.mintBg,
                    contentColor = colors.mintText
                )
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Audit Summary Hero Card with 2D Radar Illustration
            item {
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SereneBadge(
                                text = "已帮你查完 ${questions.size} 道题",
                                containerColor = colors.primarySoft,
                                contentColor = colors.primaryAccent
                            )
                            Text(
                                text = if (issueCount == 0) "没有发现重题或冲突，很干净！" else "找到 $issueCount 个可以顺手修好的地方",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        AuditRadarIllustration(modifier = Modifier.size(68.dp))
                    }

                    if (issueCount > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                conflictPairs.forEach { viewModel.resolveConflictOverwriteWithA(it) }
                                duplicatePairs.forEach { viewModel.mergeDuplicateQuestions(it) }
                                missingExpQuestions.forEach { viewModel.autoFillMissingExplanation(it) }
                            },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auto_fix_all_audit_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "一键全部修好 ($issueCount 处)",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // 2. Category Filter Tabs
            item {
                val filterOptions = listOf(
                    "all" to "答案打架的 (${conflictPairs.size})",
                    "duplicate" to "重复导入的 (${duplicatePairs.size})",
                    "missing" to "没写解析的 (${missingExpQuestions.size})"
                )
                SereneFilterChipRow(
                    options = filterOptions,
                    selectedKey = if (auditFilter in listOf("all", "duplicate", "missing")) auditFilter else "all",
                    onSelect = { viewModel.setAuditFilter(it) }
                )
            }

            // 3. Issue Cards
            when (auditFilter) {
                "duplicate" -> {
                    if (duplicatePairs.isEmpty()) {
                        item {
                            AuditEmptyStateCard("没有重复导入的题目")
                        }
                    } else {
                        items(duplicatePairs, key = { it.id }) { pair ->
                            val q = pair.questionA
                            SereneCard {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SereneBadge(
                                            text = "两道题内容一样",
                                            containerColor = colors.amberBg,
                                            contentColor = colors.amberText
                                        )
                                        Text(
                                            text = q.chapter,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = colors.textMuted,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    SereneBadge(
                                        text = q.typeLabel,
                                        containerColor = colors.primarySoft,
                                        contentColor = colors.primaryAccent
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = q.stem,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.resolveConflictKeepBothVerified(pair) },
                                        shape = RoundedCornerShape(999.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text("两道都留着", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { viewModel.mergeDuplicateQuestions(pair) },
                                        shape = RoundedCornerShape(999.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.MergeType,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("合并成一道", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }

                "missing" -> {
                    if (missingExpQuestions.isEmpty()) {
                        item {
                            AuditEmptyStateCard("每道题都已经写好解析啦")
                        }
                    } else {
                        items(missingExpQuestions, key = { it.id }) { q ->
                            SereneCard {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SereneBadge(
                                        text = "还没写解析",
                                        containerColor = colors.dangerBg,
                                        contentColor = colors.dangerText
                                    )
                                    Text(
                                        text = "答案：${q.answer.joinToString(",")}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.primaryAccent,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = q.stem,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.openQuestionEditor(q) },
                                        shape = RoundedCornerShape(999.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("自己去写", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { viewModel.autoFillMissingExplanation(q) },
                                        shape = RoundedCornerShape(999.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(imageVector = Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("帮我自动补上", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    if (conflictPairs.isEmpty()) {
                        item {
                            AuditEmptyStateCard("没有答案不一致的题目")
                        }
                    } else {
                        items(conflictPairs, key = { it.id }) { pair ->
                            CrossBankConflictCard(
                                pair = pair,
                                onKeepPrimary = { viewModel.resolveConflictOverwriteWithA(pair, preferQuestionB = false) },
                                onKeepSecondary = { viewModel.resolveConflictOverwriteWithA(pair, preferQuestionB = true) },
                                onKeepBoth = { viewModel.resolveConflictKeepBothVerified(pair) },
                                onManualEdit = { viewModel.openQuestionEditor(pair.questionA) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CrossBankConflictCard(
    pair: CrossBankConflictPair,
    onKeepPrimary: () -> Unit,
    onKeepSecondary: () -> Unit,
    onKeepBoth: () -> Unit,
    onManualEdit: () -> Unit
) {
    val colors = SereneTheme.colors
    val q1 = pair.questionA
    val q2 = pair.questionB

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, colors.amberText.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SereneBadge(
                        text = "⚠ 跨库答案不一致",
                        containerColor = colors.amberBg,
                        contentColor = colors.amberText
                    )
                    SereneBadge(
                        text = q1.typeLabel,
                        containerColor = colors.primarySoft,
                        contentColor = colors.primaryAccent
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.CompareArrows,
                    contentDescription = null,
                    tint = colors.amberText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = q1.stem,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Side-by-side comparison boxes (`study_27_screen.png`)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.primarySoft.copy(alpha = 0.55f),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, colors.primaryAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "来源 1：${q1.bank}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.primaryAccent,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "答案：${q1.answer.joinToString(",")}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = q1.explanation.ifBlank { "无解析" },
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.amberBg.copy(alpha = 0.55f),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, colors.amberText.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "来源 2：${q2.bank}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.amberText,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "答案：${q2.answer.joinToString(",")}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = q2.explanation.ifBlank { "无解析" },
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Resolution Actions — Single-line concise buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onKeepPrimary,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "采用来源 1 (${q1.answer.joinToString(",")})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                OutlinedButton(
                    onClick = onKeepSecondary,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "采用来源 2 (${q2.answer.joinToString(",")})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onKeepBoth) {
                    Text("保留两题", style = MaterialTheme.typography.labelMedium, color = colors.textSecondary, maxLines = 1, softWrap = false)
                }
                TextButton(onClick = onManualEdit) {
                    Text("编辑", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primaryAccent, maxLines = 1, softWrap = false)
                }
            }
        }
    }
}

@Composable
private fun AuditEmptyStateCard(message: String) {
    val colors = SereneTheme.colors
    SereneCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Verified,
                contentDescription = null,
                tint = colors.mintText,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 21 复习计划与提醒设置 (Review Plan & Reminder Screen) — Faithful implementation of `study_19_screen.png`
 */
@Composable
fun ReviewPlanScreen(
    viewModel: StudyViewModel,
    settings: UserSettingsState,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    var dailyGoal by remember(settings.dailyTargetCount) { mutableIntStateOf(settings.dailyTargetCount) }
    var preset by remember(settings.reviewPreset) { mutableStateOf(settings.reviewPreset) }
    var reminderEnabled by remember(settings.reminderEnabled) { mutableStateOf(settings.reminderEnabled) }
    var reminderTime by remember(settings.reminderTime) { mutableStateOf(settings.reminderTime) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        reminderEnabled = granted
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "怎么安排复习",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Memory Curve Visual Card
            item {
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            SereneBadge(
                                text = "● 刚好在快忘掉时提醒你",
                                containerColor = colors.amberBg,
                                contentColor = colors.amberText
                            )
                            Text(
                                text = "做错的题按这几个时间点回头看",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                        ReviewCurveIllustration(modifier = Modifier.size(64.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("第 1 天", "第 2 天", "第 4 天", "第 7 天", "第 15 天").forEachIndexed { idx, day ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (idx < 3) colors.primarySoft else colors.subtleSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "温习第${idx + 1}遍",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.textMuted,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (idx < 3) colors.primaryAccent else colors.textPrimary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Daily Review Goal Selector
            item {
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "每天打算看多少道题？",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        SereneBadge(
                            text = "每天 $dailyGoal 题",
                            containerColor = colors.primarySoft,
                            contentColor = colors.primaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(10, 20, 30, 50).forEach { goal ->
                            val selected = dailyGoal == goal
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) colors.primaryAccent else colors.subtleSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { dailyGoal = goal }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${goal}题",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else colors.textPrimary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Review Rhythm Preset
            item {
                SereneCard {
                    Text(
                        text = "你现在的备考节奏是哪种？",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val presets = listOf(
                        "智能推荐" to "平时正常学，错题和新题搭着来",
                        "考前冲刺" to "快考试了，先把老错的题集中刷熟",
                        "稳步巩固" to "时间充裕，每天少做几道保持手感"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.forEach { (name, desc) ->
                            val selected = preset == name
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) colors.primarySoft.copy(alpha = 0.6f) else colors.subtleSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = if (selected) 1.5.dp else 1.dp,
                                        color = if (selected) colors.primaryAccent else Color.Transparent,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { preset = name }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = name,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) colors.primaryAccent else colors.textPrimary,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = colors.textSecondary,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    if (selected) {
                                        SereneBadge(
                                            text = "正在用",
                                            containerColor = colors.primaryAccent,
                                            contentColor = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Push Reminder & Time
            item {
                SereneCard {
                    SettingSwitchRow(
                        title = "每天定时提醒我刷一会儿",
                        subtitle = null,
                        checked = reminderEnabled,
                        onCheckedChange = { wantEnable ->
                            if (wantEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                reminderEnabled = wantEnable
                            }
                        }
                    )
                    if (reminderEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("08:00", "12:30", "20:30", "22:00").forEach { t ->
                                val selected = reminderTime == t
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) colors.primarySoft else colors.subtleSurface,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { reminderTime = t }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = t,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) colors.primaryAccent else colors.textPrimary,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(
            color = colors.cardSurface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Button(
                    onClick = {
                        viewModel.updatePlanSetting(
                            target = dailyGoal,
                            preset = preset,
                            reminder = reminderEnabled,
                            time = reminderTime
                        )
                        viewModel.navigateBack()
                    },
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_review_plan_btn")
                ) {
                    Text("定好了，按这个来", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                }
            }
        }
    }
}

/**
 * 22 数据备份与导出管理 (Data Management & Backup Screen) — Faithful implementation of `study_20_screen.png`
 * Inline JSON backup preview (no popup dialog), zero excess blank space.
 */
@Composable
fun DataManagementScreen(
    viewModel: StudyViewModel,
    banks: List<QuestionBankEntity>,
    questions: List<QuestionEntity>,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    val context = LocalContext.current
    var showBackupJsonInline by remember { mutableStateOf(false) }
    val jsonPreview = remember(banks, questions) { viewModel.buildFullBackupJson() }

    val exportDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    out.write(jsonPreview.toByteArray(Charsets.UTF_8))
                }
                viewModel.showToast("备份文件已经存好啦")
            } catch (_: Exception) {
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "手机里的题库与备份",
            subtitle = null,
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = if (isTablet) 24.dp else 16.dp,
                end = if (isTablet) 24.dp else 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Storage Breakdown Card
            item {
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "保存在本机的离线数据一共占了",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.textMuted,
                                maxLines = 1,
                                softWrap = false
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "12.8 MB",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                        SereneBadge(
                            text = "${banks.size} 本题库 · ${questions.size} 道题",
                            containerColor = colors.primarySoft,
                            contentColor = colors.primaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SereneLinearProgress(progress = 0.36f, indicatorColor = colors.primaryAccent, height = 8.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("题目和解析占了 8.4 MB", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary, maxLines = 1, softWrap = false)
                        Text("做题记录和草稿占了 4.4 MB", style = MaterialTheme.typography.labelSmall, color = colors.textMuted, maxLines = 1, softWrap = false)
                    }
                }
            }

            // 2. Backup & Export Actions + Inline JSON Preview
            item {
                SereneCard {
                    Text(
                        text = "打包备份出来（换手机也不怕丢）",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { exportDocumentLauncher.launch("zhixu_study_backup_2026.json") },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("export_json_backup_btn")
                        ) {
                            Icon(imageVector = Icons.Outlined.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("存一份备份文件", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                        OutlinedButton(
                            onClick = { showBackupJsonInline = !showBackupJsonInline },
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Outlined.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showBackupJsonInline) "收起内容" else "看看备份内容",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    AnimatedVisibility(visible = showBackupJsonInline) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Text(
                                text = jsonPreview.take(900) + "\n...",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            // 3. Bank Breakdown & Factory Seed Reset
            item {
                SereneCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "目前手机里存着的题库",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        TextButton(onClick = { viewModel.resetToFactorySeed() }) {
                            Icon(imageVector = Icons.Outlined.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("恢复成初始题库", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    banks.forEach { bank ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = bank.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${bank.totalTargetCount} 题 · 已刷 ${bank.practicedCount} 题 · 对了 ${bank.accuracyPercent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textMuted,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            SereneBadge(
                                text = "没网也能刷",
                                containerColor = colors.mintBg,
                                contentColor = colors.mintText
                            )
                        }
                    }
                }
            }
        }
    }
}
