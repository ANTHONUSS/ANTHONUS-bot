package app.discord.listeners

import app.discord.commands.CommandRegistry
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SlashCommandListener: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (!event.isFromGuild) {
            event.reply("Impossible d'utiliser de commandes dans les mp du bot.")
                .setEphemeral(true)
                .queue()
            return
        }

        val command = CommandRegistry.find(event.name)
        if (command == null) {
            event.reply("Commande inconnue")
                .setEphemeral(true)
                .queue()
            return
        }

        command.execute(event)
    }


}