package discord.commands.music

import discord.commands.Command

class MusicCommand: Command() {
    override val name = "music"
    override val description = "Commandes relatives à la musique"
    override val subCommands = listOf(
        AddMusicCommand(),
        PlayMusicCommand(),
        StopMusicCommand(),
        RemoveMusicCommand(),
        ListMusicCommand()
    )
}