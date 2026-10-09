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
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import com.panomc.platform.schema.EndpointDoc
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class GetCountdownTimerConfigAPI(
    private val plugin: CountdownTimerPlugin,
) : Api() {
    override val paths = listOf(Path("/config", RouteType.GET))

    override val doc = EndpointDoc(
        summary = "The countdown settings: end date, texts, colours and where the countdown shows.",
        tag = "countdown-timer",
        response = objectSchema()
            .requiredProperty("enabled", booleanSchema())
            .requiredProperty("endDate", intSchema())
            .requiredProperty("title", stringSchema())
            .requiredProperty("message", stringSchema())
            .requiredProperty("displayMode", stringSchema())
            .requiredProperty("theme", stringSchema())
            .requiredProperty("backgroundColor", stringSchema())
            .requiredProperty("textColor", stringSchema())
            .requiredProperty("gradientStart", stringSchema())
            .requiredProperty("gradientEnd", stringSchema())
            .requiredProperty("backgroundImage", stringSchema())
            .requiredProperty("showLogo", booleanSchema())
            .requiredProperty("logoSize", stringSchema())
            .requiredProperty("showServerIp", booleanSchema())
            .requiredProperty("countdownStyle", stringSchema())
            .requiredProperty("actions", arraySchema().items(stringSchema()))
            .requiredProperty("openingEffect", stringSchema())
            .requiredProperty("pagePath", stringSchema())
            .requiredProperty("showInNav", booleanSchema())
            .requiredProperty("navLinkText", stringSchema())
            .requiredProperty("showAdminLogin", booleanSchema())
            .requiredProperty("coverDismissable", booleanSchema())
            .requiredProperty("coverScope", stringSchema())
    )

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
