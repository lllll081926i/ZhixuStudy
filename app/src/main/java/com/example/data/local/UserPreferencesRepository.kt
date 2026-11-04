package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "serene_study_prefs")

data class UserSettingsState(
    val themeMode: String = "system", // "system", "light", "dark"
    val dynamicColor: Boolean = false,
    val accentPreset: String = "柔和紫蓝", // "柔和紫蓝", "松石薄荷", "暖阳琥珀", "深海靛青"
    val fontScalePercent: Int = 100, // 85..130
    val lineSpacingMode: String = "标准 (1.5x)", // "紧凑 (1.35x)", "标准 (1.5x)", "宽松 (1.75x)"
    val instantExplanation: Boolean = true,
    val shuffleOptions: Boolean = false,
    val autoNextOnCorrect: Boolean = false,
    val hapticFeedback: Boolean = true,
    val hapticStrength: String = "轻柔微震", // "轻柔微震", "标准震感", "清脆反馈"
    val soundEffects: Boolean = true,
    val swipeSwitchQuestion: Boolean = true,
    val doubleTapStar: Boolean = true,
    val longPressExcludeOption: Boolean = true,
    val answerSheetStyle: String = "经典抽屉", // "经典抽屉", "悬浮面板"
    val reviewPreset: String = "智能推荐", // "智能推荐", "考前冲刺", "稳步巩固"
    val dailyTargetCount: Int = 20,
    val dailyDoneCount: Int = 12,
    val reminderEnabled: Boolean = true,
    val reminderTime: String = "20:30",
    val doNotDisturbMode: Boolean = false,
    val dndTimeRange: String = "22:30 - 07:30",
    val weekendProtect: Boolean = true,
    val tabletSplitViewPreferred: Boolean = true,
    // AI Service Settings
    val aiProtocol: String = "OpenAI 兼容协议 (支持 DeepSeek / GPT / Kimi)",
    val aiBaseUrl: String = "https://api.deepseek.com/v1",
    val aiModelId: String = "deepseek-chat",
    val aiApiKeyMasked: String = "sk-zhixu-local-••••••••••••38f2",
    val aiSmartExplanation: Boolean = true,
    val aiVariantGenerator: Boolean = true,
    val aiImportCorrection: Boolean = true,
    val aiTodayTokens: Int = 1420,
    val aiMonthTokens: Int = 38400,
    val aiSingleLimitTokens: Int = 1000
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val ACCENT_PRESET = stringPreferencesKey("accent_preset")
        val FONT_SCALE = intPreferencesKey("font_scale_percent")
        val LINE_SPACING = stringPreferencesKey("line_spacing_mode")
        val INSTANT_EXP = booleanPreferencesKey("instant_explanation")
        val SHUFFLE_OPTS = booleanPreferencesKey("shuffle_options")
        val AUTO_NEXT = booleanPreferencesKey("auto_next_on_correct")
        val HAPTIC = booleanPreferencesKey("haptic_feedback")
        val HAPTIC_STRENGTH = stringPreferencesKey("haptic_strength")
        val SOUND = booleanPreferencesKey("sound_effects")
        val SWIPE_SWITCH = booleanPreferencesKey("swipe_switch_question")
        val DOUBLE_TAP_STAR = booleanPreferencesKey("double_tap_star")
        val LONG_PRESS_EXCLUDE = booleanPreferencesKey("long_press_exclude")
        val ANSWER_SHEET_STYLE = stringPreferencesKey("answer_sheet_style")
        val REVIEW_PRESET = stringPreferencesKey("review_preset")
        val DAILY_TARGET = intPreferencesKey("daily_target_count")
        val DAILY_DONE = intPreferencesKey("daily_done_count")
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
        val DND_MODE = booleanPreferencesKey("dnd_mode")
        val DND_TIME_RANGE = stringPreferencesKey("dnd_time_range")
        val WEEKEND_PROTECT = booleanPreferencesKey("weekend_protect")
        val TABLET_SPLIT = booleanPreferencesKey("tablet_split_view")
        val AI_PROTOCOL = stringPreferencesKey("ai_protocol")
        val AI_BASE_URL = stringPreferencesKey("ai_base_url")
        val AI_MODEL_ID = stringPreferencesKey("ai_model_id")
        val AI_API_KEY = stringPreferencesKey("ai_api_key")
        val AI_SMART_EXP = booleanPreferencesKey("ai_smart_exp")
        val AI_VARIANT = booleanPreferencesKey("ai_variant")
        val AI_IMPORT_FIX = booleanPreferencesKey("ai_import_fix")
        val AI_TODAY_TOKENS = intPreferencesKey("ai_today_tokens")
        val AI_MONTH_TOKENS = intPreferencesKey("ai_month_tokens")
        val AI_SINGLE_LIMIT = intPreferencesKey("ai_single_limit")
    }

    val settingsFlow: Flow<UserSettingsState> = context.dataStore.data.map { prefs ->
        UserSettingsState(
            themeMode = prefs[Keys.THEME_MODE] ?: "system",
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: false,
            accentPreset = prefs[Keys.ACCENT_PRESET] ?: "柔和紫蓝",
            fontScalePercent = prefs[Keys.FONT_SCALE] ?: 100,
            lineSpacingMode = prefs[Keys.LINE_SPACING] ?: "标准 (1.5x)",
            instantExplanation = prefs[Keys.INSTANT_EXP] ?: true,
            shuffleOptions = prefs[Keys.SHUFFLE_OPTS] ?: false,
            autoNextOnCorrect = prefs[Keys.AUTO_NEXT] ?: false,
            hapticFeedback = prefs[Keys.HAPTIC] ?: true,
            hapticStrength = prefs[Keys.HAPTIC_STRENGTH] ?: "轻柔微震",
            soundEffects = prefs[Keys.SOUND] ?: true,
            swipeSwitchQuestion = prefs[Keys.SWIPE_SWITCH] ?: true,
            doubleTapStar = prefs[Keys.DOUBLE_TAP_STAR] ?: true,
            longPressExcludeOption = prefs[Keys.LONG_PRESS_EXCLUDE] ?: true,
            answerSheetStyle = prefs[Keys.ANSWER_SHEET_STYLE] ?: "经典抽屉",
            reviewPreset = prefs[Keys.REVIEW_PRESET] ?: "智能推荐",
            dailyTargetCount = prefs[Keys.DAILY_TARGET] ?: 20,
            dailyDoneCount = prefs[Keys.DAILY_DONE] ?: 12,
            reminderEnabled = prefs[Keys.REMINDER_ENABLED] ?: true,
            reminderTime = prefs[Keys.REMINDER_TIME] ?: "20:30",
            doNotDisturbMode = prefs[Keys.DND_MODE] ?: false,
            dndTimeRange = prefs[Keys.DND_TIME_RANGE] ?: "22:30 - 07:30",
            weekendProtect = prefs[Keys.WEEKEND_PROTECT] ?: true,
            tabletSplitViewPreferred = prefs[Keys.TABLET_SPLIT] ?: true,
            aiProtocol = prefs[Keys.AI_PROTOCOL] ?: "OpenAI 兼容协议 (支持 DeepSeek / GPT / Kimi)",
            aiBaseUrl = prefs[Keys.AI_BASE_URL] ?: "https://api.deepseek.com/v1",
            aiModelId = prefs[Keys.AI_MODEL_ID] ?: "deepseek-chat",
            aiApiKeyMasked = prefs[Keys.AI_API_KEY] ?: "sk-zhixu-local-••••••••••••38f2",
            aiSmartExplanation = prefs[Keys.AI_SMART_EXP] ?: true,
            aiVariantGenerator = prefs[Keys.AI_VARIANT] ?: true,
            aiImportCorrection = prefs[Keys.AI_IMPORT_FIX] ?: true,
            aiTodayTokens = prefs[Keys.AI_TODAY_TOKENS] ?: 1420,
            aiMonthTokens = prefs[Keys.AI_MONTH_TOKENS] ?: 38400,
            aiSingleLimitTokens = prefs[Keys.AI_SINGLE_LIMIT] ?: 1000
        )
    }

    suspend fun updateThemeMode(mode: String) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode }
    }

    suspend fun updateDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun updateAccentPreset(preset: String) {
        context.dataStore.edit { it[Keys.ACCENT_PRESET] = preset }
    }

    suspend fun updateFontScale(percent: Int) {
        context.dataStore.edit { it[Keys.FONT_SCALE] = percent.coerceIn(75, 130) }
    }

    suspend fun updateLineSpacing(mode: String) {
        context.dataStore.edit { it[Keys.LINE_SPACING] = mode }
    }

    suspend fun updatePracticeHabit(
        instantExp: Boolean? = null,
        shuffleOpts: Boolean? = null,
        autoNext: Boolean? = null,
        haptic: Boolean? = null,
        hapticStrength: String? = null,
        sound: Boolean? = null
    ) {
        context.dataStore.edit { prefs ->
            instantExp?.let { prefs[Keys.INSTANT_EXP] = it }
            shuffleOpts?.let { prefs[Keys.SHUFFLE_OPTS] = it }
            autoNext?.let { prefs[Keys.AUTO_NEXT] = it }
            haptic?.let { prefs[Keys.HAPTIC] = it }
            hapticStrength?.let { prefs[Keys.HAPTIC_STRENGTH] = it }
            sound?.let { prefs[Keys.SOUND] = it }
        }
    }

    suspend fun updateGestureSettings(
        swipeSwitch: Boolean? = null,
        doubleTapStar: Boolean? = null,
        longPressExclude: Boolean? = null,
        sheetStyle: String? = null
    ) {
        context.dataStore.edit { prefs ->
            swipeSwitch?.let { prefs[Keys.SWIPE_SWITCH] = it }
            doubleTapStar?.let { prefs[Keys.DOUBLE_TAP_STAR] = it }
            longPressExclude?.let { prefs[Keys.LONG_PRESS_EXCLUDE] = it }
            sheetStyle?.let { prefs[Keys.ANSWER_SHEET_STYLE] = it }
        }
    }

    suspend fun updateReviewPlan(
        target: Int? = null,
        preset: String? = null,
        reminder: Boolean? = null,
        time: String? = null,
        dnd: Boolean? = null,
        dndRange: String? = null,
        weekendProtect: Boolean? = null
    ) {
        context.dataStore.edit { prefs ->
            target?.let { prefs[Keys.DAILY_TARGET] = it.coerceIn(5, 100) }
            preset?.let { prefs[Keys.REVIEW_PRESET] = it }
            reminder?.let { prefs[Keys.REMINDER_ENABLED] = it }
            time?.let { prefs[Keys.REMINDER_TIME] = it }
            dnd?.let { prefs[Keys.DND_MODE] = it }
            dndRange?.let { prefs[Keys.DND_TIME_RANGE] = it }
            weekendProtect?.let { prefs[Keys.WEEKEND_PROTECT] = it }
        }
    }

    suspend fun incrementDailyDone(added: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.DAILY_DONE] ?: 12
            prefs[Keys.DAILY_DONE] = current + added
        }
    }

    suspend fun updateTabletSplitPreferred(enabled: Boolean) {
        context.dataStore.edit { it[Keys.TABLET_SPLIT] = enabled }
    }

    suspend fun updateAiConfig(
        protocol: String? = null,
        baseUrl: String? = null,
        modelId: String? = null,
        apiKey: String? = null,
        smartExp: Boolean? = null,
        variant: Boolean? = null,
        importFix: Boolean? = null,
        singleLimit: Int? = null,
        addTokens: Int = 0
    ) {
        context.dataStore.edit { prefs ->
            protocol?.let { prefs[Keys.AI_PROTOCOL] = it }
            baseUrl?.let { prefs[Keys.AI_BASE_URL] = it }
            modelId?.let { prefs[Keys.AI_MODEL_ID] = it }
            apiKey?.let { prefs[Keys.AI_API_KEY] = it }
            smartExp?.let { prefs[Keys.AI_SMART_EXP] = it }
            variant?.let { prefs[Keys.AI_VARIANT] = it }
            importFix?.let { prefs[Keys.AI_IMPORT_FIX] = it }
            singleLimit?.let { prefs[Keys.AI_SINGLE_LIMIT] = it }
            if (addTokens > 0) {
                prefs[Keys.AI_TODAY_TOKENS] = (prefs[Keys.AI_TODAY_TOKENS] ?: 1420) + addTokens
                prefs[Keys.AI_MONTH_TOKENS] = (prefs[Keys.AI_MONTH_TOKENS] ?: 38400) + addTokens
            }
        }
    }
}
