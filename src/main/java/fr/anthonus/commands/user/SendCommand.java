package fr.anthonus.commands.user;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;

public class SendCommand extends Command {
    private final User user;
    private final String message;
    private final Message.Attachment attachment;

    public SendCommand(SlashCommandInteractionEvent event, User user, String message, Message.Attachment attachment) {
        super(event);

        this.user = user;
        this.message = message;
        this.attachment = attachment;

        LOGs.sendLog("Commande /send initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        currentEvent.deferReply().setEphemeral(true).queue();

        FileUpload fileUpload;
        if (attachment != null) {
            fileUpload = FileUpload.fromData(attachment.getProxy().download().join(), attachment.getFileName());
        } else {
            fileUpload = null;
        }

        user.openPrivateChannel().queue(privateChannel -> {
            if (message != null && attachment != null) {
                privateChannel.sendMessage(message).addFiles(fileUpload).queue();
            } else if (message != null) {
                privateChannel.sendMessage(message).queue();
            } else if (fileUpload != null) {
                privateChannel.sendFiles(fileUpload).queue();
            }
        });

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(":incoming_envelope: Message envoyé :incoming_envelope:");
        embed.setDescription("Votre message a été envoyé avec succès à " + user.getAsMention() + ".");
        if (message != null) {
            embed.addField("Message", "`" + message + "`", false);
        }
        if (attachment != null) {
            embed.addField("Fichier joint", "`" +  attachment.getFileName() + "`", false);
        }
        embed.setColor(Color.GREEN);
        currentEvent.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
    }

}
