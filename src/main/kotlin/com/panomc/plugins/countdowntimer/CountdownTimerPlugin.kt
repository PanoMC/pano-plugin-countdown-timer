package com.panomc.plugins.countdowntimer

import com.panomc.platform.api.PanoPlugin
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import java.io.File

class CountdownTimerPlugin : PanoPlugin() {
    val uploadsDir: File by lazy {
        File(pluginDataFolder, "uploads")
    }

    override suspend fun onStart() {
        logger.info("Starting Countdown Timer Plugin...")

        val configManager = PluginConfigManager(this, CountdownTimerConfig::class.java)
        pluginBeanContext.beanFactory.registerSingleton(PluginConfigManager::class.java.name, configManager)

        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs()
        }
    }

    override suspend fun onEnable() {
        logger.info("Enabled Countdown Timer Plugin!")
    }

    override suspend fun onUninstall() {
        logger.info("Uninstalling Countdown Timer Plugin...")

        if (uploadsDir.exists()) {
            uploadsDir.deleteRecursively()
        }
    }
}
