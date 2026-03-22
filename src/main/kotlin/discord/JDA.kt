package discord

import club.minnced.discord.jdave.interop.JDaveSessionFactory
import discord.commands.CommandRegistry
import discord.listeners.AutoCompleteSlashCommandListener
import discord.listeners.MessageListener
import helpers.LogsHelper
import helpers.SettingsHelper
import discord.listeners.SlashCommandListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.audio.AudioModuleConfig
import net.dv8tion.jda.api.requests.GatewayIntent
import java.time.Duration

object JDA {
    lateinit var jda: net.dv8tion.jda.api.JDA

    fun start() {
        LogsHelper.log.info("Initializing the bot...")

        jda = JDABuilder.createDefault(SettingsHelper.discordToken)
            .enableIntents(
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES
            )
            .addEventListeners(
                SlashCommandListener(),
                MessageListener(),
                AutoCompleteSlashCommandListener()
            )
            .setAudioModuleConfig(
                AudioModuleConfig()
                    .withDaveSessionFactory(JDaveSessionFactory())
            )
            .build()

        jda.awaitReady()
        LogsHelper.log.info("Bot started")

        LogsHelper.log.info("loading commands...")

        val updater = jda.updateCommands()
        updater.addCommands(
            CommandRegistry.all().map {
                it.toSlashCommandData()
            }
        )

        updater.queue(
            { LogsHelper.log.info("Commands registered") },
            { err -> LogsHelper.log.error("Failed to register commands", err) }
        )
    }

    fun stop() {
        if (!::jda.isInitialized) {
            LogsHelper.log.warn("JDA is not initialized, nothing to stop")
            return
        }

        LogsHelper.log.info("Shutting down the bot...")
        jda.shutdown()

        if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
            LogsHelper.log.warn("JDA did not stop in time, forcing shutdown")
            jda.shutdownNow()
        }

        LogsHelper.log.info("Bot shutdown successfully")
    }

}