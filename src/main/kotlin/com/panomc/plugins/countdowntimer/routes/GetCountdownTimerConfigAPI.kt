package com.panomc.plugins.countdowntimer.routes

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.model.*
import com.panomc.plugins.countdowntimer.CountdownTimerPlugin
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import com.panomc.plugins.countdowntimer.config.TimerAction
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository

@Endpoint
class GetCountdownTimerConfigAPI(
    private val plugin: CountdownTimerPlugin,
) : Api() {
    override val paths = listOf(Path("/api/countdown-timer/config", RouteType.GET))

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<CountdownTimerConfig>
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        val config = configManager.config

        // Auto-disable if timer has ended and HIDE action is configured
        if (config.enabled && config.endDate > 0 && config.endDate <= System.currentTimeMillis()) {
            if (config.actions.contains(TimerAction.HIDE)) {
                val updateBody = JsonObject().put("enabled", false)
                configManager.saveConfig(updateBody)

                // Return updated config with enabled = false
                val updatedConfig = configManager.config
                return Successful(JsonObject.mapFrom(updatedConfig).map)
            }
        }

        return Successful(JsonObject.mapFrom(config).map)
    }
}
