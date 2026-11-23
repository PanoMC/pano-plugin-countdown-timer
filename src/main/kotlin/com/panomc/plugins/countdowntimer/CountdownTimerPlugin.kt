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

        // A stop / start in the same context keeps the registered manager: registering it again would fail.
        val beanFactory = pluginBeanContext.beanFactory
        if (!beanFactory.containsSingleton(PluginConfigManager::class.java.name)) {
            beanFactory.registerSingleton(PluginConfigManager::class.java.name, PluginConfigManager(this, CountdownTimerConfig::class.java))
        }

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
