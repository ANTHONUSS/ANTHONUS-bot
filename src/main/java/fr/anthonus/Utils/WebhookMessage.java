package fr.anthonus.Utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

public class WebhookMessage {
    private TextChannel channel;
    private String userName;
    private String avatarUrl;

    private String message;

    private WebhookClient webhookClient;


    public WebhookMessage(SlashCommandInteractionEvent event, String message) {
        this.message = message;
        channel = event.getChannel().asTextChannel();
        userName = event.getMember().getUser().getEffectiveName();
        avatarUrl = event.getMember().getUser().getAvatarUrl();
    }

    public WebhookMessage(MessageReceivedEvent event, String message) {
        this.message = message;
        channel = event.getChannel().asTextChannel();
        userName = event.getMember().getUser().getEffectiveName();
        avatarUrl = event.getMember().getUser().getAvatarUrl();
    }

    public void send() {
        channel.retrieveWebhooks().queue(webhooks -> {
                    if (webhooks.isEmpty()) {
                        WebhookAction webhookAction = channel.createWebhook("fakeBotWebhook");
                        webhookAction.queue(hook -> {
                            webhookClient = WebhookClient.withUrl(hook.getUrl());
                            sendFakeMessage(webhookClient);
                        });
                    } else {
                        webhookClient = WebhookClient.withUrl(webhooks.get(0).getUrl());
                        sendFakeMessage(webhookClient);
                    }
                }
        );
    }

    private void sendFakeMessage(WebhookClient webhookClient) {
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setUsername(userName)
                .setAvatarUrl(avatarUrl)
                .setContent(message);

        webhookClient.send(messageBuilder.build());
    }
}
