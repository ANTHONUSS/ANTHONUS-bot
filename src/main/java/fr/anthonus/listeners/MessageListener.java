package fr.anthonus.listeners;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.SettingsLoader;
import fr.anthonus.utils.api.OpenAIAPI;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        String message = event.getMessage().getContentRaw();

        if (message.toLowerCase().matches(".*\\bquoi\\s?\\p{Punct}*$")) {
            handleFeur(event);
        }

        String botPing = event.getJDA().getSelfUser().getAsMention();

        if (message.startsWith(botPing)) {
            handleFastTalk(event);
        }
    }

    private void handleFeur(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();

        if (!ServerManager.getServer(guildId).isAllowFeur()) return;

        if (Math.random() >= 0.5) {
            event.getMessage().reply("feur").queue();
        } else {
            event.getMessage().reply("coubeh").queue();
        }

        LOGs.sendLog("Feur envoyé à @" + event.getAuthor().getEffectiveName(), DefaultLogType.AUTOCOMMAND);
    }

    private void handleFastTalk(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String messageContent = "[MESSAGE PRINCIPAL] Message de " + message.getAuthor().getEffectiveName() + " : " + message.getContentRaw();

        MessageReference ref = message.getMessageReference();
        if (ref != null) {
            String referencedMessageContent = ref.getMessage().getContentRaw();
            messageContent += "\n[CITATION RAJOUTÉE] Message de " + ref.getMessage().getAuthor().getEffectiveName() + " : " + referencedMessageContent;
        }

        String personality = SettingsLoader.getFastTalkPersonnality();

        String response = OpenAIAPI.getChatGPTResponse(personality, messageContent);

        if (response == null) {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle(":x: ERREUR :x:");
            embed.setDescription("La personnalité de FastTalk n'est pas définie dans le fichier .txt");

            embed.setColor(Color.RED);

            message.replyEmbeds(embed.build()).queue();
            return;
        }

        message.reply(response).queue();
    }
}
