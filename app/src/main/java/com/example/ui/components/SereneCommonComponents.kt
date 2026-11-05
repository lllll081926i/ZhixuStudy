package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.ViewSidebar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SereneTheme

@Composable
fun SereneCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    containerColor: Color = SereneTheme.colors.cardSurface,
    borderColor: Color = SereneTheme.colors.borderSubtle,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickMod = if (onClick != null) {
        Modifier.bounceClick(scaleDown = 0.985f, onClick = onClick)
    } else Modifier

    Surface(
        modifier = modifier.then(clickMod),
        shape = RoundedCornerShape(cornerRadius),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

@Composable
fun SereneBadge(
    text: String,
    containerColor: Color = SereneTheme.colors.primarySoft,
    contentColor: Color = SereneTheme.colors.primaryAccent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun SereneTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    isTablet: Boolean = false,
    tabletSplitActive: Boolean = true,
    onToggleTabletSplit: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = SereneTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.cardSurface)
                        .border(1.dp, colors.borderSubtle, CircleShape)
                        .testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "返回",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isTablet && onToggleTabletSplit != null) {
                TabletViewModeToggle(
                    isSplitView = tabletSplitActive,
                    onToggle = onToggleTabletSplit
                )
            }
            actions()
        }
    }
}

@Composable
fun TabletViewModeToggle(
    isSplitView: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SereneTheme.colors
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(colors.subtleSurface)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(50))
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val splitBg by animateColorAsState(
            targetValue = if (isSplitView) colors.cardSurface else Color.Transparent,
            label = "split_bg"
        )
        val focusBg by animateColorAsState(
            targetValue = if (!isSplitView) colors.cardSurface else Color.Transparent,
            label = "focus_bg"
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(splitBg)
                .clickable { if (!isSplitView) onToggle() }
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ViewSidebar,
                contentDescription = "分栏",
                tint = if (isSplitView) colors.primaryAccent else colors.textSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "分栏",
                color = if (isSplitView) colors.primaryAccent else colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(focusBg)
                .clickable { if (isSplitView) onToggle() }
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Fullscreen,
                contentDescription = "单栏",
                tint = if (!isSplitView) colors.primaryAccent else colors.textSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "单栏",
                color = if (!isSplitView) colors.primaryAccent else colors.textSecondary,
                maxLines = 1,
                softWrap = false,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun SereneLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    trackColor: Color = SereneTheme.colors.subtleSurface,
    indicatorColor: Color = SereneTheme.colors.primaryAccent
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(indicatorColor)
        )
    }
}

@Composable
fun SereneFilterChipRow(
    options: List<Pair<String, String>>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SereneTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { (key, label) ->
            val selected = key == selectedKey
            val bg by animateColorAsState(
                targetValue = if (selected) colors.primaryAccent else colors.cardSurface,
                label = "chip_bg_$key"
            )
            val fg by animateColorAsState(
                targetValue = if (selected) Color.White else colors.textSecondary,
                label = "chip_fg_$key"
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(bg)
                    .border(
                        width = 1.dp,
                        color = if (selected) colors.primaryAccent else colors.borderSubtle,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onSelect(key) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("filter_chip_$key"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = fg,
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun ImportStepIndicator(
    currentStep: Int,
    totalSteps: Int = 4,
    stepLabels: List<String> = listOf("选择文件", "字段映射", "预览审查", "导入完成")
) {
    val colors = SereneTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            stepLabels.forEachIndexed { idx, label ->
                val stepNum = idx + 1
                val isDone = stepNum < currentStep
                val isActive = stepNum == currentStep
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> colors.primaryAccent
                                    isDone -> colors.mintBg
                                    else -> colors.subtleSurface
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDone) "✓" else stepNum.toString(),
                            color = when {
                                isActive -> Color.White
                                isDone -> colors.mintText
                                else -> colors.textMuted
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = label,
                        color = if (isActive) colors.textPrimary else colors.textSecondary,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        SereneLinearProgress(
            progress = currentStep.toFloat() / totalSteps.toFloat(),
            height = 4.dp
        )
    }
}
