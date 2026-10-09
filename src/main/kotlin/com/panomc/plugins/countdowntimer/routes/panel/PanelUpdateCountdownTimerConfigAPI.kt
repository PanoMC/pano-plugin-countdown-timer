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
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import com.panomc.platform.schema.dsl.Bodies
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class PanelUpdateCountdownTimerConfigAPI(
    private val plugin: CountdownTimerPlugin,
) : PanelApi() {
    override val paths = listOf(Path("/config", RouteType.PUT))

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
            .body(
                Bodies.json(
                    objectSchema()
                        .optionalProperty("enabled", booleanSchema())
                        .optionalProperty("endDate", numberSchema())
                        .optionalProperty("title", stringSchema())
                        .optionalProperty("message", stringSchema())
                        .optionalProperty("displayMode", stringSchema())
                        .optionalProperty("theme", stringSchema())
                        .optionalProperty("backgroundColor", stringSchema())
                        .optionalProperty("textColor", stringSchema())
                        .optionalProperty("gradientStart", stringSchema())
                        .optionalProperty("gradientEnd", stringSchema())
                        .optionalProperty("backgroundImage", stringSchema())
                        .optionalProperty("showLogo", booleanSchema())
                        .optionalProperty("logoSize", stringSchema())
                        .optionalProperty("showServerIp", booleanSchema())
                        .optionalProperty("countdownStyle", stringSchema())
                        .optionalProperty("actions", arraySchema())
                        .optionalProperty("openingEffect", stringSchema())
                        .optionalProperty("pagePath", stringSchema())
                        .optionalProperty("showInNav", booleanSchema())
                        .optionalProperty("navLinkText", stringSchema())
                        .optionalProperty("showAdminLogin", booleanSchema())
                        .optionalProperty("coverDismissable", booleanSchema())
                        .optionalProperty("coverScope", stringSchema())
                )
            )
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageCountdownTimerPermission(), context)

        val body = context.body().asJsonObject()
        configManager.saveConfig(body)

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
