package helpers

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import java.io.IOException
import java.nio.charset.StandardCharsets

object SettingsHelper {
    val version: String = try {
        SettingsHelper::class.java
            .getResourceAsStream("/version.txt")
            ?.bufferedReader(StandardCharsets.UTF_8)
            ?.use { it.readText().trim() }
            ?: "unknown"
    } catch (_: IOException) {
        "unknown"
    }

    var discordToken: String? = null

    fun loadEnv() {
        val dotenv = Dotenv.configure()
            .directory("conf")
            .load()

        LogsHelper.log.info("Loading discord bot token...")
        discordToken = dotenv.get("DISCORD_TOKEN")
        if (discordToken.isNullOrEmpty()) {
            throw DotenvException("Discord token not found in .env file")
        } else {
            LogsHelper.log.info("Discord token loaded successfully")
        }
    }
}