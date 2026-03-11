package com.panomc.plugins.countdowntimer.routes

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.model.Api
import com.panomc.platform.model.Path
import com.panomc.platform.model.Result
import com.panomc.platform.model.RouteType
import com.panomc.platform.util.MimeTypeUtil
import com.panomc.plugins.countdowntimer.CountdownTimerPlugin
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.Parameters.param
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.stringSchema
import java.io.File

@Endpoint
class GetCountdownTimerBackgroundAPI(
    private val plugin: CountdownTimerPlugin,
) : Api() {
    override val paths = listOf(Path("/api/countdown-timer/background/:fileName", RouteType.GET))

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<CountdownTimerConfig>
    }

    companion object {
        private const val CACHE_TTL_SECONDS = 7 * 24 * 60 * 60 // 1 week
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .pathParameter(param("fileName", stringSchema()))
            .build()

    override suspend fun handle(context: RoutingContext): Result? {
        val parameters = getParameters(context)
        val fileName = parameters.pathParameter("fileName").string

        val config = configManager.config

        if (config.backgroundImage != fileName) {
            context.response().setStatusCode(404).end()
            return null
        }

        val file = File(plugin.uploadsDir, fileName)

        if (!file.exists()) {
            context.response().setStatusCode(404).end()
            return null
        }

        val mimeType = MimeTypeUtil.getMimeTypeFromFileName(file.absolutePath)

        val response = context.response()
        response.putHeader("Content-Type", mimeType)
        response.putHeader("Cache-Control", "public, max-age=$CACHE_TTL_SECONDS, immutable")

        try {
            response.sendFile(file.absolutePath)
        } catch (_: Exception) {}

        return null
    }
}
