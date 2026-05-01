package com.panomc.plugins.countdowntimer

import com.panomc.platform.api.PanoPlugin
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import com.panomc.plugins.license.PluginLicenseClient
import java.io.File

class CountdownTimerPlugin : PanoPlugin() {
    val uploadsDir: File by lazy {
        File(pluginDataFolder, "uploads")
    }

    /**
     * Premium plugin: enforce a panomc.com license at startup. No-op for free builds.
     */
    private val licenseClient by lazy { PluginLicenseClient(this) }

    override suspend fun onStart() {
        // Verify license BEFORE doing any startup work. If invalid, throws and the
        // host marks this plugin failed and records the failure for the panel UI.
        // Pano core itself keeps running so the operator can resolve the issue.
        licenseClient.requireValidLicense()

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
