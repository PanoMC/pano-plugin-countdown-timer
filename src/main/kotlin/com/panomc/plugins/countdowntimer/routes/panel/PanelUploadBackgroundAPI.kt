package com.panomc.plugins.countdowntimer.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.db.DatabaseManager
import com.panomc.platform.error.BadRequest
import com.panomc.platform.model.*
import com.panomc.plugins.countdowntimer.CountdownTimerPlugin
import com.panomc.plugins.countdowntimer.config.CountdownTimerConfig
import com.panomc.plugins.countdowntimer.log.UpdatedCountdownTimerConfigLog
import com.panomc.plugins.countdowntimer.permission.ManageCountdownTimerPermission
import com.panomc.plugins.countdowntimer.util.BackgroundImageFiles
import com.panomc.plugins.countdowntimer.util.ImageSniffer
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.validation.RequestPredicate
import io.vertx.ext.web.validation.ValidationHandler
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import java.io.File

@Endpoint
class PanelUploadBackgroundAPI(
    private val plugin: CountdownTimerPlugin,
) : PanelApi() {
    override val paths = listOf(Path("/background", RouteType.POST))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val databaseManager by lazy {
        plugin.applicationContext.getBean(DatabaseManager::class.java)
    }

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<CountdownTimerConfig>
    }

    override fun bodyHandler(): Handler<RoutingContext> =
        BodyHandler.create()
            .setDeleteUploadedFilesOnEnd(true)
            .setBodyLimit(5 * 1024 * 1024) // 5MB

    override suspend fun getFailureHandler(context: RoutingContext) {
        if (context.failure() == null) {
            throw BadRequest()
        }
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .predicate(RequestPredicate.BODY_REQUIRED)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageCountdownTimerPermission(), context)

        val fileUploads = context.fileUploads()

        if (fileUploads.size != 1) {
            throw BadRequest()
        }

        val fileUpload = fileUploads.first()

        val allowedTypes = listOf("image/webp", "image/jpeg", "image/png", "image/gif")
        if (!allowedTypes.contains(fileUpload.contentType())) {
            throw BadRequest()
        }

        if (fileUpload.size() > 5 * 1024 * 1024) {
            throw BadRequest()
        }

        // The stored extension comes from the file's real content, never from the client's file name.
        val extension = ImageSniffer.detectExtension(File(fileUpload.uploadedFileName())) ?: throw BadRequest()

        // Delete old background if exists
        val oldImage = configManager.config.backgroundImage
        if (oldImage.isNotBlank()) {
            val oldFile = BackgroundImageFiles.resolve(plugin.uploadsDir, oldImage)
            if (oldFile != null && oldFile.exists()) {
                oldFile.delete()
            }
        }

        // Save new file
        val fileName = "bg-${System.currentTimeMillis()}.$extension"
        val destFile = File(plugin.uploadsDir, fileName)

        File(fileUpload.uploadedFileName()).copyTo(destFile, true)

        // Update config
        val updateBody = JsonObject().put("backgroundImage", fileName)
        configManager.saveConfig(updateBody)

        // Log activity
        val userId = authProvider.getUserIdFromRoutingContext(context)
        val sqlClient = databaseManager.getSqlClient()
        val username = databaseManager.userDao.getUsernameFromUserId(userId, sqlClient) ?: "Unknown"

        databaseManager.panelActivityLogDao.add(
            UpdatedCountdownTimerConfigLog(userId, username, plugin.pluginId),
            sqlClient
        )

        return Successful(mapOf("backgroundImage" to fileName))
    }
}
