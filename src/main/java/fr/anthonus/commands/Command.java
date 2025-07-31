package fr.anthonus.commands;

import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public abstract class Command {
    protected SlashCommandInteractionEvent currentEvent;

    public Command(SlashCommandInteractionEvent event) {
        currentEvent = event;
    }

    public abstract void run();

    protected boolean isUserInVoiceChannel() {
        AudioChannelUnion voiceChannel = currentEvent.getMember().getVoiceState().getChannel();
        if (voiceChannel == null) {
            LOGs.sendLog("Aucun salon vocal trouvé pour l'utilisateur", DefaultLogType.WARNING);

            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle(":warning: ATTENTION :warning:");
            embed.setDescription("Vous devez être dans un salon vocal pour utiliser cette commande.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

            return false;
        }
        return true;
    }
}
