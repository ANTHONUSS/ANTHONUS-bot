package fr.anthonus.listeners;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.Utils;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.settings.SettingsLoader;
import fr.anthonus.utils.api.OpenAIAPI;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String messageString = message.getContentRaw();
        Server server = ServerManager.getServer(event.getGuild().getIdLong());
        long channelId = event.getChannel().getIdLong();

        String messageToAdd = "[MESSAGE DE " + message.getAuthor().getEffectiveName() + "] " + messageString;
        MessageReference ref = message.getMessageReference();
        if (ref != null) {
            String referencedMessageContent = ref.getMessage().getContentRaw();
            messageToAdd+= "\n[REFERENCING " + ref.getMessage().getAuthor().getEffectiveName() + "] " + referencedMessageContent;
        }
        messageToAdd = Utils.replaceIDsByNickname(event.getGuild(), messageToAdd);

        server.addMessageToHistory(channelId, messageToAdd);

        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        if (messageString.toLowerCase().matches(".*\\bquoi\\s?\\p{Punct}*$")) {
            handleFeur(event);
        }

        String botPing = event.getJDA().getSelfUser().getAsMention();
        if (messageString.startsWith(botPing)) {
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
        event.getChannel().sendTyping().queue();
        Server server = ServerManager.getServer(event.getGuild().getIdLong());
        long channelId = event.getChannel().getIdLong();

        String personality = SettingsLoader.getFastTalkPersonnality();

        event.getChannel().sendTyping().queue();

        List<String> messageHistory = server.getMessageHistory().get(channelId);

        for (String message : messageHistory) {
            LOGs.sendLog(message, DefaultLogType.DEBUG);
        }

        String response = OpenAIAPI.getChatGPTResponse(personality, messageHistory);

        if (response.length() > 2000) {
            String ending = "\n-#(Message tronqué car trop long)";
            response = response.substring(0, 2000 - ending.length()) + ending;
        }

        event.getMessage().reply(response).queue();
    }

}
