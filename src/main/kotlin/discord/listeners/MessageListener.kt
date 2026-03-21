package discord.listeners

import helpers.LogsHelper
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.random.Random

class MessageListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (!event.isFromGuild) return

        handleFeur(event)
    }

    private fun handleFeur(event: MessageReceivedEvent) {
        val messageString = event.message.contentRaw

        val regex = ".*\\bquoi\\s*\\p{Punct}*$".toRegex(RegexOption.IGNORE_CASE)
        if (regex.matches(messageString)) {

            if (Random.nextInt(2) == 0) {
                event.message.reply("feur")
                    .queue(
                        { LogsHelper.success(event, "Feur sent") },
                        { err -> LogsHelper.failure(event, "Error while sending feur", err) }
                    )
            } else {
                event.message.reply("coubeh")
                    .queue(
                        { LogsHelper.success(event, "coubeh sent") },
                        { err -> LogsHelper.failure(event, "Error while sending coubeh", err) }
                    )
            }
        }
    }
}