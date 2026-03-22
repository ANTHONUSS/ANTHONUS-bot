import discord.JDA
import helpers.LogsHelper
import helpers.SettingsHelper
import java.util.*
import kotlin.system.exitProcess

fun main() {
    SettingsHelper.loadEnv()
    JDA.start()

    val scanner = Scanner(System.`in`)
    if (System.console() != null) {
        while (scanner.hasNextLine()) {
            val input = scanner.nextLine()

            when (input) {
                "stop" -> {
                    JDA.stop()
                    exitProcess(0)
                }

                "restart" -> {
                    JDA.stop()
                    JDA.start()
                }

                else -> LogsHelper.log.info("Unrecognized command.")
            }
        }

    } else {
        try {
            Thread.currentThread().join()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}