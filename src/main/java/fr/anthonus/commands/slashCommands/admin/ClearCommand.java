package fr.anthonus.commands.slashCommands.admin;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearCommand extends Command {
    private int amount;


    public ClearCommand(SlashCommandInteractionEvent event, int amount) {
        super(event);
        this.amount = amount;

        LOGs.sendLog("Commande /clear initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        TextChannel channel = (TextChannel) currentEvent.getChannel();
        channel.getHistory().retrievePast(amount).queue(messages -> {
            channel.deleteMessages(messages).queue(
                    success -> {
                        currentEvent.reply("## ✅ " + amount + " messages ont été supprimés").setEphemeral(true).queue();
                        LOGs.sendLog("Clear effectué"
                                            + "\nUser : @" + currentEvent.getUser().getName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName()
                                            + "\nAmount : " + amount,
                                CustomLogType.COMMAND);
                    },
                    failure -> {
                        currentEvent.reply("## ❌ Impossible de supprimer les messages : " + failure.getMessage()).setEphemeral(true).queue();
                        LOGs.sendLog("Erreur sur la commande clear"
                                            + "\nUser : @" + currentEvent.getUser().getName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName()
                                            + "\nAmount : " + amount,
                                DefaultLogType.ERROR);
                    }
            );
        });
    }
}
