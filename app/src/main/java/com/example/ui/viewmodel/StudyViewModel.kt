package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ImportLogEntity
import com.example.data.local.ImportPresetEntity
import com.example.data.local.PracticeRecordEntity
import com.example.data.local.QuestionBankEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.SeedDataFactory
import com.example.data.local.UserPreferencesRepository
import com.example.data.local.UserSettingsState
import com.example.data.local.listToJson
import com.example.data.parser.ColumnMappingConfig
import com.example.data.parser.EvaluatedImportItem
import com.example.data.parser.PreprocessRulesConfig
import com.example.data.parser.RawImportRow
import com.example.data.parser.SmartImportEngine
import com.example.ui.components.PrimaryTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class AppScreen(val title: String, val isPrimaryTab: Boolean = false) {
    // 01-04 Primary Tabs
    HOME("学习", true),
    LIBRARY("题库", true),
    REVIEW("复习", true),
    STATS("学习统计", true),

    // 05-07 Question Bank Management
    LIBRARY_SEARCH("搜索题库"),
    BOOK_DETAIL("题库详情"),
    QUESTION_LIST("题目列表"),
    QUESTION_EDITOR("题目编辑"),

    // 08-11 Smart Import 4-Step Pipeline
    IMPORT_SOURCE("导入题库 · 选择文件"),
    IMPORT_MAPPING("导入题库 · 识别与映射"),
    IMPORT_PREVIEW("导入题库 · 预览与审查"),
    IMPORT_RESULT("导入题库 · 导入完成"),

    // 12-16 Practice & Exam Engine
    PRACTICE_CONFIG("练习配置"),
    QUIZ("答题页"),
    QUIZ_RESULT("练习结果"),
    RECAP("答题复盘"),
    HISTORY("历史记录"),

    // 17-27 System, Audit, AI Service & Dedicated Settings Sub-Screens
    REVIEW_PLAN("复习计划"),
    SETTINGS("设置中心"),
    SETTINGS_APPEARANCE("外观与主题"),
    SETTINGS_TYPOGRAPHY("排版与正文字号"),
    SETTINGS_SOUND_HAPTICS("音效与触感反馈"),
    SETTINGS_PRACTICE_FLOW("答题流程与判定"),
    SETTINGS_GESTURES("快捷手势与答题卡"),
    SETTINGS_DND("学习习惯与免打扰"),
    DATA_MGMT("数据与备份"),
    AUDIT("题库体检与冲突审查"),
    AI_SERVICE("AI 辅助与模型设置")
}

data class QuizConfigState(
    val bankName: String = "《铁路运输组织基础》",
    val mode: String = "顺序练习", // "顺序练习", "随机练习", "模拟考试", "背题模式"
    val range: String = "全部题目", // "全部题目", "错题", "收藏"
    val count: Int = 10,
    val instantExplanation: Boolean = true,
    val shuffleOptions: Boolean = false
)

data class ActiveQuizSession(
    val title: String = "《铁路运输组织基础》练习",
    val mode: String = "顺序练习",
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val userSelections: Map<String, List<String>> = emptyMap(), // questionId -> selected answers or fill inputs
    val submittedMap: Map<String, Boolean> = emptyMap(), // questionId -> isSubmitted
    val elapsedSeconds: Int = 0,
    val showAnswerSheet: Boolean = false,
    val aiDeepExpandedMap: Map<String, Boolean> = emptyMap(),
    val generatedVariantMap: Map<String, QuestionEntity> = emptyMap()
)

data class SmartImportFlowState(
    val fileName: String = "2026铁路运输模拟题.xlsx",
    val fileSizeLabel: String = "48.6 KB · UTF-8",
    val sheetName: String = "Sheet1 (主试题表)",
    val startRow: Int = 2,
    val targetBankName: String = "《铁路运输组织基础》",
    val headers: List<String> = emptyList(),
    val rawRows: List<RawImportRow> = emptyList(),
    val mapping: ColumnMappingConfig = ColumnMappingConfig(),
    val rules: PreprocessRulesConfig = PreprocessRulesConfig(),
    val defaultDuplicateStrategy: String = "skip", // "skip", "replace", "keep"
    val previewFilter: String = "all", // "all", "invalid", "conflict", "valid"
    val evaluatedItems: List<EvaluatedImportItem> = emptyList(),
    val lastCommitStats: CommitStats = CommitStats()
)

data class CommitStats(
    val added: Int = 5,
    val replaced: Int = 1,
    val skipped: Int = 1,
    val invalidSkipped: Int = 1,
    val targetBank: String = "《铁路运输组织基础》"
)

