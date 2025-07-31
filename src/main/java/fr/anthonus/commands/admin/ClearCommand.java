package fr.anthonus.commands.admin;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class ClearCommand extends Command {
    private int amount;


    public ClearCommand(SlashCommandInteractionEvent event, int amount) {
        super(event);
        this.amount = amount;

        LOGs.sendLog("Commande /clear initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        TextChannel channel = (TextChannel) currentEvent.getChannel();
        channel.getHistory().retrievePast(amount).queue(messages -> {
            channel.deleteMessages(messages).queue(
                    success -> {
                        EmbedBuilder embed = new EmbedBuilder();

                        embed.setTitle(":white_check_mark: Suppression de messages réussie :white_check_mark:");
                        embed.setDescription("**" + amount + "** messages ont été supprimés.");

                        embed.setColor(Color.GREEN);

                        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

                        LOGs.sendLog(amount + " messages supprimés", DefaultLogType.COMMAND);
                    },
                    failure -> {
                        EmbedBuilder embed = new EmbedBuilder();

                        embed.setTitle(":x: ERREUR :x:");
                        embed.setDescription("Impossible de supprimer les messages : " + failure.getMessage());

                        embed.setColor(Color.RED);

                        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

                        LOGs.sendLog("Impossible de supprimer les messages", DefaultLogType.ERROR);
                    }
            );
        });
    }
}
