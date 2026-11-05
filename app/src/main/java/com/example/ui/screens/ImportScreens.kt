package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ImportPresetEntity
import com.example.data.local.QuestionBankEntity
import com.example.data.parser.EvaluatedImportItem
import com.example.ui.components.CompletionVectorIllustration
import com.example.ui.components.ImportStepIndicator
import com.example.ui.components.SereneBadge
import com.example.ui.components.SereneCard
import com.example.ui.components.SereneFilterChipRow
import com.example.ui.components.SereneTopBar
import com.example.ui.theme.SereneTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SmartImportFlowState
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun ImportSourceScreen(
    viewModel: StudyViewModel,
    importState: SmartImportFlowState,
    banks: List<QuestionBankEntity>,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    val context = LocalContext.current
    var showPasteInline by remember { mutableStateOf(false) }
    var pastedText by remember {
        mutableStateOf(
            """
            1. 调车作业中发生冲突或脱轨，造成直接经济损失构成事故的，按《铁路交通事故调查处理规则》办理。
            答案：正确
            解析：凡调车作业发生冲突或脱轨达到事故标准的，均严格按《事规》定性调查。

            2. 自动闭塞区段通过信号机显示绿灯表示运行前方至少有几个闭塞分区空闲？
            A. 三个及以上闭塞分区空闲
            B. 仅一个闭塞分区空闲
            C. 两个闭塞分区空闲
            D. 前方车站到发线占用
            答案：A
            解析：四显示绿灯表示前方至少有三个闭塞分区空闲。
            """.trimIndent()
        )
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "导入题库.xlsx"
            val isXlsx = fileName.endsWith(".xlsx", ignoreCase = true)
            val isDocx = fileName.endsWith(".docx", ignoreCase = true)
            if (isXlsx) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        viewModel.loadImportedXlsxStream(fileName, stream, importState.targetBankName)
                    }
                } catch (e: Exception) {
                    viewModel.showToast("Excel 文件读取失败，请检查文件格式")
                }
            } else if (isDocx) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        viewModel.loadImportedDocxStream(fileName, stream, importState.targetBankName)
                    }
                } catch (e: Exception) {
                    viewModel.showToast("Word 试卷读取失败，请检查文件格式")
                }
            } else {
                val content = try {
                    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
                } catch (e: Exception) {
                    ""
                }
                if (content.isNotBlank()) {
                    viewModel.loadImportedFileOrText(fileName, content, importState.targetBankName)
                } else {
                    viewModel.loadDemoImportData(fileName = fileName, navigateToStep2 = true)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "导入题库",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )
        ImportStepIndicator(
            currentStep = 1,
            stepLabels = listOf("选择来源", "字段映射", "审查去重", "导入完成")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = if (isTablet) 28.dp else 16.dp,
                end = if (isTablet) 28.dp else 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),
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
                        banks.forEach { b ->
                            val selected = b.name == importState.targetBankName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (selected) colors.primaryAccent else colors.subtleSurface)
                                    .clickable { viewModel.updateImportTargetBank(b.name) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = b.name,
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

            // 1. File & Paste Action Center
            item {
                SereneCard(
                    cornerRadius = 24.dp,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { filePickerLauncher.launch("*/*") },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("import_pick_local_file_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                        ) {
                            Icon(Icons.Outlined.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("选取文件", maxLines = 1, softWrap = false, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { showPasteInline = !showPasteInline },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("import_paste_text_btn"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Outlined.ContentPaste, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (showPasteInline) "收起输入" else "粘贴文本", maxLines = 1, softWrap = false, fontWeight = FontWeight.Bold)
                        }
                    }

                    AnimatedVisibility(visible = showPasteInline) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = pastedText,
                                onValueChange = { pastedText = it },
                                placeholder = { Text("粘贴题目文本内容...") },
                                minLines = 5,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            )
                            Button(
                                onClick = {
                                    showPasteInline = false
                                    viewModel.loadImportedFileOrText(
                                        fileName = "剪贴板题集.txt",
                                        content = pastedText,
                                        targetBank = importState.targetBankName
                                    )
                                },
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                            ) {
                                Text("开始解析", maxLines = 1, softWrap = false, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. Presets Quick Start
            item {
                SereneCard(cornerRadius = 24.dp) {
                    Text(
                        text = "示例模版",
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    viewModel.loadDemoImportData(
                                        fileName = "2026铁路运输模拟题.xlsx",
                                        navigateToStep2 = true
                                    )
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                Text("Excel", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    val demoAiken = """
                                        列车运行图的基本作用是什么？
                                        A. 全路组织列车运行的基础，综合性作业规划纲领
                                        B. 仅用于客运车站时刻表的制定依据
                                        ANSWER: A
                                    """.trimIndent()
                                    viewModel.loadImportedFileOrText(
                                        fileName = "Aiken试卷.txt",
                                        content = demoAiken,
                                        targetBank = importState.targetBankName
                                    )
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                Text("Aiken", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    val demoMd = """
                                        ### 1. 自动闭塞信号机显示绿灯表示运行前方至少几个闭塞分区空闲？
                                        - A. 三个及以上
                                        - B. 仅一个
                                        > 答案：A
                                    """.trimIndent()
                                    viewModel.loadImportedFileOrText(
                                        fileName = "Markdown真题.md",
                                        content = demoMd,
                                        targetBank = importState.targetBankName
                                    )
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                Text("Markdown", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.subtleSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    val demoWjx = """
                                        1. [单选题] 车站接发列车工作必须首先保证什么？
                                        A. 行车安全与列车按运行图正点运行
                                        B. 车站调车作业效率最大化
                                        正确答案：A
                                    """.trimIndent()
                                    viewModel.loadImportedFileOrText(
                                        fileName = "问卷星试卷.txt",
                                        content = demoWjx,
                                        targetBank = importState.targetBankName
                                    )
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                Text("问卷星", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImportMappingScreen(
    viewModel: StudyViewModel,
    importState: SmartImportFlowState,
    presets: List<ImportPresetEntity>,
    isTablet: Boolean,
    tabletSplitActive: Boolean
) {
    val colors = SereneTheme.colors
    var showSavePresetDialog by remember { mutableStateOf(false) }
    var presetNameInput by remember { mutableStateOf("常用的表格整理习惯") }

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "字段与规则映射",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet,
            tabletSplitActive = tabletSplitActive,
            onToggleTabletSplit = { viewModel.setTabletSplitPreferred(!tabletSplitActive) }
        )
        ImportStepIndicator(
            currentStep = 2,
            stepLabels = listOf("选择来源", "字段映射", "审查去重", "导入完成")
        )

        if (isTablet && tabletSplitActive) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(0.95f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        MappingAndRulesPaneContent(
                            importState = importState,
                            presets = presets,
                            onUpdateRules = { viewModel.updateImportRules(it) },
                            onApplyPreset = { viewModel.applyImportPreset(it) },
                            onOpenSavePreset = { showSavePresetDialog = true }
                        )
                    }
                }

                Column(modifier = Modifier.weight(1.05f)) {
                    ImportPreviewContentPane(
                        viewModel = viewModel,
                        importState = importState,
                        isEmbeddedInTabletWorkbench = true
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    MappingAndRulesPaneContent(
                        importState = importState,
                        presets = presets,
                        onUpdateRules = { viewModel.updateImportRules(it) },
                        onApplyPreset = { viewModel.applyImportPreset(it) },
                        onOpenSavePreset = { showSavePresetDialog = true }
                    )
                }
            }

            Surface(
                color = colors.cardSurface,
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("← 重选文件", maxLines = 1, softWrap = false)
                    }
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.IMPORT_PREVIEW) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("import_to_preview_btn"),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                    ) {
                        Text(
                            text = "看看认出来的 ${importState.evaluatedItems.size} 道题",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }

    if (showSavePresetDialog) {
        AlertDialog(
            onDismissRequest = { showSavePresetDialog = false },
            title = { Text("记住这套整理方式", maxLines = 1) },
            text = {
                OutlinedTextField(
                    value = presetNameInput,
                    onValueChange = { presetNameInput = it },
                    label = { Text("起个好记的名字") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveImportPreset(presetNameInput)
                        showSavePresetDialog = false
                    }
                ) {
                    Text("保存", maxLines = 1, softWrap = false)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSavePresetDialog = false }) {
                    Text("取消", maxLines = 1, softWrap = false)
                }
            }
        )
    }
}

@Composable
private fun MappingAndRulesPaneContent(
    importState: SmartImportFlowState,
    presets: List<ImportPresetEntity>,
    onUpdateRules: (com.example.data.parser.PreprocessRulesConfig) -> Unit,
    onApplyPreset: (ImportPresetEntity) -> Unit,
    onOpenSavePreset: () -> Unit
) {
    val colors = SereneTheme.colors
    val rules = importState.rules

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SereneCard(cornerRadius = 22.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = importState.fileName,
                        color = colors.textPrimary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "工作表：${importState.sheetName} · 从第 ${importState.startRow} 行开始读",
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                SereneBadge(
                    text = "已自动对好列",
                    containerColor = colors.mintBg,
                    contentColor = colors.mintText
                )
            }
        }

        SereneCard(cornerRadius = 22.dp) {
            Text(
                text = "表格里的每一列对应什么？",
                color = colors.textPrimary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            val mappingPairs = listOf(
                "题目内容" to importState.mapping.stemCol,
                "单选/多选/判断" to importState.mapping.typeCol,
                "四个选项 (A~D)" to "${importState.mapping.optionACol} ~ ${importState.mapping.optionDCol}",
                "正确答案" to importState.mapping.answerCol,
                "答案解析" to importState.mapping.explanationCol,
                "所属章节与难度" to "${importState.mapping.chapterCol} / ${importState.mapping.difficultyCol}"
            )
            mappingPairs.forEach { (fieldLabel, colVal) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fieldLabel,
                        color = colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SereneBadge(
                        text = "$colVal ✓",
                        containerColor = colors.primarySoft,
                        contentColor = colors.primaryAccent
                    )
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
                    text = "顺手帮你整理排版",
                    color = colors.textPrimary,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(onClick = onOpenSavePreset) {
                    Text("记住这套设置", fontSize = 12.sp, maxLines = 1, softWrap = false)
                }
            }

            if (presets.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presets.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(colors.subtleSurface)
                                .clickable { onApplyPreset(preset) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = preset.name,
                                color = colors.primaryAccent,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            RuleSwitchItem(
                title = "自动去掉题目开头的旧题号（如 1. 、(1)）",
                checked = rules.removeSerial,
                onCheckedChange = { onUpdateRules(rules.copy(removeSerial = it)) }
            )
            RuleSwitchItem(
                title = "把答案里的大小写和打勾统一整理干净",
                checked = rules.normalizeAnswer,
                onCheckedChange = { onUpdateRules(rules.copy(normalizeAnswer = it)) }
            )
            RuleSwitchItem(
                title = "没写题型时，根据答案自动猜是单选还是多选",
                checked = rules.inferType,
                onCheckedChange = { onUpdateRules(rules.copy(inferType = it)) }
            )
        }
    }
}

@Composable
private fun RuleSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = SereneTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ImportPreviewScreen(
    viewModel: StudyViewModel,
    importState: SmartImportFlowState,
    isTablet: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "核对题目",
            onBack = { viewModel.navigateBack() },
            isTablet = isTablet
        )
        ImportStepIndicator(
            currentStep = 3,
            stepLabels = listOf("选文件", "对表格列", "核对题目", "导进去了")
        )
        ImportPreviewContentPane(
            viewModel = viewModel,
            importState = importState,
            isEmbeddedInTabletWorkbench = false
        )
    }
}

@Composable
fun ImportPreviewContentPane(
    viewModel: StudyViewModel,
    importState: SmartImportFlowState,
    isEmbeddedInTabletWorkbench: Boolean
) {
    val colors = SereneTheme.colors
    val items = importState.evaluatedItems
    val totalCount = items.size
    val validCount = items.count { it.status == "valid" }
    val conflictCount = items.count { it.status == "conflict" || it.status == "duplicate" }
    val invalidCount = items.count { it.status == "invalid" }

    var editingItem by remember { mutableStateOf<EvaluatedImportItem?>(null) }
    var comparingItem by remember { mutableStateOf<EvaluatedImportItem?>(null) }
    var strategyMenuExpanded by remember { mutableStateOf(false) }

    val filteredItems = remember(items, importState.previewFilter) {
        when (importState.previewFilter) {
            "invalid" -> items.filter { it.status == "invalid" }
            "conflict" -> items.filter { it.status == "conflict" || it.status == "duplicate" }
            "valid" -> items.filter { it.status == "valid" }
            else -> items.sortedBy {
                when (it.rowNumber) {
                    4 -> 0
                    5 -> 1
                    1 -> 2
                    else -> it.rowNumber + 2
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = if (isEmbeddedInTabletWorkbench) 4.dp else 16.dp,
                vertical = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PreviewStatTile(
                        modifier = Modifier.weight(1f),
                        label = "一共读出",
                        count = totalCount.toString(),
                        unit = "道",
                        accentColor = colors.textPrimary,
                        badgeBg = colors.primarySoft,
                        onClick = { viewModel.setImportPreviewFilter("all") }
                    )
                    PreviewStatTile(
                        modifier = Modifier.weight(1f),
                        label = "直接能用",
                        count = validCount.toString(),
                        unit = "道",
                        accentColor = colors.mintText,
                        badgeBg = colors.mintBg,
                        onClick = { viewModel.setImportPreviewFilter("valid") }
                    )
                    PreviewStatTile(
                        modifier = Modifier.weight(1f),
                        label = "和旧题重了",
                        count = conflictCount.toString(),
                        unit = "道",
                        accentColor = colors.peachText,
                        badgeBg = colors.peachBg,
                        onClick = { viewModel.setImportPreviewFilter("conflict") }
                    )
                    PreviewStatTile(
                        modifier = Modifier.weight(1f),
                        label = "缺了选项",
                        count = invalidCount.toString(),
                        unit = "道",
                        accentColor = colors.dangerText,
                        badgeBg = colors.dangerBg,
                        onClick = { viewModel.setImportPreviewFilter("invalid") }
                    )
                }
            }

            item {
                SereneCard(cornerRadius = 20.dp, contentPadding = PaddingValues(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "遇到重复题怎么办？",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Box {
                            val currentStrategyLabel = when (importState.defaultDuplicateStrategy) {
                                "replace" -> "用新题换掉旧题"
                                "keep" -> "两道都留着"
                                else -> "跳过重复题（推荐）"
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.subtleSurface)
                                    .clickable { strategyMenuExpanded = true }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentStrategyLabel,
                                    color = colors.primaryAccent,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("▼", color = colors.primaryAccent, fontSize = 11.sp)
                            }
                            DropdownMenu(
                                expanded = strategyMenuExpanded,
                                onDismissRequest = { strategyMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("跳过重复题（推荐）", maxLines = 1) },
                                    onClick = {
                                        viewModel.setDefaultDuplicateStrategy("skip")
                                        strategyMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("用新题换掉旧题", maxLines = 1) },
                                    onClick = {
                                        viewModel.setDefaultDuplicateStrategy("replace")
                                        strategyMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("两道都留着", maxLines = 1) },
                                    onClick = {
                                        viewModel.setDefaultDuplicateStrategy("keep")
                                        strategyMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                SereneFilterChipRow(
                    options = listOf(
                        "all" to "全部 $totalCount",
                        "invalid" to "得改一下 $invalidCount",
                        "conflict" to "有重复 $conflictCount",
                        "valid" to "没问题 $validCount"
                    ),
                    selectedKey = importState.previewFilter,
                    onSelect = { viewModel.setImportPreviewFilter(it) }
                )
            }

            items(filteredItems, key = { it.id }) { item ->
                ImportRowInspectionCard(
                    item = item,
                    onEdit = { editingItem = item },
                    onCompare = { comparingItem = item },
                    onToggleResolution = {
                        val next = when (item.resolutionAction) {
                            "replace" -> "skip"
                            "skip" -> "keep"
                            else -> "replace"
                        }
                        viewModel.updateImportItemResolution(item.id, next)
                        val label = when (next) {
                            "replace" -> "换掉旧题"
                            "keep" -> "两道都留"
                            else -> "跳过不导"
                        }
                        viewModel.showToast("第 ${item.rowNumber} 行已改为：$label")
                    },
                    onDiscard = { viewModel.discardImportItem(item.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        Surface(
            color = colors.cardSurface,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isEmbeddedInTabletWorkbench) {
                    OutlinedButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上一步", maxLines = 1, softWrap = false)
                    }
                }
                Button(
                    onClick = { viewModel.commitSmartImport() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("confirm_smart_import_btn"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryBright)
                ) {
                    Icon(Icons.Outlined.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "确认把这 $validCount 道题导进去",
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    editingItem?.let { target ->
        var stemText by remember(target.id) { mutableStateOf(target.stem) }
        var ansText by remember(target.id) {
            mutableStateOf(
                if (target.status == "invalid") "B, C" else target.rawAnswerText
            )
        }
        var expText by remember(target.id) { mutableStateOf(target.explanation) }

        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = { Text("改一下第 ${target.rowNumber} 行这道题", maxLines = 1) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = stemText,
                        onValueChange = { stemText = it },
                        label = { Text("题目内容") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ansText,
                        onValueChange = { ansText = it },
                        label = { Text("正确答案") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fix_row_answer_input")
                    )
                    OutlinedTextField(
                        value = expText,
                        onValueChange = { expText = it },
                        label = { Text("解析说明") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.fixImportItemInline(
                            itemId = target.id,
                            newStem = stemText,
                            newAnswerText = ansText,
                            newOptions = target.options,
                            newExplanation = expText
                        )
                        editingItem = null
                    },
                    modifier = Modifier.testTag("confirm_fix_row_btn")
                ) {
                    Text("改好了", maxLines = 1, softWrap = false)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("取消", maxLines = 1, softWrap = false)
                }
            }
        )
    }

    comparingItem?.let { target ->
        val oldQ = target.existingQuestion
        AlertDialog(
            onDismissRequest = { comparingItem = null },
            title = { Text("跟题库里的旧题比一比", maxLines = 1) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = target.stem,
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    SereneCard(
                        cornerRadius = 14.dp,
                        containerColor = colors.subtleSurface,
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text("题库里原来有的版本", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
                        Text(
                            text = "答案：${oldQ?.answer?.joinToString(",") ?: "A"}",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = oldQ?.explanation ?: "行车安全第一",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }
                    SereneCard(
                        cornerRadius = 14.dp,
                        containerColor = colors.primarySoft,
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text("这次新导入的版本", style = MaterialTheme.typography.labelSmall, color = colors.primaryAccent)
                        Text(
                            text = "答案：${target.rawAnswerText}",
                            color = colors.primaryAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = target.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateImportItemResolution(target.id, "replace")
                        comparingItem = null
                        viewModel.showToast("已选择用新题换掉旧题")
                    }
                ) {
                    Text("用新题换掉旧题", maxLines = 1, softWrap = false)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.updateImportItemResolution(target.id, "skip")
                        comparingItem = null
                        viewModel.showToast("已选择保留原来的旧题")
                    }
                ) {
                    Text("保留原来的", maxLines = 1, softWrap = false)
                }
            }
        )
    }
}

@Composable
private fun PreviewStatTile(
    modifier: Modifier = Modifier,
    label: String,
    count: String,
    unit: String,
    accentColor: Color,
    badgeBg: Color,
    onClick: () -> Unit
) {
    val colors = SereneTheme.colors
    SereneCard(
        modifier = modifier,
        cornerRadius = 16.dp,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        onClick = onClick
    ) {
        Text(
            text = label,
            color = colors.textSecondary,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = count,
                color = accentColor,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = unit,
                color = colors.textMuted,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@Composable
private fun ImportRowInspectionCard(
    item: EvaluatedImportItem,
    onEdit: () -> Unit,
    onCompare: () -> Unit,
    onToggleResolution: () -> Unit,
    onDiscard: () -> Unit
) {
    val colors = SereneTheme.colors
    val (badgeText, badgeBg, badgeFg) = when (item.status) {
        "invalid" -> Triple("⊗ 得改一下", colors.dangerBg, colors.dangerText)
        "conflict" -> Triple("⚠ 答案不一样", colors.peachBg, colors.peachText)
        "duplicate" -> Triple("⇄ 完全重了", colors.amberBg, colors.amberText)
        else -> Triple("✓ 没问题", colors.mintBg, colors.mintText)
    }

    SereneCard(
        cornerRadius = 22.dp,
        borderColor = when (item.status) {
            "invalid" -> colors.dangerText.copy(alpha = 0.45f)
            "conflict" -> colors.peachText.copy(alpha = 0.35f)
            else -> colors.borderSubtle
        },
        modifier = Modifier.testTag("import_row_card_${item.rowNumber}")
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
                SereneBadge(
                    text = "表格第 ${item.rowNumber} 行",
                    containerColor = colors.subtleSurface,
                    contentColor = colors.textSecondary
                )
                SereneBadge(
                    text = badgeText,
                    containerColor = badgeBg,
                    contentColor = badgeFg
                )
                Text(
                    text = item.typeLabel,
                    color = colors.textMuted,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            if (item.resolutionAction != "default") {
                SereneBadge(
                    text = when (item.resolutionAction) {
                        "replace" -> "换掉旧题"
                        "keep" -> "两道都留"
                        else -> "跳过不导"
                    },
                    containerColor = colors.primarySoft,
                    contentColor = colors.primaryAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.statusMessage,
            color = badgeFg,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )

        if (item.status == "invalid" && item.invalidDetail.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.dangerBg)
                    .border(1.dp, colors.dangerText.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = null,
                    tint = colors.dangerText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "答案里写了不存在的选项字母",
                        color = colors.dangerText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = item.invalidDetail,
                        color = colors.dangerText.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.stem,
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        if (item.options.isNotEmpty() && item.type != "judge") {
            Spacer(modifier = Modifier.height(10.dp))
            item.options.forEach { opt ->
                val letter = opt.take(1)
                val isSelectedInImport = letter in item.answer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelectedInImport) colors.primarySoft else colors.subtleSurface.copy(alpha = 0.6f))
                        .border(
                            width = 1.dp,
                            color = if (isSelectedInImport) colors.primaryAccent.copy(alpha = 0.4f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = opt,
                        color = if (isSelectedInImport) colors.primaryAccent else colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (isSelectedInImport) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
            if (item.status == "invalid") {
                Spacer(modifier = Modifier.height(6.dp))
                SereneBadge(
                    text = "✕ 表格里没写 Z 选项的内容",
                    containerColor = colors.dangerBg,
                    contentColor = colors.dangerText
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (item.status) {
            "conflict", "duplicate" -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("顺手改改", fontSize = 12.sp, maxLines = 1, softWrap = false)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onCompare,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primarySoft,
                            contentColor = colors.primaryAccent
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Outlined.CompareArrows, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("比一下新旧题", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onToggleResolution,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("换处理方式", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                    }
                }
            }
            "invalid" -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDiscard,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("不要这题了", fontSize = 12.sp, maxLines = 1, softWrap = false)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onEdit,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("fix_invalid_row_btn_${item.rowNumber}")
                    ) {
                        Icon(Icons.Outlined.Build, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("现在改好它", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                    }
                }
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.chapter,
                        color = colors.textMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("看看解析", fontSize = 12.sp, maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

@Composable
fun ImportResultScreen(
    viewModel: StudyViewModel,
    importState: SmartImportFlowState,
    isTablet: Boolean
) {
    val colors = SereneTheme.colors
    val stats = importState.lastCommitStats

    Column(modifier = Modifier.fillMaxSize()) {
        SereneTopBar(
            title = "导入完成",
            onBack = { viewModel.openBankDetail(stats.targetBank) },
            isTablet = isTablet
        )
        ImportStepIndicator(
            currentStep = 4,
            stepLabels = listOf("选择来源", "字段映射", "审查去重", "导入完成")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = if (isTablet) 32.dp else 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
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
                        CompletionVectorIllustration(size = 104.dp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "题库导入完成",
                            color = colors.textPrimary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "已归入 ${stats.targetBank}",
                            color = colors.textSecondary,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PreviewStatTile(
                                modifier = Modifier.weight(1f),
                                label = "新增录入",
                                count = stats.added.toString(),
                                unit = "题",
                                accentColor = colors.mintText,
                                badgeBg = colors.mintBg,
                                onClick = {}
                            )
                            PreviewStatTile(
                                modifier = Modifier.weight(1f),
                                label = "替换旧题",
                                count = stats.replaced.toString(),
                                unit = "题",
                                accentColor = colors.primaryAccent,
                                badgeBg = colors.primarySoft,
                                onClick = {}
                            )
                            PreviewStatTile(
                                modifier = Modifier.weight(1f),
                                label = "跳过重复",
                                count = stats.skipped.toString(),
                                unit = "题",
                                accentColor = colors.amberText,
                                badgeBg = colors.amberBg,
                                onClick = {}
                            )
                            PreviewStatTile(
                                modifier = Modifier.weight(1f),
                                label = "没导进去",
                                count = stats.invalidSkipped.toString(),
                                unit = "题",
                                accentColor = colors.textSecondary,
                                badgeBg = colors.subtleSurface,
                                onClick = {}
                            )
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = { viewModel.openBankDetail(stats.targetBank) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("import_result_open_bank_btn"),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                        ) {
                            Text("去看看这本题库", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.IMPORT_SOURCE) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("再导一份文件", maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}
