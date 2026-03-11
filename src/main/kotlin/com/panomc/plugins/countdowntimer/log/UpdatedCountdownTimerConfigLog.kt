package com.panomc.plugins.countdowntimer.log

import com.panomc.platform.db.model.PluginActivityLog
import io.vertx.core.json.JsonObject

class UpdatedCountdownTimerConfigLog(
    userId: Long,
    username: String,
    pluginId: String,
) : PluginActivityLog(
    userId = userId,
    pluginId = pluginId,
    details = JsonObject().put("username", username)
)
