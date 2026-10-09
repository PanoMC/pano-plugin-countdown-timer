package com.panomc.plugins.countdowntimer.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.db.DatabaseManager
import com.panomc.platform.model.*
import com.panomc.plugins.countdowntimer.CountdownTimerPlugin
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import com.panomc.plugins.countdowntimer.log.UpdatedCountdownTimerConfigLog
import com.panomc.plugins.countdowntimer.permission.ManageCountdownTimerPermission
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import java.io.File

@Endpoint
class PanelDeleteBackgroundAPI(
    private val plugin: CountdownTimerPlugin,
) : PanelApi() {
    override val paths = listOf(Path("/background", RouteType.DELETE))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val databaseManager by lazy {
        plugin.applicationContext.getBean(DatabaseManager::class.java)
    }

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<CountdownTimerConfig>
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageCountdownTimerPermission(), context)

        val oldImage = configManager.config.backgroundImage
        if (oldImage.isNotBlank()) {
            val oldFile = File(plugin.uploadsDir, oldImage)
            if (oldFile.exists()) {
                oldFile.delete()
            }
        }

        val updateBody = JsonObject().put("backgroundImage", "")
        configManager.saveConfig(updateBody)

        val userId = authProvider.getUserIdFromRoutingContext(context)
        val sqlClient = databaseManager.getSqlClient()
        val username = databaseManager.userDao.getUsernameFromUserId(userId, sqlClient) ?: "Unknown"

        databaseManager.panelActivityLogDao.add(
            UpdatedCountdownTimerConfigLog(userId, username, plugin.pluginId),
            sqlClient
        )

        return Successful()
    }
}
