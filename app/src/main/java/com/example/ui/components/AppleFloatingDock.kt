package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SereneTheme

enum class PrimaryTab(
    val route: String,
    val label: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val badgeCount: Int = 0
) {
    HOME("home", "学习", Icons.Outlined.School, Icons.Filled.School),
    LIBRARY("library", "题库", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
    REVIEW("review", "复习", Icons.Outlined.History, Icons.Filled.History),
    STATS("stats", "统计", Icons.Outlined.BarChart, Icons.Filled.BarChart)
}

@Composable
fun AppleFloatingDock(
    activeTab: PrimaryTab,
    reviewBadgeCount: Int,
    isTablet: Boolean,
    onSelectTab: (PrimaryTab) -> Unit,
    onOpenQuickSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = SereneTheme.colors
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val dockBgColor = if (colors.isDark) {
        Color(0xFF1E2235).copy(alpha = 0.94f)
    } else {
        Color(0xFFFFFFFF).copy(alpha = 0.94f)
    }

    val dockBorderColor = if (colors.isDark) {
        Color.White.copy(alpha = 0.12f)
    } else {
        colors.primaryAccent.copy(alpha = 0.14f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = (navBarBottomPadding + 10.dp).coerceAtLeast(12.dp),
                top = 4.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = if (isTablet) 500.dp else 380.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = colors.primaryAccent.copy(alpha = 0.20f),
                    spotColor = colors.primaryAccent.copy(alpha = 0.24f)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            dockBorderColor,
                            dockBorderColor.copy(alpha = 0.04f)
                        )
                    ),
                    shape = RoundedCornerShape(36.dp)
                ),
            shape = RoundedCornerShape(36.dp),
            color = dockBgColor,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = if (isTablet) 10.dp else 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PrimaryTab.entries.forEach { tab ->
                    val isSelected = tab == activeTab
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.03f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "dock_item_scale_${tab.name}"
                    )
                    val pillBg by animateColorAsState(
                        targetValue = if (isSelected) colors.primarySoft else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "dock_item_bg_${tab.name}"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) colors.primaryAccent else colors.textSecondary,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "dock_item_fg_${tab.name}"
                    )
                    val horizontalPad by animateDpAsState(
                        targetValue = if (isSelected) (if (isTablet) 18.dp else 12.dp) else (if (isTablet) 14.dp else 8.dp),
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "dock_item_pad_${tab.name}"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(scale)
                            .clip(RoundedCornerShape(28.dp))
                            .background(pillBg)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onSelectTab(tab) }
                            .padding(horizontal = horizontalPad, vertical = if (isTablet) 10.dp else 7.dp)
                            .testTag("dock_tab_${tab.route}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isTablet) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box {
                                    Icon(
                                        imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                        contentDescription = tab.label,
                                        tint = contentColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    if (tab == PrimaryTab.REVIEW && reviewBadgeCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(colors.peachText)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.label,
                                    color = contentColor,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box {
                                    Icon(
                                        imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                        contentDescription = tab.label,
                                        tint = contentColor,
                                        modifier = Modifier.size(21.dp)
                                    )
                                    if (tab == PrimaryTab.REVIEW && reviewBadgeCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(colors.peachText)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tab.label,
                                    color = contentColor,
                                    maxLines = 1,
                                    softWrap = false,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                if (isTablet && onOpenQuickSettings != null) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .width(1.dp)
                            .height(24.dp)
                            .background(colors.borderStrong.copy(alpha = 0.6f))
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.subtleSurface.copy(alpha = 0.7f))
                            .clickable { onOpenQuickSettings() }
                            .testTag("dock_tablet_settings"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "设置",
                            tint = colors.textSecondary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}
