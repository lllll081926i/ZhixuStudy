package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppleFloatingDock
import com.example.ui.screens.AiServiceScreen
import com.example.ui.screens.AuditScreen
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.DataManagementScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImportMappingScreen
import com.example.ui.screens.ImportPreviewScreen
import com.example.ui.screens.ImportResultScreen
import com.example.ui.screens.ImportSourceScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.LibrarySearchScreen
import com.example.ui.screens.PracticeConfigScreen
import com.example.ui.screens.QuestionEditorScreen
import com.example.ui.screens.QuestionListScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RecapScreen
import com.example.ui.screens.ReviewPlanScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.screens.SettingsAppearanceScreen
import com.example.ui.screens.SettingsDndScreen
import com.example.ui.screens.SettingsGesturesScreen
import com.example.ui.screens.SettingsPracticeFlowScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SettingsSoundHapticsScreen
import com.example.ui.screens.SettingsTypographyScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.SereneStudyTheme
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val studyViewModel: StudyViewModel = viewModel()
            val settings by studyViewModel.settings.collectAsStateWithLifecycle()

            SereneStudyTheme(
                themeMode = settings.themeMode,
                fontScalePercent = settings.fontScalePercent
            ) {
                SereneStudyAppRoot(viewModel = studyViewModel)
            }
        }
    }
}

