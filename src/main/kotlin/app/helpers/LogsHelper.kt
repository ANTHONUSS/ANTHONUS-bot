package app.helpers

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LogsHelper {
    val log: Logger = LoggerFactory.getLogger(LogsHelper::class.java)

    private fun context(event: GenericEvent): String = when (event) {
        is GenericCommandInteractionEvent -> {
            val user = event.user.name
            val guild = event.guild?.name ?: "DM"
            "@$user from $guild"
        }
        is MessageReceivedEvent -> {
            val user = event.author.name
            val guild = if (event.isFromGuild) event.guild.name else "DM"
            "@$user from $guild"
        }
        else -> {
            "event=${event.javaClass.simpleName}"
        }
    }

    fun success(event: GenericEvent, message: String) {
        log.info("${context(event)} | $message")
    }

    fun failure(event: GenericEvent, message: String, err: Throwable) {
        log.error("${context(event)} | $message", err)
    }

}