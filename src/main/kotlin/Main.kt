import helpers.LogsHelper.log
import helpers.SettingsHelper
import discord.JDA
import helpers.LogsHelper
import io.github.cdimascio.dotenv.DotenvException
import java.util.Scanner
import kotlin.system.exitProcess

fun main() {
    SettingsHelper.loadEnv()
    JDA.start()

    val scanner = Scanner(System.`in`)
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
            else -> log.info("Unrecognized command.")
        }
    }
}