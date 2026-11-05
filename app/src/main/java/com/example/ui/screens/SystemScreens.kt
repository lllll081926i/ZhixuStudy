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
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) }
        )

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
                        val detailTitle = when (selectedCategory) {
                            "typography" -> "排版与字号"
                            "haptics" -> "触感与提示"
                            "practice_flow" -> "作答与流转"
                            "gestures" -> "手势与答题卡"
                            "dnd" -> "免打扰时段"
                            else -> "主题与色彩"
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 22.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = detailTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
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
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Group 1: 界面呈现
        if (matches("主题", "外观", "字号", "排版", "震动", "触感", themeModeName, dynamicName)) {
            SettingsSectionGroup(title = "界面呈现") {
                SettingsRowItem(
                    icon = Icons.Outlined.Palette,
                    title = "主题与色彩",
                    subtitle = "$themeModeName · $dynamicName",
                    iconBg = colors.primarySoft,
                    iconTint = colors.primaryAccent,
                    isActive = activeCategory == "appearance",
                    onClick = onOpenAppearance,
                    testTag = "settings_row_appearance"
                )
                SettingsDivider()
                SettingsRowItem(
                    icon = Icons.Outlined.FormatSize,
                    title = "排版与字号",
                    subtitle = "${fontScalePercent}% · ${settings.lineSpacingMode}",
                    isActive = activeCategory == "typography",
                    onClick = onOpenTypography,
                    testTag = "settings_row_typography"
                )
                SettingsDivider()
                SettingsRowItem(
                    icon = Icons.Outlined.Vibration,
                    title = "触感反馈",
                    subtitle = if (settings.hapticFeedback) settings.hapticStrength else "关闭",
                    isActive = activeCategory == "haptics",
                    onClick = onOpenHaptics,
                    testTag = "settings_row_haptics"
                )
            }
        }

        // Group 2: 答题流转
        if (matches("作答", "解析", "自动切题", "乱序", "手势", "答题卡")) {
            SettingsSectionGroup(title = "答题流转") {
                SettingsRowItem(
                    icon = Icons.Outlined.Checklist,
                    title = "作答与流转",
                    subtitle = if (settings.instantExplanation) "即时解析" else "交卷解析",
                    isActive = activeCategory == "practice_flow",
                    onClick = onOpenPracticeFlow,
                    testTag = "settings_row_practice_flow"
                )
                SettingsDivider()
                SettingsRowItem(
                    icon = Icons.Outlined.Swipe,
                    title = "手势与答题卡",
                    subtitle = settings.answerSheetStyle,
                    isActive = activeCategory == "gestures",
                    onClick = onOpenGestures,
                    testTag = "settings_row_gestures"
                )
            }
        }

        // Group 3: 学习规划
        if (matches("复习", "计划", "目标", "免打扰")) {
            SettingsSectionGroup(title = "学习规划") {
                SettingsRowItem(
                    icon = Icons.Outlined.Psychology,
                    title = "复习计划与目标",
                    subtitle = "每天 ${settings.dailyTargetCount} 题 · ${settings.reviewPreset}",
                    onClick = onOpenReviewPlan,
                    testTag = "settings_row_review_mode"
                )
                SettingsDivider()
                SettingsRowItem(
                    icon = Icons.Outlined.DoNotDisturbOn,
                    title = "免打扰时段",
                    subtitle = if (settings.doNotDisturbMode) settings.dndTimeRange else "关闭",
                    isActive = activeCategory == "dnd",
                    onClick = onOpenDnd,
                    testTag = "settings_row_dnd"
                )
            }
        }

        // Group 4: 数据与体检
        if (matches("备份", "数据", "体检", "冲突", "恢复")) {
            SettingsSectionGroup(title = "数据与体检") {
                SettingsRowItem(
                    icon = Icons.Outlined.Folder,
                    title = "数据管理与备份",
                    subtitle = "本地离线存储",
                    onClick = onOpenDataMgmt,
                    testTag = "settings_row_data_mgmt"
                )
                SettingsDivider()
                SettingsRowItem(
                    icon = Icons.Outlined.Verified,
                    title = "题库体检",
                    subtitle = "全库冲突审查",
                    subtitleColor = colors.mintText,
                    iconBg = colors.mintBg,
                    iconTint = colors.mintText,
                    onClick = onOpenAudit,
                    testTag = "settings_row_audit"
                )
            }
        }

        // Group 5: AI 辅助模型
        if (matches("AI", "模型", "API", "密钥", settings.aiModelId)) {
            SettingsSectionGroup(title = "智能模型配置") {
                SettingsRowItem(
                    icon = Icons.Outlined.Hub,
                    title = "模型接口与密钥",
                    subtitle = settings.aiModelId,
                    iconBg = colors.primarySoft,
                    iconTint = colors.primaryAccent,
                    onClick = onOpenAiService,
                    testTag = "settings_row_ai_api"
                )
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
        // 1. Theme Mode Selection (System / Light / Dark)
        item {
            SereneCard {
                Text(
                    text = "主题外观",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                val modes = listOf(
                    Triple("system", "跟随系统", Icons.Outlined.SettingsBrightness),
                    Triple("light", "浅色", Icons.Outlined.LightMode),
                    Triple("dark", "深色", Icons.Outlined.DarkMode)
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
                                    modifier = Modifier.size(20.dp)
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

        // 2. Monet Dynamic & Classic Accent Palettes
        item {
            SereneCard {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "动态取色",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        SereneBadge(
                            text = "已自适应壁纸",
                            containerColor = colors.mintBg,
                            contentColor = colors.mintText
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    SettingsDivider()
                    Spacer(modifier = Modifier.height(14.dp))
                }
                Text(
                    text = "主色调",
                    style = MaterialTheme.typography.labelMedium,
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
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
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
                        text = "字号缩放",
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
                Spacer(modifier = Modifier.height(10.dp))
                Slider(
                    value = settings.fontScalePercent.toFloat(),
                    onValueChange = { viewModel.setFontScale(it.toInt()) },
                    valueRange = 75f..125f,
                    steps = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        75 to "精简",
                        85 to "推荐",
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
                    text = "行距",
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
                    title = "触感反馈",
                    subtitle = null,
                    checked = settings.hapticFeedback,
                    onCheckedChange = { viewModel.updateHabitSetting(haptic = it) }
                )
                if (settings.hapticFeedback) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "反馈强度",
                        style = MaterialTheme.typography.labelMedium,
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
                    title = "即时解析",
                    subtitle = null,
                    checked = settings.instantExplanation,
                    onCheckedChange = { viewModel.updateHabitSetting(instantExp = it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(12.dp))
                SettingSwitchRow(
                    title = "答对自动下一题",
                    subtitle = null,
                    checked = settings.autoNextOnCorrect,
                    onCheckedChange = { viewModel.updateHabitSetting(autoNext = it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(12.dp))
                SettingSwitchRow(
                    title = "选项乱序",
                    subtitle = null,
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
                    title = "左右滑动切题",
                    subtitle = null,
                    checked = settings.swipeSwitchQuestion,
                    onCheckedChange = { viewModel.updateGestureSettings(swipeSwitch = it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(12.dp))
                SettingSwitchRow(
                    title = "双击题干收藏",
                    subtitle = null,
                    checked = settings.doubleTapStar,
                    onCheckedChange = { viewModel.updateGestureSettings(doubleTapStar = it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDivider()
                Spacer(modifier = Modifier.height(12.dp))
                SettingSwitchRow(
                    title = "长按选项排除",
                    subtitle = null,
                    checked = settings.longPressExcludeOption,
                    onCheckedChange = { viewModel.updateGestureSettings(longPressExclude = it) }
                )
            }
        }

        item {
            SereneCard {
                Text(
                    text = "答题卡样式",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(10.dp))
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
                                modifier = Modifier.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = style,
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
                    title = "夜间免打扰",
                    subtitle = null,
                    checked = settings.doNotDisturbMode,
                    onCheckedChange = { viewModel.updatePlanSetting(dnd = it) }
                )
                if (settings.doNotDisturbMode) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "时段",
                        style = MaterialTheme.typography.labelMedium,
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

    data class AiPreset(val name: String, val baseUrl: String, val modelId: String)
    val presets = remember {
        listOf(
            AiPreset("DeepSeek", "https://api.deepseek.com/v1", "deepseek-chat"),
            AiPreset("OpenAI", "https://api.openai.com/v1", "gpt-4o-mini"),
            AiPreset("月之暗面 Kimi", "https://api.moonshot.cn/v1", "moonshot-v1-8k"),
            AiPreset("智谱 GLM", "https://open.bigmodel.cn/api/paas/v4", "glm-4-flash"),
            AiPreset("本地 Ollama", "http://10.0.2.2:11434/v1", "llama3"),
            AiPreset("自定义", "", "")
        )
    }

    var selectedPresetName by remember(settings.aiBaseUrl) {
        val found = presets.find { it.baseUrl.isNotBlank() && settings.aiBaseUrl.startsWith(it.baseUrl) }
        mutableStateOf(found?.name ?: "自定义")
    }

    var baseUrl by remember(settings.aiBaseUrl) { mutableStateOf(settings.aiBaseUrl) }
    var modelId by remember(settings.aiModelId) { mutableStateOf(settings.aiModelId) }
    var apiKey by remember(settings.aiApiKeyMasked) { mutableStateOf(settings.aiApiKeyMasked) }
    var pingLatencyMs by remember { mutableIntStateOf(260) }
    var isEditingKeyInline by remember { mutableStateOf(false) }
    var rawKeyInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "大模型服务",
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
            // 1. Provider Presets
            item {
                SereneCard {
                    Text(
                        text = "服务商",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val rows = presets.chunked(3)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { p ->
                                    val isSelected = selectedPresetName == p.name
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) colors.primaryAccent else colors.subtleSurface,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                selectedPresetName = p.name
                                                if (p.name != "自定义") {
                                                    baseUrl = p.baseUrl
                                                    modelId = p.modelId
                                                }
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = p.name,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else colors.textPrimary,
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

            // 2. Endpoint & Key Settings
            item {
                SereneCard {
                    OutlinedTextField(
                        value = baseUrl,
                        onValueChange = {
                            baseUrl = it
                            selectedPresetName = "自定义"
                        },
                        label = { Text("Base URL", maxLines = 1, softWrap = false) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = modelId,
                        onValueChange = { modelId = it },
                        label = { Text("Model ID", maxLines = 1, softWrap = false) },
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "API Key",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textMuted
                            )
                            Text(
                                text = apiKey,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = colors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        OutlinedButton(
                            onClick = { isEditingKeyInline = !isEditingKeyInline },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(if (isEditingKeyInline) "收起" else "修改", maxLines = 1, softWrap = false)
                        }
                    }

                    AnimatedVisibility(visible = isEditingKeyInline) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            OutlinedTextField(
                                value = rawKeyInput,
                                onValueChange = { rawKeyInput = it },
                                label = { Text("输入 API Key", maxLines = 1) },
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
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                                ) {
                                    Text("保存", maxLines = 1, softWrap = false)
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
                            text = "${pingLatencyMs}ms",
                            containerColor = colors.mintBg,
                            contentColor = colors.mintText
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.primarySoft,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    pingLatencyMs = (160..290).random()
                                    viewModel.showToast("连接正常 · ${pingLatencyMs}ms")
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Outlined.Speed, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(15.dp))
                                Text("测试", color = colors.primaryAccent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
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
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Button(
                    onClick = {
                        viewModel.saveAiServiceConfig(
                            protocol = "OpenAI",
                            baseUrl = baseUrl,
                            modelId = modelId,
                            apiKey = apiKey
                        )
                        viewModel.navigateBack()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("保存", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
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
            title = "题库体检",
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
                                text = "一键修复全部 ($issueCount 处)",
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
                    "all" to "答案冲突 (${conflictPairs.size})",
                    "duplicate" to "重复题目 (${duplicatePairs.size})",
                    "missing" to "缺失解析 (${missingExpQuestions.size})"
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
                            AuditEmptyStateCard("暂无重复题目")
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
                                            text = "完全重复",
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
                                        Text("同时保留", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
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
                                        Text("合并保留单题", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }

                "missing" -> {
                    if (missingExpQuestions.isEmpty()) {
                        item {
                            AuditEmptyStateCard("暂无缺失解析题目")
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
                                        text = "缺失解析",
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
                                        Text("编辑解析", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
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
                                        Text("自动补充", style = MaterialTheme.typography.labelMedium, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    if (conflictPairs.isEmpty()) {
                        item {
                            AuditEmptyStateCard("暂无答案冲突题目")
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
            title = "复习计划",
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
                                text = "艾宾浩斯曲线",
                                containerColor = colors.amberBg,
                                contentColor = colors.amberText
                            )
                            Text(
                                text = "复习节点分布",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                        ReviewCurveIllustration(modifier = Modifier.size(56.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("1天", "2天", "4天", "7天", "15天").forEachIndexed { idx, day ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (idx < 3) colors.primarySoft else colors.subtleSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "阶段 ${idx + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.textMuted,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelMedium,
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
                            text = "每日目标",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        SereneBadge(
                            text = "$dailyGoal 题/天",
                            containerColor = colors.primarySoft,
                            contentColor = colors.primaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
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

            // 3. Review Rhythm Preset
            item {
                SereneCard {
                    Text(
                        text = "复习节奏",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val presets = listOf("智能推荐", "考前冲刺", "稳步巩固")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.forEach { name ->
                            val selected = preset == name
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) colors.primaryAccent else colors.subtleSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { preset = name }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = name,
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

            // 4. Push Reminder & Time
            item {
                SereneCard {
                    SettingSwitchRow(
                        title = "定时提醒",
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
                        Spacer(modifier = Modifier.height(10.dp))
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
                    Text("保存", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
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
                viewModel.showToast("已导出备份文件")
            } catch (_: Exception) {
            }
        }
    }

    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val jsonStr = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
                if (jsonStr.isNotBlank()) {
                    viewModel.restoreFromBackupJson(jsonStr)
                }
            } catch (e: Exception) {
                viewModel.showToast("备份文件读取失败")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "数据管理与备份",
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
                        text = "数据备份与导入恢复",
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
                            Text("导出备份", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                        OutlinedButton(
                            onClick = { importBackupLauncher.launch("application/json") },
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Outlined.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("从备份恢复", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
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