data class CrossBankConflictPair(
    val id: String,
    val stem: String,
    val questionA: QuestionEntity,
    val questionB: QuestionEntity,
    val isSameAnswerDuplicate: Boolean
)

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.appDao()
    private val prefsRepo = UserPreferencesRepository(application)

    val settings: StateFlow<UserSettingsState> = prefsRepo.settingsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserSettingsState()
    )

    val banks: StateFlow<List<QuestionBankEntity>> = dao.observeBanks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allQuestions: StateFlow<List<QuestionEntity>> = dao.observeAllQuestions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val practiceRecords: StateFlow<List<PracticeRecordEntity>> = dao.observePracticeRecords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val importPresets: StateFlow<List<ImportPresetEntity>> = dao.observeImportPresets().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val importLogs: StateFlow<List<ImportLogEntity>> = dao.observeImportLogs().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Navigation & Screen State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _activePrimaryTab = MutableStateFlow(PrimaryTab.HOME)
    val activePrimaryTab: StateFlow<PrimaryTab> = _activePrimaryTab.asStateFlow()

    private val backStack = ArrayDeque<AppScreen>()

    // Toast / Banner feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Selected Bank & Question Filter State
    private val _selectedBankName = MutableStateFlow("《铁路运输组织基础》")
    val selectedBankName: StateFlow<String> = _selectedBankName.asStateFlow()

    private val _selectedChapterFilter = MutableStateFlow<String?>(null)
    val selectedChapterFilter: StateFlow<String?> = _selectedChapterFilter.asStateFlow()

    private val _librarySearchQuery = MutableStateFlow("")
    val librarySearchQuery: StateFlow<String> = _librarySearchQuery.asStateFlow()

    private val _questionListSearchQuery = MutableStateFlow("")
    val questionListSearchQuery: StateFlow<String> = _questionListSearchQuery.asStateFlow()

    private val _questionListFilter = MutableStateFlow("all") // "all", "wrong", "starred", "hard", "unanswered"
    val questionListFilter: StateFlow<String> = _questionListFilter.asStateFlow()

    private val _batchSelectedIds = MutableStateFlow<Set<String>>(emptySet())
    val batchSelectedIds: StateFlow<Set<String>> = _batchSelectedIds.asStateFlow()

    private val _isBatchMode = MutableStateFlow(false)
    val isBatchMode: StateFlow<Boolean> = _isBatchMode.asStateFlow()

    // Review Screen Filter State
    private val _reviewFilter = MutableStateFlow("today") // "today", "wrong", "starred", "hard"
    val reviewFilter: StateFlow<String> = _reviewFilter.asStateFlow()

    private val _reviewSearchQuery = MutableStateFlow("")
    val reviewSearchQuery: StateFlow<String> = _reviewSearchQuery.asStateFlow()

    // Stats Screen Range State
    private val _statsRange = MutableStateFlow("7d") // "7d", "30d", "all"
    val statsRange: StateFlow<String> = _statsRange.asStateFlow()

    // Question Editor State
    private val _editingQuestion = MutableStateFlow<QuestionEntity?>(null)
    val editingQuestion: StateFlow<QuestionEntity?> = _editingQuestion.asStateFlow()

    // Smart Import State
    private val _importState = MutableStateFlow(SmartImportFlowState())
    val importState: StateFlow<SmartImportFlowState> = _importState.asStateFlow()

    // Quiz Config & Active Session
    private val _quizConfig = MutableStateFlow(QuizConfigState())
    val quizConfig: StateFlow<QuizConfigState> = _quizConfig.asStateFlow()

    private val _quizSession = MutableStateFlow(ActiveQuizSession())
    val quizSession: StateFlow<ActiveQuizSession> = _quizSession.asStateFlow()

    // Audit Filter State
    private val _auditFilter = MutableStateFlow("all") // "all", "conflict", "duplicate", "missing_exp"
    val auditFilter: StateFlow<String> = _auditFilter.asStateFlow()

    // Settings Sub-Category selection (for Tablet Master-Detail Settings)
    private val _selectedSettingsCategory = MutableStateFlow("appearance")
    val selectedSettingsCategory: StateFlow<String> = _selectedSettingsCategory.asStateFlow()

    private var quizTimerJob: Job? = null

    init {
        viewModelScope.launch {
            val existingBanks = dao.getAllBanks()
            if (existingBanks.isEmpty()) {
                dao.upsertBanks(SeedDataFactory.initialBanks())
                dao.upsertQuestions(SeedDataFactory.initialQuestions())
                dao.insertPracticeRecords(SeedDataFactory.initialPracticeRecords())
                SeedDataFactory.initialPresets().forEach { dao.insertImportPreset(it) }
                SeedDataFactory.initialImportLogs().forEach { dao.insertImportLog(it) }
            }
            loadDemoImportData(navigateToStep2 = false)
        }
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _toastMessage.value = message
            delay(2800)
            if (_toastMessage.value == message) {
                _toastMessage.value = null
            }
        }
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    // Navigation Logic
    fun selectPrimaryTab(tab: PrimaryTab) {
        backStack.clear()
        _activePrimaryTab.value = tab
        _currentScreen.value = when (tab) {
            PrimaryTab.HOME -> AppScreen.HOME
            PrimaryTab.LIBRARY -> AppScreen.LIBRARY
            PrimaryTab.REVIEW -> AppScreen.REVIEW
            PrimaryTab.STATS -> AppScreen.STATS
        }
    }

    fun navigateTo(screen: AppScreen) {
        val current = _currentScreen.value
        if (current != screen) {
            backStack.addLast(current)
            _currentScreen.value = screen
            when (screen) {
                AppScreen.HOME -> _activePrimaryTab.value = PrimaryTab.HOME
                AppScreen.LIBRARY -> _activePrimaryTab.value = PrimaryTab.LIBRARY
                AppScreen.REVIEW -> _activePrimaryTab.value = PrimaryTab.REVIEW
                AppScreen.STATS -> _activePrimaryTab.value = PrimaryTab.STATS
                else -> {}
            }
        }
    }

    fun navigateBack(): Boolean {
        if (_currentScreen.value == AppScreen.QUIZ) {
            quizTimerJob?.cancel()
        }
        if (backStack.isNotEmpty()) {
            val prev = backStack.removeLast()
            _currentScreen.value = prev
            when (prev) {
                AppScreen.HOME -> _activePrimaryTab.value = PrimaryTab.HOME
                AppScreen.LIBRARY -> _activePrimaryTab.value = PrimaryTab.LIBRARY
                AppScreen.REVIEW -> _activePrimaryTab.value = PrimaryTab.REVIEW
                AppScreen.STATS -> _activePrimaryTab.value = PrimaryTab.STATS
                else -> {}
            }
            return true
        }
        if (_currentScreen.value != AppScreen.HOME) {
            selectPrimaryTab(PrimaryTab.HOME)
            return true
        }
        return false
    }

    // Question Bank & Question List Operations
    fun openBankDetail(bankName: String) {
        _selectedBankName.value = bankName
        _selectedChapterFilter.value = null
        navigateTo(AppScreen.BOOK_DETAIL)
    }

    fun openQuestionList(bankName: String = _selectedBankName.value, chapter: String? = null, filter: String = "all") {
        _selectedBankName.value = bankName
        _selectedChapterFilter.value = chapter
        _questionListFilter.value = filter
        _isBatchMode.value = false
        _batchSelectedIds.value = emptySet()
        navigateTo(AppScreen.QUESTION_LIST)
    }

    fun setLibrarySearch(query: String) {
        _librarySearchQuery.value = query
    }

    fun setQuestionListSearch(query: String) {
        _questionListSearchQuery.value = query
    }

    fun setQuestionListFilter(filter: String) {
        _questionListFilter.value = filter
    }

    fun setChapterFilter(chapter: String?) {
        _selectedChapterFilter.value = chapter
    }

    fun toggleBatchMode() {
        _isBatchMode.value = !_isBatchMode.value
        if (!_isBatchMode.value) {
            _batchSelectedIds.value = emptySet()
        }
    }

    fun toggleBatchSelection(questionId: String) {
        _batchSelectedIds.update { current ->
            if (questionId in current) current - questionId else current + questionId
        }
    }

    fun selectAllBatchQuestions(ids: List<String>) {
        _batchSelectedIds.value = ids.toSet()
    }

    fun batchDeleteSelected() {
        val ids = _batchSelectedIds.value.toList()
        if (ids.isEmpty()) return
        viewModelScope.launch {
            dao.deleteQuestionsByIds(ids)
            _batchSelectedIds.value = emptySet()
            _isBatchMode.value = false
            showToast("已批量删除 ${ids.size} 道题目")
        }
    }

    fun batchUpdateSelectedChapter(newChapter: String) {
        val ids = _batchSelectedIds.value.toList()
        if (ids.isEmpty() || newChapter.isBlank()) return
        viewModelScope.launch {
            dao.batchUpdateChapter(ids, newChapter.trim())
            _batchSelectedIds.value = emptySet()
            _isBatchMode.value = false
            showToast("已将 ${ids.size} 道题目移至「$newChapter」")
        }
    }

    fun batchUpdateSelectedDifficulty(newDiff: String) {
        val ids = _batchSelectedIds.value.toList()
        if (ids.isEmpty()) return
        viewModelScope.launch {
            dao.batchUpdateDifficulty(ids, newDiff)
            _batchSelectedIds.value = emptySet()
            _isBatchMode.value = false
            showToast("已将 ${ids.size} 道题目难度设为「$newDiff」")
        }
    }

    fun createNewBank(name: String, subtitle: String, domainTag: String = "railway") {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            showToast("请输入题库名称")
            return
        }
        val formattedName = if (trimmed.startsWith("《") && trimmed.endsWith("》")) trimmed else "《$trimmed》"
        viewModelScope.launch {
            dao.upsertBank(
                QuestionBankEntity(
                    name = formattedName,
                    subtitle = subtitle.trim().ifBlank { "自定义专业题库 · 本地存储" },
                    domainTag = domainTag,
                    totalTargetCount = 50,
                    practicedCount = 0,
                    accuracyPercent = 0
                )
            )
            _selectedBankName.value = formattedName
            showToast("已创建题库 $formattedName")
        }
    }

    fun renameBank(oldName: String, newName: String) {
        val formatted = if (newName.startsWith("《")) newName else "《${newName.trim()}》"
        if (formatted.isBlank() || formatted == oldName) return
        viewModelScope.launch {
            val all = dao.getAllBanks()
            val target = all.find { it.name == oldName } ?: return@launch
            dao.upsertBank(target.copy(name = formatted, updatedAt = System.currentTimeMillis()))
            dao.moveQuestionsBank(oldName, formatted)
            dao.updateRecordsBankName(oldName, formatted)
            dao.updateImportLogsBankName(oldName, formatted)
            dao.deleteBankByName(oldName)
            _selectedBankName.value = formatted
            showToast("题库已重命名为 $formatted")
        }
    }

    fun deleteBank(bankName: String) {
        viewModelScope.launch {
            dao.deleteQuestionsByBank(bankName)
            dao.deleteBankByName(bankName)
            val remaining = dao.getAllBanks()
            _selectedBankName.value = remaining.firstOrNull()?.name ?: "《铁路运输组织基础》"
            selectPrimaryTab(PrimaryTab.LIBRARY)
            showToast("已删除题库 $bankName")
        }
    }

    // Question Editor Operations
    fun openQuestionEditor(question: QuestionEntity? = null) {
        val target = question ?: QuestionEntity(
            id = "Q${(allQuestions.value.size + 1).toString().padStart(3, '0')}",
            bank = _selectedBankName.value,
            chapter = _selectedChapterFilter.value ?: "第一章：车站工作组织",
            section = "核心考点",
            type = "single",
            difficulty = "中等",
            score = 2.0,
            stem = "",
            optionsJson = listToJson(
                listOf(
                    "A. 请输入选项 A 内容",
                    "B. 请输入选项 B 内容",
                    "C. 请输入选项 C 内容",
                    "D. 请输入选项 D 内容"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "",
            trapAnalysis = "",
            mnemonic = ""
        )
        _editingQuestion.value = target
        navigateTo(AppScreen.QUESTION_EDITOR)
    }

    fun saveEditedQuestion(updated: QuestionEntity, returnBack: Boolean = true) {
        if (updated.stem.isBlank()) {
            showToast("请填写题干内容后再保存")
            return
        }
        viewModelScope.launch {
            dao.upsertQuestion(updated.copy(updatedAt = System.currentTimeMillis()))
            _editingQuestion.value = updated
            showToast("题目 ${updated.id} 已保存入库")
            if (returnBack) {
                navigateBack()
            }
        }
    }

    fun toggleQuestionStar(questionId: String) {
        viewModelScope.launch {
            val q = allQuestions.value.find { it.id == questionId } ?: return@launch
            val updated = q.copy(isStarred = !q.isStarred)
            dao.updateQuestion(updated)
            // Also update in active quiz session if present
            _quizSession.update { session ->
                session.copy(
                    questions = session.questions.map { if (it.id == questionId) updated else it }
                )
            }
            showToast(if (updated.isStarred) "已加入收藏夹 ★" else "已取消收藏")
        }
    }

    fun toggleQuestionFlag(questionId: String) {
        viewModelScope.launch {
            val q = allQuestions.value.find { it.id == questionId } ?: return@launch
            val updated = q.copy(isFlagged = !q.isFlagged)
            dao.updateQuestion(updated)
            _quizSession.update { session ->
                session.copy(
                    questions = session.questions.map { if (it.id == questionId) updated else it }
                )
            }
            showToast(if (updated.isFlagged) "已记入重点关注" else "已取消标记")
        }
    }

    fun markQuestionMastered(questionId: String) {
        viewModelScope.launch {
            val q = allQuestions.value.find { it.id == questionId } ?: return@launch
            val sm2 = com.example.data.scheduler.Sm2Scheduler.calculate(
                repetitions = q.repetitions + 1,
                intervalDays = q.intervalDays,
                easeFactor = q.easeFactor,
                quality = com.example.data.scheduler.ReviewQuality.EASY
            )
            val updated = q.copy(
                wrongCount = 0,
                masteryPercent = 98,
                repetitions = sm2.repetitions,
                intervalDays = sm2.intervalDays,
                easeFactor = sm2.easeFactor,
                nextReviewTimestamp = sm2.nextReviewTimestamp,
                lastPracticedLabel = "刚刚学会"
            )
            dao.updateQuestion(updated)
            showToast("已将复习调度至 ${sm2.intervalDays} 天后")
        }
    }

    fun saveQuestionNote(questionId: String, note: String) {
        viewModelScope.launch {
            val q = allQuestions.value.find { it.id == questionId } ?: return@launch
            val updated = q.copy(mnemonic = note.trim(), updatedAt = System.currentTimeMillis())
            dao.updateQuestion(updated)
            _quizSession.update { session ->
                session.copy(
                    questions = session.questions.map { if (it.id == questionId) updated else it }
                )
            }
            showToast("随手笔记已保存")
        }
    }

    fun startQuickFiveSprint(bankName: String = _selectedBankName.value) {
        val all = allQuestions.value
        val bankQuestions = all.filter { it.bank == bankName }
        if (bankQuestions.isEmpty()) {
            showToast("该题库暂无题目，请先添加题目")
            return
        }
        val list = bankQuestions.shuffled().take(5)
        launchQuizSession(
            title = "${bankName.removeSurrounding("《", "》")} · 速练 5 题",
            mode = "随机练习",
            questions = list
        )
    }

    // Review Screen Operations
    fun setReviewFilter(filter: String) {
        _reviewFilter.value = filter
    }

    fun setReviewSearch(query: String) {
        _reviewSearchQuery.value = query
    }

    fun setStatsRange(range: String) {
        _statsRange.value = range
    }

    // Smart Import 4-Step Pipeline Operations
    fun loadDemoImportData(
        fileName: String = "2026铁路运输模拟题.xlsx",
        navigateToStep2: Boolean = true
    ) {
        viewModelScope.launch {
            val (headers, rows) = SmartImportEngine.getDemoExcelRows()
            val currentQuestions = dao.getAllQuestionsOnce()
            val mapping = ColumnMappingConfig()
            val rules = _importState.value.rules
            val evaluated = SmartImportEngine.evaluateImportRows(rows, mapping, rules, currentQuestions)
            _importState.update {
                it.copy(
                    fileName = fileName,
                    fileSizeLabel = "48.6 KB · UTF-8 · 8 行记录",
                    targetBankName = _selectedBankName.value,
                    headers = headers,
                    rawRows = rows,
                    mapping = mapping,
                    evaluatedItems = evaluated
                )
            }
            if (navigateToStep2) {
                navigateTo(AppScreen.IMPORT_MAPPING)
            }
        }
    }

    fun loadImportedFileOrText(fileName: String, content: String, targetBank: String = _selectedBankName.value) {
        viewModelScope.launch {
            val (headers, rows) = SmartImportEngine.parseRawTextContent(fileName, content)
            val currentQuestions = dao.getAllQuestionsOnce()
            val mapping = ColumnMappingConfig(
                stemCol = headers.getOrNull(0) ?: "题干",
                typeCol = headers.getOrNull(1) ?: "题型",
                optionACol = headers.getOrNull(2) ?: "选项A",
                optionBCol = headers.getOrNull(3) ?: "选项B",
                optionCCol = headers.getOrNull(4) ?: "选项C",
                optionDCol = headers.getOrNull(5) ?: "选项D",
                answerCol = headers.getOrNull(6) ?: "答案",
                explanationCol = headers.getOrNull(7) ?: "解析",
                difficultyCol = headers.getOrNull(8) ?: "难度",
                chapterCol = headers.getOrNull(9) ?: "章节"
            )
            val rules = _importState.value.rules
            val evaluated = SmartImportEngine.evaluateImportRows(rows, mapping, rules, currentQuestions)
            _importState.update {
                it.copy(
                    fileName = fileName,
                    fileSizeLabel = "${(content.length / 1024.0).coerceAtLeast(1.2).toString().take(4)} KB · UTF-8 · ${rows.size} 行记录",
                    targetBankName = targetBank,
                    headers = headers,
                    rawRows = rows,
                    mapping = mapping,
                    evaluatedItems = evaluated
                )
            }
            showToast("成功解析 $fileName，识别到 ${evaluated.size} 道候选题目")
            navigateTo(AppScreen.IMPORT_MAPPING)
        }
    }

    fun loadImportedXlsxStream(fileName: String, inputStream: java.io.InputStream, targetBank: String = _selectedBankName.value) {
        viewModelScope.launch {
            val currentQuestions = dao.getAllQuestionsOnce()
            val (headers, rows) = SmartImportEngine.parseXlsxStream(inputStream)
            val mapping = ColumnMappingConfig(
                stemCol = headers.find { it.contains("题干") || it.contains("题目") } ?: headers.getOrNull(0) ?: "题干",
                typeCol = headers.find { it.contains("题型") || it.contains("类型") } ?: headers.getOrNull(1) ?: "题型",
                optionACol = headers.find { it.contains("选项A") || it.contains("A") } ?: headers.getOrNull(2) ?: "选项A",
                optionBCol = headers.find { it.contains("选项B") || it.contains("B") } ?: headers.getOrNull(3) ?: "选项B",
                optionCCol = headers.find { it.contains("选项C") || it.contains("C") } ?: headers.getOrNull(4) ?: "选项C",
                optionDCol = headers.find { it.contains("选项D") || it.contains("D") } ?: headers.getOrNull(5) ?: "选项D",
                answerCol = headers.find { it.contains("答案") } ?: headers.getOrNull(6) ?: "答案",
                explanationCol = headers.find { it.contains("解析") } ?: headers.getOrNull(7) ?: "解析",
                difficultyCol = headers.find { it.contains("难度") } ?: headers.getOrNull(8) ?: "难度",
                chapterCol = headers.find { it.contains("章节") } ?: headers.getOrNull(9) ?: "章节"
            )
            val rules = _importState.value.rules
            val evaluated = SmartImportEngine.evaluateImportRows(rows, mapping, rules, currentQuestions)
            _importState.update {
                it.copy(
                    fileName = fileName,
                    fileSizeLabel = "Excel (.xlsx) · ${rows.size} 行有效记录",
                    targetBankName = targetBank,
                    headers = headers,
                    rawRows = rows,
                    mapping = mapping,
                    evaluatedItems = evaluated
                )
            }
            showToast("成功解包 $fileName，识别到 ${evaluated.size} 道题目")
            navigateTo(AppScreen.IMPORT_MAPPING)
        }
    }

    fun loadImportedDocxStream(fileName: String, inputStream: java.io.InputStream, targetBank: String = _selectedBankName.value) {
        viewModelScope.launch {
            val currentQuestions = dao.getAllQuestionsOnce()
            val (headers, rows) = SmartImportEngine.parseDocxStream(inputStream)
            val mapping = ColumnMappingConfig()
            val rules = _importState.value.rules
            val evaluated = SmartImportEngine.evaluateImportRows(rows, mapping, rules, currentQuestions)
            _importState.update {
                it.copy(
                    fileName = fileName,
                    fileSizeLabel = "Word (.docx) · ${rows.size} 道解析题目",
                    targetBankName = targetBank,
                    headers = headers,
                    rawRows = rows,
                    mapping = mapping,
                    evaluatedItems = evaluated
                )
            }
            showToast("成功解包 $fileName，识别到 ${evaluated.size} 道试卷题目")
            navigateTo(AppScreen.IMPORT_MAPPING)
        }
    }

    fun updateImportTargetBank(bankName: String) {
        _importState.update { it.copy(targetBankName = bankName) }
    }

    fun updateImportMapping(newMapping: ColumnMappingConfig) {
        viewModelScope.launch {
            val currentQuestions = dao.getAllQuestionsOnce()
            val evaluated = SmartImportEngine.evaluateImportRows(
                _importState.value.rawRows,
                newMapping,
                _importState.value.rules,
                currentQuestions
            )
            _importState.update {
                it.copy(mapping = newMapping, evaluatedItems = evaluated)
            }
        }
    }

    fun updateImportRules(newRules: PreprocessRulesConfig) {
        viewModelScope.launch {
            val currentQuestions = dao.getAllQuestionsOnce()
            val evaluated = SmartImportEngine.evaluateImportRows(
                _importState.value.rawRows,
                _importState.value.mapping,
                newRules,
                currentQuestions
            )
            _importState.update {
                it.copy(rules = newRules, evaluatedItems = evaluated)
            }
        }
    }

    fun saveImportPreset(presetName: String) {
        val rules = _importState.value.rules
        viewModelScope.launch {
            dao.insertImportPreset(
                ImportPresetEntity(
                    name = presetName.ifBlank { "自定义清洗预设" },
                    customSerialRegex = rules.customSerialRegex,
                    multiChoiceDelimiter = rules.multiChoiceDelimiter,
                    removeSerial = rules.removeSerial,
                    normalizeAnswer = rules.normalizeAnswer,
                    inferType = rules.inferType,
                    mergeLines = rules.mergeLines,
                    skipEmpty = rules.skipEmpty
                )
            )
            showToast("已保存清洗规则预设「$presetName」")
        }
    }

    fun applyImportPreset(preset: ImportPresetEntity) {
        updateImportRules(
            PreprocessRulesConfig(
                removeSerial = preset.removeSerial,
                normalizeAnswer = preset.normalizeAnswer,
                inferType = preset.inferType,
                mergeLines = preset.mergeLines,
                skipEmpty = preset.skipEmpty,
                customSerialRegex = preset.customSerialRegex,
                multiChoiceDelimiter = preset.multiChoiceDelimiter
            )
        )
        showToast("已加载预设「${preset.name}」")
    }

    fun setImportPreviewFilter(filter: String) {
        _importState.update { it.copy(previewFilter = filter) }
    }

    fun setDefaultDuplicateStrategy(strategy: String) {
        _importState.update { it.copy(defaultDuplicateStrategy = strategy) }
    }

    fun updateImportItemResolution(itemId: String, resolution: String) {
        _importState.update { state ->
            state.copy(
                evaluatedItems = state.evaluatedItems.map { item ->
                    if (item.id == itemId) item.copy(resolutionAction = resolution) else item
                }
            )
        }
    }

    fun fixImportItemInline(
        itemId: String,
        newStem: String,
        newAnswerText: String,
        newOptions: List<String>,
        newExplanation: String
    ) {
        _importState.update { state ->
            val updatedItems = state.evaluatedItems.map { item ->
                if (item.id != itemId) return@map item
                val tokens = newAnswerText.uppercase()
                    .split(Regex("[,，、;；\\s]+"))
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                val definedLetters = newOptions.map { it.take(1).uppercase() }.toSet()
                val invalidLetters = if (item.type in listOf("single", "multiple")) {
                    tokens.filter { it !in definedLetters }
                } else emptyList()

                val isNowValid = newStem.isNotBlank() && tokens.isNotEmpty() && invalidLetters.isEmpty()
                item.copy(
                    stem = newStem,
                    options = newOptions,
                    answer = tokens,
                    rawAnswerText = tokens.joinToString(", "),
                    explanation = newExplanation,
                    status = if (isNowValid) "valid" else "invalid",
                    statusMessage = if (isNowValid) {
                        "已人工修正通过 (正确答案: ${tokens.joinToString(", ")})"
                    } else {
                        "答案仍包含未定义选项: ${invalidLetters.joinToString(",")}"
                    },
                    invalidDetail = ""
                )
            }
            state.copy(evaluatedItems = updatedItems)
        }
        showToast("已更新并重新校验该题")
    }

    fun discardImportItem(itemId: String) {
        _importState.update { state ->
            state.copy(evaluatedItems = state.evaluatedItems.filterNot { it.id == itemId })
        }
        showToast("已从本次导入队列中移除该题")
    }

    fun commitSmartImport() {
        viewModelScope.launch {
            val state = _importState.value
            val targetBank = state.targetBankName
            val existingAll = dao.getAllQuestionsOnce()
            var nextNum = existingAll.size + 1

            var added = 0
            var replaced = 0
            var skipped = 0
            var invalidSkipped = 0

            state.evaluatedItems.forEach { item ->
                if (item.resolutionAction == "discard" || item.status == "invalid") {
                    invalidSkipped++
                    return@forEach
                }

                val effectiveAction = if (item.resolutionAction != "default") {
                    item.resolutionAction
                } else {
                    state.defaultDuplicateStrategy
                }

                when (item.status) {
                    "valid" -> {
                        val newQ = QuestionEntity(
                            id = "Q${(nextNum++).toString().padStart(3, '0')}",
                            bank = targetBank,
                            chapter = item.chapter,
                            type = item.type,
                            difficulty = item.difficulty,
                            stem = item.stem,
                            optionsJson = listToJson(item.options),
                            answersJson = listToJson(item.answer),
                            explanation = item.explanation.ifBlank { "智能导入标准解析" }
                        )
                        dao.upsertQuestion(newQ)
                        added++
                    }
                    "conflict", "duplicate" -> {
                        when (effectiveAction) {
                            "skip" -> skipped++
                            "replace" -> {
                                val old = item.existingQuestion
                                if (old != null) {
                                    dao.upsertQuestion(
                                        old.copy(
                                            stem = item.stem,
                                            optionsJson = listToJson(item.options),
                                            answersJson = listToJson(item.answer),
                                            explanation = item.explanation.ifBlank { old.explanation },
                                            updatedAt = System.currentTimeMillis()
                                        )
                                    )
                                    replaced++
                                }
                            }
                            "keep" -> {
                                val newQ = QuestionEntity(
                                    id = "Q${(nextNum++).toString().padStart(3, '0')}",
                                    bank = targetBank,
                                    chapter = item.chapter,
                                    type = item.type,
                                    difficulty = item.difficulty,
                                    stem = item.stem,
                                    optionsJson = listToJson(item.options),
                                    answersJson = listToJson(item.answer),
                                    explanation = item.explanation.ifBlank { "保留新版本导入" }
                                )
                                dao.upsertQuestion(newQ)
                                added++
                            }
                        }
                    }
                }
            }

            dao.insertImportLog(
                ImportLogEntity(
                    fileName = state.fileName,
                    targetBank = targetBank,
                    addedCount = added,
                    replacedCount = replaced,
                    skippedCount = skipped,
                    invalidCount = invalidSkipped,
                    dateLabel = "刚刚"
                )
            )

            _importState.update {
                it.copy(
                    lastCommitStats = CommitStats(
                        added = added,
                        replaced = replaced,
                        skipped = skipped,
                        invalidSkipped = invalidSkipped,
                        targetBank = targetBank
                    )
                )
            }
            _selectedBankName.value = targetBank
            navigateTo(AppScreen.IMPORT_RESULT)
        }
    }

    // Practice & Quiz Engine Operations
    fun openPracticeConfig(bankName: String = _selectedBankName.value, presetMode: String = "顺序练习", presetRange: String = "全部题目") {
        val currentSettings = settings.value
        _quizConfig.value = QuizConfigState(
            bankName = bankName,
            mode = presetMode,
            range = presetRange,
            count = 10,
            instantExplanation = currentSettings.instantExplanation,
            shuffleOptions = currentSettings.shuffleOptions
        )
        navigateTo(AppScreen.PRACTICE_CONFIG)
    }

    fun updateQuizConfig(updater: (QuizConfigState) -> QuizConfigState) {
        _quizConfig.update(updater)
    }

    fun startQuizFromConfig() {
        val cfg = _quizConfig.value
        val all = allQuestions.value
        val bankQuestions = all.filter { it.bank == cfg.bankName }
        if (bankQuestions.isEmpty()) {
            showToast("题库中暂无可练习题目，请先录入或导入题目")
            return
        }
        val filtered = when (cfg.range) {
            "错题" -> bankQuestions.filter { it.wrongCount > 0 }.ifEmpty {
                showToast("该题库暂无错题，已为你切换为全部题目")
                bankQuestions
            }
            "收藏" -> bankQuestions.filter { it.isStarred }.ifEmpty {
                showToast("该题库暂无重点收藏，已为你切换为全部题目")
                bankQuestions
            }
            else -> bankQuestions
        }
        val ordered = if (cfg.mode == "随机练习" || cfg.mode == "模拟考试") {
            filtered.shuffled()
        } else {
            filtered
        }
        val selectedList = ordered.take(cfg.count.coerceAtLeast(1))
        launchQuizSession(
            title = "${cfg.bankName} · ${cfg.mode}",
            mode = cfg.mode,
            questions = selectedList
        )
    }

    fun startQuickReviewSession(singleQuestion: QuestionEntity? = null) {
        val all = allQuestions.value
        val list = if (singleQuestion != null) {
            listOf(singleQuestion) + all.filter { it.wrongCount > 0 && it.id != singleQuestion.id }
        } else {
            all.filter { it.wrongCount > 0 || it.isStarred }.ifEmpty { all }
        }
        launchQuizSession(
            title = "艾宾浩斯错题与重点巩固",
            mode = "错题复习",
            questions = list.take(8)
        )
    }

    fun launchQuizSession(title: String, mode: String, questions: List<QuestionEntity>) {
        if (questions.isEmpty()) {
            showToast("当前筛选条件下暂无题目")
            return
        }
        val preSubmitted = if (mode == "背题模式") {
            questions.associate { it.id to true }
        } else {
            emptyMap()
        }
        val preSelections = if (mode == "背题模式") {
            questions.associate { it.id to it.answer }
        } else {
            emptyMap()
        }

        _quizSession.value = ActiveQuizSession(
            title = title,
            mode = mode,
            questions = questions,
            currentIndex = 0,
            userSelections = preSelections,
            submittedMap = preSubmitted,
            elapsedSeconds = 0
        )
        startQuizTimer()
        navigateTo(AppScreen.QUIZ)
    }

    private fun startQuizTimer() {
        quizTimerJob?.cancel()
        quizTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_currentScreen.value == AppScreen.QUIZ) {
                    _quizSession.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
                }
            }
        }
    }

    fun selectQuizOption(optionCode: String) {
        val session = _quizSession.value
        val currentQ = session.questions.getOrNull(session.currentIndex) ?: return
        if (session.submittedMap[currentQ.id] == true && session.mode != "模拟考试") return

        val currentSelected = session.userSelections[currentQ.id] ?: emptyList()
        val newSelected = when (currentQ.type) {
            "multiple" -> {
                if (optionCode in currentSelected) currentSelected - optionCode else (currentSelected + optionCode).sorted()
            }
            else -> listOf(optionCode)
        }

        _quizSession.update {
            it.copy(userSelections = it.userSelections + (currentQ.id to newSelected))
        }

        // For Single Choice or Judge in instant mode, auto-submit immediately
        if (currentQ.type in listOf("single", "judge") && _quizConfig.value.instantExplanation && session.mode != "模拟考试") {
            submitCurrentQuestion()
        }
    }

    fun updateFillOrCalcInput(slotIndex: Int, text: String) {
        val session = _quizSession.value
        val currentQ = session.questions.getOrNull(session.currentIndex) ?: return
        val slotCount = currentQ.answer.size.coerceAtLeast(1)
        val existing = session.userSelections[currentQ.id] ?: List(slotCount) { "" }
        val updated = existing.toMutableList().apply {
            while (size <= slotIndex) add("")
            this[slotIndex] = text
        }
        _quizSession.update {
            it.copy(userSelections = it.userSelections + (currentQ.id to updated))
        }
    }

    fun submitCurrentQuestion() {
        val session = _quizSession.value
        val currentQ = session.questions.getOrNull(session.currentIndex) ?: return
        val userAns = session.userSelections[currentQ.id] ?: emptyList()
        if (userAns.isEmpty() || userAns.all { it.isBlank() }) {
            showToast("请先选择或填写答案")
            return
        }

        val isCorrect = evaluateQuestionCorrectness(currentQ, userAns)
        _quizSession.update {
            it.copy(submittedMap = it.submittedMap + (currentQ.id to true))
        }

        viewModelScope.launch {
            val quality = if (isCorrect) com.example.data.scheduler.ReviewQuality.GOOD else com.example.data.scheduler.ReviewQuality.AGAIN
            val sm2 = com.example.data.scheduler.Sm2Scheduler.calculate(
                repetitions = currentQ.repetitions,
                intervalDays = currentQ.intervalDays,
                easeFactor = currentQ.easeFactor,
                quality = quality
            )
            val updatedQ = if (isCorrect) {
                currentQ.copy(
                    masteryPercent = (currentQ.masteryPercent + 8).coerceAtMost(99),
                    repetitions = sm2.repetitions,
                    intervalDays = sm2.intervalDays,
                    easeFactor = sm2.easeFactor,
                    nextReviewTimestamp = sm2.nextReviewTimestamp,
                    lastPracticedLabel = "刚刚"
                )
            } else {
                currentQ.copy(
                    wrongCount = currentQ.wrongCount + 1,
                    masteryPercent = (currentQ.masteryPercent - 10).coerceAtLeast(20),
                    repetitions = sm2.repetitions,
                    intervalDays = sm2.intervalDays,
                    easeFactor = sm2.easeFactor,
                    nextReviewTimestamp = sm2.nextReviewTimestamp,
                    lastPracticedLabel = "刚刚",
                    lastWrongAnswer = userAns.joinToString(",")
                )
            }
            dao.updateQuestion(updatedQ)
            prefsRepo.incrementDailyDone(1)

            if (isCorrect && settings.value.autoNextOnCorrect && session.currentIndex < session.questions.lastIndex) {
                delay(550)
                jumpToQuestion(session.currentIndex + 1)
            }
        }
    }

    fun evaluateQuestionCorrectness(q: QuestionEntity, userAns: List<String>): Boolean {
        if (userAns.isEmpty()) return false
        return when (q.type) {
            "fill", "calc" -> {
                q.answer.indices.all { idx ->
                    val expected = q.answer[idx].trim()
                    val actual = userAns.getOrNull(idx)?.trim() ?: ""
                    actual.equals(expected, ignoreCase = true) ||
                        (expected == "有效度系数" && actual == "有效利用系数")
                }
            }
            else -> {
                userAns.map { it.trim().uppercase() }.sorted() == q.answer.map { it.trim().uppercase() }.sorted()
            }
        }
    }

    fun jumpToQuestion(index: Int) {
        val session = _quizSession.value
        if (index in session.questions.indices) {
            _quizSession.update { it.copy(currentIndex = index, showAnswerSheet = false) }
        }
    }

    fun toggleAnswerSheet(show: Boolean? = null) {
        _quizSession.update {
            it.copy(showAnswerSheet = show ?: !it.showAnswerSheet)
        }
    }

    fun toggleAiDeepExplain(questionId: String) {
        _quizSession.update { session ->
            val current = session.aiDeepExpandedMap[questionId] ?: false
            session.copy(aiDeepExpandedMap = session.aiDeepExpandedMap + (questionId to !current))
        }
        viewModelScope.launch {
            prefsRepo.updateAiConfig(addTokens = 160)
        }
    }

    fun generateAiVariantQuestion(question: QuestionEntity) {
        val s = settings.value
        val hasRealApiKey = s.aiApiKeyMasked.isNotBlank() && !s.aiApiKeyMasked.contains("••••")
        viewModelScope.launch {
            if (hasRealApiKey) {
                showToast("正在通过 ${s.aiModelId} 衍生试题...")
                val res = com.example.data.remote.AiApiClient.requestVariantQuestion(
                    baseUrl = s.aiBaseUrl,
                    apiKey = s.aiApiKeyMasked,
                    modelId = s.aiModelId,
                    question = question
                )
                if (res.isSuccess) {
                    val variant = res.getOrThrow()
                    _quizSession.update { session ->
                        session.copy(generatedVariantMap = session.generatedVariantMap + (question.id to variant))
                    }
                    prefsRepo.updateAiConfig(addTokens = 380)
                    showToast("AI 衍生题已就绪")
                    return@launch
                }
            }

            // Local fallback template
            val variant = QuestionEntity(
                id = "${question.id}_VAR",
                bank = question.bank,
                chapter = question.chapter,
                section = "${question.section} · 举一反三",
                type = "single",
                difficulty = question.difficulty,
                score = 2.0,
                stem = "【衍生题】针对「${question.section}」考点：当现场作业条件发生变异时，下列何种处置符合规程？",
                optionsJson = listToJson(
                    listOf(
                        "A. 严格依照规章核准行车凭证，确保进路绝不冲突",
                        "B. 为抢抓周转效率，擅自临时解除设备联锁",
                        "C. 简化道岔锁闭核验流程，先放行后补填记",
                        "D. 仅凭经验口头联控，省略凭证交接"
                    )
                ),
                answersJson = listToJson(listOf("A")),
                explanation = "【核心要领】安全永远处于最高优先级，凭证手续必须齐备合规。"
            )
            _quizSession.update { session ->
                session.copy(generatedVariantMap = session.generatedVariantMap + (question.id to variant))
            }
            prefsRepo.updateAiConfig(addTokens = 150)
            showToast("已衍生一道同类考题")
        }
    }

    fun finishQuizSession() {
        quizTimerJob?.cancel()
        val session = _quizSession.value
        val total = session.questions.size.coerceAtLeast(1)
        val correct = session.questions.count { q ->
            val userAns = session.userSelections[q.id] ?: emptyList()
            evaluateQuestionCorrectness(q, userAns)
        }
        val scorePercent = ((correct * 100.0) / total).toInt()

        // Mark all questions as submitted so Result & Recap screens can inspect everything
        val allSubmitted = session.questions.associate { it.id to true }
        _quizSession.update { it.copy(submittedMap = allSubmitted, showAnswerSheet = false) }

        viewModelScope.launch {
            dao.insertPracticeRecord(
                PracticeRecordEntity(
                    title = session.title,
                    bankName = _selectedBankName.value,
                    modeLabel = session.mode,
                    dateLabel = "刚刚",
                    scorePercent = scorePercent,
                    totalCount = total,
                    correctCount = correct,
                    durationSec = session.elapsedSeconds.coerceAtLeast(15)
                )
            )
            navigateTo(AppScreen.QUIZ_RESULT)
        }
    }

    // Full Library Audit & Conflict Resolution Operations
    fun setAuditFilter(filter: String) {
        _auditFilter.value = filter
    }

    fun computeCrossBankConflicts(questions: List<QuestionEntity>): List<CrossBankConflictPair> {
        val grouped = questions
            .filter { it.auditStatus != "manual_verified" }
            .groupBy { it.stem.replace(Regex("[\\s，。？、！,.?!]"), "") }
        val pairs = mutableListOf<CrossBankConflictPair>()
        grouped.values.forEach { list ->
            if (list.size >= 2) {
                val first = list[0]
                val second = list[1]
                val sameAns = first.answer == second.answer
                pairs.add(
                    CrossBankConflictPair(
                        id = "${first.id}_${second.id}",
                        stem = first.stem,
                        questionA = first,
                        questionB = second,
                        isSameAnswerDuplicate = sameAns
                    )
                )
            }
        }
        return pairs
    }

    fun resolveConflictOverwriteWithA(pair: CrossBankConflictPair, preferQuestionB: Boolean = false) {
        viewModelScope.launch {
            val winner = if (preferQuestionB) pair.questionB else pair.questionA
            val loser = if (preferQuestionB) pair.questionA else pair.questionB
            dao.updateQuestion(
                loser.copy(
                    optionsJson = winner.optionsJson,
                    answersJson = winner.answersJson,
                    explanation = winner.explanation,
                    auditStatus = "manual_verified",
                    updatedAt = System.currentTimeMillis()
                )
            )
            dao.updateQuestion(winner.copy(auditStatus = "manual_verified"))
            showToast("已统一按 ${winner.bank} 的答案（${winner.answer.joinToString(",")}）修正")
        }
    }

    fun resolveConflictKeepBothVerified(pair: CrossBankConflictPair) {
        viewModelScope.launch {
            dao.updateQuestion(pair.questionA.copy(auditStatus = "manual_verified"))
            dao.updateQuestion(pair.questionB.copy(auditStatus = "manual_verified"))
            showToast("已保留两道题各自的答案")
        }
    }

    fun mergeDuplicateQuestions(pair: CrossBankConflictPair) {
        viewModelScope.launch {
            val mergedMastery = maxOf(pair.questionA.masteryPercent, pair.questionB.masteryPercent)
            val mergedStar = pair.questionA.isStarred || pair.questionB.isStarred
            dao.updateQuestion(
                pair.questionA.copy(
                    masteryPercent = mergedMastery,
                    isStarred = mergedStar,
                    auditStatus = "manual_verified"
                )
            )
            dao.deleteQuestionById(pair.questionB.id)
            showToast("已合并重复题，只留一道")
        }
    }

    fun autoFillMissingExplanation(question: QuestionEntity) {
        viewModelScope.launch {
            val generatedExp = "这道题主要考「${question.chapter} · ${question.section}」。正确答案是 ${question.answer.joinToString(",")}，做题时抓准定义里的关键条件就能选对。"
            dao.updateQuestion(
                question.copy(
                    explanation = generatedExp,
                    mnemonic = question.mnemonic.ifBlank { "记住关键条件，先看前提再选答案" },
                    updatedAt = System.currentTimeMillis()
                )
            )
            prefsRepo.updateAiConfig(addTokens = 140)
            showToast("已为这道题补上解析")
        }
    }

    // Export / Backup & Restore JSON
    fun buildBankExportTsv(bankName: String): String {
        val list = allQuestions.value.filter { it.bank == bankName }
        val sb = StringBuilder()
        sb.append("#separator:tab\n")
        sb.append("#html:false\n")
        sb.append("#deck:${bankName.removeSurrounding("《", "》")}\n")
        list.forEach { q ->
            val front = if (q.options.isNotEmpty() && q.type != "judge") {
                "${q.stem}\n${q.options.joinToString("\n")}"
            } else {
                q.stem
            }
            val back = if (q.explanation.isNotBlank()) {
                "【参考答案】${q.answer.joinToString(", ")}\n【解析说明】${q.explanation}"
            } else {
                q.answer.joinToString(", ")
            }
            val tags = "${q.chapter} ${q.typeLabel}"
            sb.append("${front.replace("\t", " ")}\t${back.replace("\t", " ")}\t$tags\n")
        }
        return sb.toString()
    }

    fun buildWrongBookExportText(): String {
        val list = allQuestions.value.filter { it.wrongCount > 0 }
        val sb = StringBuilder()
        sb.append("知序 Study · 错题巩固打印集\n")
        sb.append("导出时间：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.CHINA).format(java.util.Date())}\n")
        sb.append("待巩固错题共 ${list.size} 道\n")
        sb.append("=========================================\n\n")

        list.forEachIndexed { index, q ->
            sb.append("${index + 1}. [${q.typeLabel}] ${q.stem}\n")
            if (q.options.isNotEmpty() && q.type != "judge") {
                q.options.forEach { opt -> sb.append("   $opt\n") }
            }
            sb.append("   [错题记录] 累计做错 ${q.wrongCount} 次 · 最近错选：${q.lastWrongAnswer.ifBlank { "未作答" }}\n\n")
        }

        sb.append("=========================================\n")
        sb.append("【参考答案与解析附录】\n")
        sb.append("=========================================\n\n")

        list.forEachIndexed { index, q ->
            sb.append("第 ${index + 1} 题答案：${q.answer.joinToString(", ")}\n")
            if (q.explanation.isNotBlank()) {
                sb.append("解析：${q.explanation}\n")
            }
            if (q.mnemonic.isNotBlank()) {
                sb.append("记忆技巧：${q.mnemonic}\n")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun buildFullBackupJson(): String {
        val root = JSONObject()
        root.put("app", "知序 Study")
        root.put("version", "1.2.0")
        root.put("exportedAt", System.currentTimeMillis())

        val banksArr = JSONArray()
        banks.value.forEach { b ->
            val obj = JSONObject()
            obj.put("name", b.name)
            obj.put("subtitle", b.subtitle)
            obj.put("domainTag", b.domainTag)
            obj.put("totalTargetCount", b.totalTargetCount)
            obj.put("practicedCount", b.practicedCount)
            obj.put("accuracyPercent", b.accuracyPercent)
            banksArr.put(obj)
        }
        root.put("banks", banksArr)

        val qArr = JSONArray()
        allQuestions.value.forEach { q ->
            val obj = JSONObject()
            obj.put("id", q.id)
            obj.put("bank", q.bank)
            obj.put("chapter", q.chapter)
            obj.put("section", q.section)
            obj.put("type", q.type)
            obj.put("difficulty", q.difficulty)
            obj.put("stem", q.stem)
            obj.put("options", JSONArray(q.optionsJson))
            obj.put("answer", JSONArray(q.answersJson))
            obj.put("explanation", q.explanation)
            obj.put("mnemonic", q.mnemonic)
            qArr.put(obj)
        }
        root.put("questions", qArr)
        return root.toString(2)
    }

    fun restoreFromBackupJson(jsonContent: String) {
        viewModelScope.launch {
            try {
                val root = JSONObject(jsonContent)
                val qArr = root.optJSONArray("questions")
                if (qArr != null && qArr.length() > 0) {
                    val restored = mutableListOf<QuestionEntity>()
                    for (i in 0 until qArr.length()) {
                        val o = qArr.getJSONObject(i)
                        restored.add(
                            QuestionEntity(
                                id = o.optString("id", "Q_RES_$i"),
                                bank = o.optString("bank", "《铁路运输组织基础》"),
                                chapter = o.optString("chapter", "恢复章节"),
                                section = o.optString("section", "核心考点"),
                                type = o.optString("type", "single"),
                                difficulty = o.optString("difficulty", "中等"),
                                stem = o.optString("stem", ""),
                                optionsJson = (o.optJSONArray("options") ?: JSONArray()).toString(),
                                answersJson = (o.optJSONArray("answer") ?: JSONArray()).toString(),
                                explanation = o.optString("explanation", ""),
                                mnemonic = o.optString("mnemonic", "")
                            )
                        )
                    }
                    dao.upsertQuestions(restored)
                    showToast("成功从备份恢复 ${restored.size} 道题目")
                } else {
                    showToast("备份文件格式有效，已完成增量同步")
                }
            } catch (e: Exception) {
                showToast("解析备份 JSON 失败，请检查文件格式")
            }
        }
    }

    fun resetToFactorySeed() {
        viewModelScope.launch {
            dao.upsertBanks(SeedDataFactory.initialBanks())
            dao.upsertQuestions(SeedDataFactory.initialQuestions())
            showToast("已恢复默认演示题库与跨库冲突样本")
        }
    }

    // Settings Delegations
    fun setSelectedSettingsCategory(cat: String) {
        _selectedSettingsCategory.value = cat
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            prefsRepo.updateThemeMode(mode)
            val label = when (mode) {
                "dark" -> "深色模式"
                "light" -> "浅色模式"
                else -> "跟随系统"
            }
            showToast("主题外观已切换为：$label")
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateDynamicColor(enabled)
            showToast(if (enabled) "已开启 Material You 动态取色" else "已恢复知序经典配色")
        }
    }

    fun setAccentPreset(preset: String) {
        viewModelScope.launch {
            prefsRepo.updateAccentPreset(preset)
            showToast("主题色彩预设已切换为：$preset")
        }
    }

    fun setFontScale(percent: Int) {
        viewModelScope.launch {
            prefsRepo.updateFontScale(percent)
        }
    }

    fun setLineSpacing(mode: String) {
        viewModelScope.launch {
            prefsRepo.updateLineSpacing(mode)
        }
    }

    fun setTabletSplitPreferred(enabled: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateTabletSplitPreferred(enabled)
            showToast(if (enabled) "已切换为分栏工作台视图" else "已切换为专注单栏视图")
        }
    }

    fun updateHabitSetting(
        instantExp: Boolean? = null,
        shuffleOpts: Boolean? = null,
        autoNext: Boolean? = null,
        haptic: Boolean? = null,
        hapticStrength: String? = null,
        sound: Boolean? = null
    ) {
        viewModelScope.launch {
            prefsRepo.updatePracticeHabit(instantExp, shuffleOpts, autoNext, haptic, hapticStrength, sound)
        }
    }

    fun updateGestureSettings(
        swipeSwitch: Boolean? = null,
        doubleTapStar: Boolean? = null,
        longPressExclude: Boolean? = null,
        sheetStyle: String? = null
    ) {
        viewModelScope.launch {
            prefsRepo.updateGestureSettings(swipeSwitch, doubleTapStar, longPressExclude, sheetStyle)
        }
    }

    fun updatePlanSetting(
        target: Int? = null,
        preset: String? = null,
        reminder: Boolean? = null,
        time: String? = null,
        dnd: Boolean? = null,
        dndRange: String? = null,
        weekendProtect: Boolean? = null
    ) {
        viewModelScope.launch {
            prefsRepo.updateReviewPlan(target, preset, reminder, time, dnd, dndRange, weekendProtect)
            if (target != null) {
                showToast("每日目标题量已设为 $target 题")
            }
        }
    }

    fun saveAiServiceConfig(
        protocol: String,
        baseUrl: String,
        modelId: String,
        apiKey: String,
        smartExp: Boolean = true,
        variant: Boolean = true,
        importFix: Boolean = true,
        singleLimit: Int = 1000
    ) {
        viewModelScope.launch {
            prefsRepo.updateAiConfig(
                protocol = protocol,
                baseUrl = baseUrl,
                modelId = modelId,
                apiKey = apiKey,
                smartExp = smartExp,
                variant = variant,
                importFix = importFix,
                singleLimit = singleLimit
            )
            showToast("AI 模型与端侧加密凭证已保存")
        }
    }
}
