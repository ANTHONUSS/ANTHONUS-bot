package fr.anthonus.commands.autoCommands;

import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FeurCommand extends AutoCommand {

    public FeurCommand(MessageReceivedEvent event) {
        super(event);

        LOGs.sendLog("AutoCommande feur intilisaliée", "AUTOCOMMAND");
    }

    @Override
    public void run() {

        if (!ServerManager.servers.get(currentEvent.getGuild().getIdLong()).getSettingJson().isAllowFeur()) {
            LOGs.sendLog("AutoCommande feur désactivée", "AUTOCOMMAND");
            return;
        }

        if (Math.random() >= 0.5) currentEvent.getMessage().reply("feur").queue();
        else currentEvent.getMessage().reply("coubeh").queue();

        LOGs.sendLog("feur envoyé"
                        + "\nUser : @" + currentEvent.getAuthor().getName()
                        + "\nServeur : " + currentEvent.getGuild().getName()
                        + "\nSalon : #" + currentEvent.getChannel().getName(),
                "AUTOCOMMAND");
    }
}
