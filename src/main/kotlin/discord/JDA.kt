package discord

import club.minnced.discord.jdave.interop.JDaveSessionFactory
import discord.commands.CommandRegistry
import discord.listeners.AutoCompleteSlashCommandListener
import discord.listeners.MessageListener
import helpers.LogsHelper.log
import helpers.SettingsHelper
import discord.listeners.SlashCommandListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.audio.AudioModuleConfig
import net.dv8tion.jda.api.requests.GatewayIntent
import java.time.Duration

object JDA {
    lateinit var jda: net.dv8tion.jda.api.JDA

    fun start() {
        log.info("Initializing the bot...")

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
        log.info("Bot started")

        log.info("loading commands...")

        val updater = jda.updateCommands()
        updater.addCommands(
            CommandRegistry.all().map {
                it.toSlashCommandData()
            }
        )

        updater.queue(
            { log.info("Commands registered") },
            { err -> log.error("Failed to register commands", err) }
        )
    }

    fun stop() {
        if (!::jda.isInitialized) {
            log.warn("JDA is not initialized, nothing to stop")
            return
        }

        log.info("Shutting down the bot...")
        jda.shutdown()

        if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
            log.warn("JDA did not stop in time, forcing shutdown")
            jda.shutdownNow()
        }

        log.info("Bot shutdown successfully")
    }

}