@Composable
fun SereneStudyAppRoot(viewModel: StudyViewModel) {
    val colors = SereneTheme.colors
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activePrimaryTab by viewModel.activePrimaryTab.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val banks by viewModel.banks.collectAsStateWithLifecycle()
    val questions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val practiceRecords by viewModel.practiceRecords.collectAsStateWithLifecycle()
    val importPresets by viewModel.importPresets.collectAsStateWithLifecycle()
    val importLogs by viewModel.importLogs.collectAsStateWithLifecycle()

    val selectedBankName by viewModel.selectedBankName.collectAsStateWithLifecycle()
    val selectedChapterFilter by viewModel.selectedChapterFilter.collectAsStateWithLifecycle()
    val librarySearchQuery by viewModel.librarySearchQuery.collectAsStateWithLifecycle()
    val questionListSearchQuery by viewModel.questionListSearchQuery.collectAsStateWithLifecycle()
    val questionListFilter by viewModel.questionListFilter.collectAsStateWithLifecycle()
    val isBatchMode by viewModel.isBatchMode.collectAsStateWithLifecycle()
    val batchSelectedIds by viewModel.batchSelectedIds.collectAsStateWithLifecycle()
    val reviewFilter by viewModel.reviewFilter.collectAsStateWithLifecycle()
    val reviewSearchQuery by viewModel.reviewSearchQuery.collectAsStateWithLifecycle()
    val statsRange by viewModel.statsRange.collectAsStateWithLifecycle()
    val editingQuestion by viewModel.editingQuestion.collectAsStateWithLifecycle()
    val importState by viewModel.importState.collectAsStateWithLifecycle()
    val quizConfig by viewModel.quizConfig.collectAsStateWithLifecycle()
    val quizSession by viewModel.quizSession.collectAsStateWithLifecycle()
    val auditFilter by viewModel.auditFilter.collectAsStateWithLifecycle()
    val selectedSettingsCategory by viewModel.selectedSettingsCategory.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Handle system back navigation across all sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    val reviewDueCount = questions.count { it.wrongCount > 0 || it.isStarred }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgCanvas)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val isTablet = maxWidth >= 600.dp
        val tabletSplitActive = settings.tabletSplitViewPreferred

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    val isEnteringDeep = targetState != AppScreen.HOME && initialState == AppScreen.HOME
                    val isPoppingBack = targetState == AppScreen.HOME && initialState != AppScreen.HOME
                    when {
                        isEnteringDeep -> {
                            (slideInHorizontally(
                                initialOffsetX = { fullWidth -> (fullWidth * 0.12f).toInt() },
                                animationSpec = tween(280, easing = FastOutSlowInEasing)
                            ) + fadeIn(animationSpec = tween(240))) togetherWith (slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -(fullWidth * 0.08f).toInt() },
                                animationSpec = tween(240, easing = FastOutSlowInEasing)
                            ) + fadeOut(animationSpec = tween(200)))
                        }
                        isPoppingBack -> {
                            (slideInHorizontally(
                                initialOffsetX = { fullWidth -> -(fullWidth * 0.08f).toInt() },
                                animationSpec = tween(260, easing = FastOutSlowInEasing)
                            ) + fadeIn(animationSpec = tween(220))) togetherWith (slideOutHorizontally(
                                targetOffsetX = { fullWidth -> (fullWidth * 0.12f).toInt() },
                                animationSpec = tween(240, easing = FastOutSlowInEasing)
                            ) + fadeOut(animationSpec = tween(180)))
                        }
                        targetState.isPrimaryTab && initialState.isPrimaryTab -> {
                            val primaryOrder = listOf(AppScreen.HOME, AppScreen.LIBRARY, AppScreen.REVIEW, AppScreen.STATS)
                            val fromIdx = primaryOrder.indexOf(initialState)
                            val toIdx = primaryOrder.indexOf(targetState)
                            val isForward = toIdx >= fromIdx
                            val sign = if (isForward) 1 else -1

                            (slideInHorizontally(
                                initialOffsetX = { fullWidth -> (fullWidth * 0.25f * sign).toInt() },
                                animationSpec = tween(280, easing = FastOutSlowInEasing)
                            ) + fadeIn(animationSpec = tween(240))) togetherWith (slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -(fullWidth * 0.25f * sign).toInt() },
                                animationSpec = tween(240, easing = FastOutSlowInEasing)
                            ) + fadeOut(animationSpec = tween(200)))
                        }
                        else -> {
                            (fadeIn(animationSpec = tween(240, easing = FastOutSlowInEasing)) + scaleIn(
                                initialScale = 0.98f,
                                animationSpec = tween(240, easing = FastOutSlowInEasing)
                            )) togetherWith (fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)) + scaleOut(
                                targetScale = 0.98f,
                                animationSpec = tween(200, easing = FastOutSlowInEasing)
                            ))
                        }
                    }
                },
                label = "serene_screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(
                        viewModel = viewModel,
                        banks = banks,
                        questions = questions,
                        settings = settings,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.LIBRARY -> LibraryScreen(
                        viewModel = viewModel,
                        banks = banks,
                        questions = questions,
                        searchQuery = librarySearchQuery,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.REVIEW -> ReviewScreen(
                        viewModel = viewModel,
                        questions = questions,
                        activeFilter = reviewFilter,
                        searchQuery = reviewSearchQuery,
                        isTablet = isTablet
                    )

                    AppScreen.STATS -> StatsScreen(
                        viewModel = viewModel,
                        questions = questions,
                        records = practiceRecords,
                        statsRange = statsRange,
                        isTablet = isTablet
                    )

                    AppScreen.LIBRARY_SEARCH -> LibrarySearchScreen(
                        viewModel = viewModel,
                        banks = banks,
                        questions = questions,
                        searchQuery = librarySearchQuery,
                        isTablet = isTablet
                    )

                    AppScreen.BOOK_DETAIL -> BookDetailScreen(
                        viewModel = viewModel,
                        bankName = selectedBankName,
                        banks = banks,
                        questions = questions,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.QUESTION_LIST -> QuestionListScreen(
                        viewModel = viewModel,
                        bankName = selectedBankName,
                        chapterFilter = selectedChapterFilter,
                        questions = questions,
                        searchQuery = questionListSearchQuery,
                        activeFilter = questionListFilter,
                        isBatchMode = isBatchMode,
                        batchSelectedIds = batchSelectedIds,
                        isTablet = isTablet
                    )

                    AppScreen.QUESTION_EDITOR -> QuestionEditorScreen(
                        viewModel = viewModel,
                        initialQuestion = editingQuestion,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.IMPORT_SOURCE -> ImportSourceScreen(
                        viewModel = viewModel,
                        importState = importState,
                        banks = banks,
                        isTablet = isTablet
                    )

                    AppScreen.IMPORT_MAPPING -> ImportMappingScreen(
                        viewModel = viewModel,
                        importState = importState,
                        presets = importPresets,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.IMPORT_PREVIEW -> ImportPreviewScreen(
                        viewModel = viewModel,
                        importState = importState,
                        isTablet = isTablet
                    )

                    AppScreen.IMPORT_RESULT -> ImportResultScreen(
                        viewModel = viewModel,
                        importState = importState,
                        isTablet = isTablet
                    )

                    AppScreen.PRACTICE_CONFIG -> PracticeConfigScreen(
                        viewModel = viewModel,
                        config = quizConfig,
                        banks = banks,
                        isTablet = isTablet
                    )

                    AppScreen.QUIZ -> QuizScreen(
                        viewModel = viewModel,
                        session = quizSession,
                        hapticEnabled = settings.hapticFeedback,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.QUIZ_RESULT -> QuizResultScreen(
                        viewModel = viewModel,
                        session = quizSession,
                        isTablet = isTablet
                    )

                    AppScreen.RECAP -> RecapScreen(
                        viewModel = viewModel,
                        session = quizSession,
                        isTablet = isTablet
                    )

                    AppScreen.HISTORY -> HistoryScreen(
                        viewModel = viewModel,
                        records = practiceRecords,
                        isTablet = isTablet
                    )

                    AppScreen.REVIEW_PLAN -> ReviewPlanScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS -> SettingsScreen(
                        viewModel = viewModel,
                        settings = settings,
                        selectedCategory = selectedSettingsCategory,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.SETTINGS_APPEARANCE -> SettingsAppearanceScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS_TYPOGRAPHY -> SettingsTypographyScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS_SOUND_HAPTICS -> SettingsSoundHapticsScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS_PRACTICE_FLOW -> SettingsPracticeFlowScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS_GESTURES -> SettingsGesturesScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.SETTINGS_DND -> SettingsDndScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )

                    AppScreen.DATA_MGMT -> DataManagementScreen(
                        viewModel = viewModel,
                        banks = banks,
                        questions = questions,
                        isTablet = isTablet
                    )

                    AppScreen.AUDIT -> AuditScreen(
                        viewModel = viewModel,
                        questions = questions,
                        importLogs = importLogs,
                        auditFilter = auditFilter,
                        isTablet = isTablet,
                        tabletSplitActive = tabletSplitActive
                    )

                    AppScreen.AI_SERVICE -> AiServiceScreen(
                        viewModel = viewModel,
                        settings = settings,
                        isTablet = isTablet
                    )
                }
            }

            // Apple-Style Floating Bottom Dock UI Block:
            // Shown on all 4 primary tabs on both Phone and Tablet
            val showFloatingDock = currentScreen.isPrimaryTab

            AnimatedVisibility(
                visible = showFloatingDock,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AppleFloatingDock(
                    activeTab = activePrimaryTab,
                    reviewBadgeCount = reviewDueCount,
                    isTablet = isTablet,
                    onSelectTab = { tab -> viewModel.selectPrimaryTab(tab) }
                )
            }

            // Floating Toast Pill Banner
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF1E2235).copy(alpha = 0.94f),
                    shadowElevation = 10.dp,
                    modifier = Modifier.clickable { viewModel.dismissToast() }
                ) {
                    Text(
                        text = toastMessage ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}
