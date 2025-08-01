package fr.anthonus.listeners;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.Utils;
import fr.anthonus.utils.settings.SettingsLoader;
import fr.anthonus.utils.api.OpenAIAPI;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
        event.getChannel().sendTyping().queue();

        Message message = event.getMessage();

        String rawMessage = message.getContentRaw();
        Guild guild = event.getGuild();
        rawMessage = Utils.replaceIDsByNickname(guild, rawMessage);

        String messageContent = "[MESSAGE PRINCIPAL] Message de " + message.getAuthor().getEffectiveName() + " : " + rawMessage;

        MessageReference ref = message.getMessageReference();
        if (ref != null) {
            String referencedMessageContent = ref.getMessage().getContentRaw();
            messageContent += "\n[CITATION RAJOUTÉE] Message de " + ref.getMessage().getAuthor().getEffectiveName() + " : " + referencedMessageContent;
        }

        String personality = SettingsLoader.getFastTalkPersonnality();

        event.getChannel().sendTyping().queue();
        String response = OpenAIAPI.getChatGPTResponse(personality, messageContent);

        message.reply(response).queue();
    }

}
