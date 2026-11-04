package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SereneTheme

/**
 * GitHub-style Habit Heatmap Matrix for daily review tracking.
 */
@Composable
fun StudyHeatmapCard(
    modifier: Modifier = Modifier,
    monthLabel: String = "近期打卡热力矩阵",
    daysActivity: List<Int> = listOf(
        0, 12, 18, 5, 24, 30, 15,
        8, 0, 16, 22, 28, 14, 0,
        19, 25, 32, 18, 12, 26, 20,
        15, 21, 0, 27, 35, 18, 22
    )
) {
    val colors = SereneTheme.colors

    fun heatColor(count: Int): Color = when {
        count == 0 -> colors.subtleSurface
        count <= 10 -> colors.mintBg
        count <= 22 -> colors.mintText.copy(alpha = 0.55f)
        else -> colors.mintText
    }

    SereneCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = monthLabel,
                    color = colors.textPrimary,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "近4周连续打卡 24 天 · 知识沉淀深厚",
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            SereneBadge(
                text = "活跃度 92%",
                containerColor = colors.mintBg,
                contentColor = colors.mintText
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Heatmap Grid: 4 weeks (rows) x 7 days (cols)
        val weeks = daysActivity.chunked(7)
        val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Weekday headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { d ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = d,
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Heat cells
            weeks.forEach { weekList ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    weekList.forEach { count ->
                        val cellBg = heatColor(count)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(cellBg)
                                .border(
                                    width = 0.5.dp,
                                    color = if (count == 0) colors.borderSubtle else cellBg.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Heat legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("少", color = colors.textMuted, fontSize = 10.sp)
            Spacer(modifier = Modifier.width(4.dp))
            listOf(0, 8, 18, 30).forEach { lvl ->
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(heatColor(lvl))
                )
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text("多", color = colors.textMuted, fontSize = 10.sp)
        }
    }
}
