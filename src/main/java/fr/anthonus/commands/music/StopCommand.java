package fr.anthonus.commands.music;

import fr.anthonus.Main;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class StopCommand extends Command {
    public StopCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /stop initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isThereACurrentTrack()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        ServerManager.getServer(guildID).getPlayerManager().setCurrentTrack(null);

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(":stop_button: Musique arrêtée :stop_button:");
        embed.setDescription("Le bot a été déconnecté du canal vocal.");

        embed.setColor(Color.RED);

        currentEvent.replyEmbeds(embed.build()).queue();
    }
}
