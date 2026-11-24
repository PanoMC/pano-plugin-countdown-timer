package com.panomc.plugins.countdowntimer.config

import com.panomc.platform.api.config.PluginConfig

class CountdownTimerConfig(
    val enabled: Boolean = false,
    val endDate: Long = 0,
    val title: String = "",
    val message: String = "",
    val displayMode: DisplayMode = DisplayMode.COVER,
    val theme: CountdownTheme = CountdownTheme.DARK,
    val backgroundColor: String = "#000000",
    val textColor: String = "#ffffff",
    val gradientStart: String = "#667eea",
    val gradientEnd: String = "#764ba2",
    val backgroundImage: String = "",
    val showLogo: Boolean = true,
    val logoSize: String = "MEDIUM",
    val showServerIp: Boolean = false,
    val countdownStyle: CountdownStyle = CountdownStyle.SIMPLE,
    val actions: List<TimerAction> = listOf(TimerAction.HIDE),
    val openingEffect: String = "NONE",
    val pagePath: String = "/countdown",
    val showInNav: Boolean = true,
    val navLinkText: String = "",
    val showAdminLogin: Boolean = true,
    val coverDismissable: Boolean = false,
    val coverScope: String = "ALL",
    version: Int = 1
) : PluginConfig(version)
