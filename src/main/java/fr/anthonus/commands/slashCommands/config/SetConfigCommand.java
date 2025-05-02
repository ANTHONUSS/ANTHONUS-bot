package fr.anthonus.commands.slashCommands.config;

import fr.anthonus.logs.LOGs;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.ServerManager;
import fr.anthonus.utils.json.SettingJson;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SetConfigCommand extends Command {
    private final String parametre;
    private final String valeur;

    public SetConfigCommand(SlashCommandInteractionEvent event, String parametre, String valeur) {
        super(event);
        this.parametre = parametre;
        this.valeur = valeur;

        LOGs.sendLog("Commande /set-config initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {

        if (!parametreValide()) {
            currentEvent.reply("## :x: Le paramètre entré n'existe pas.").setEphemeral(true).queue();
            LOGs.sendLog("Paramètre invalide : " + parametre, DefaultLogType.ERROR);
            return;
        }

        if (!valeurValide()) {
            currentEvent.reply("## :x: La valeur entrée pour le paramètre n'est pas valide.").setEphemeral(true).queue();
            LOGs.sendLog("valeur invalide : " + parametre, DefaultLogType.ERROR);
            return;
        }

        SettingJson settingJson = ServerManager.servers.get(currentEvent.getGuild().getIdLong()).getSettingJson();
        switch (parametre) {
            case "autoCommandProbability" -> {
                int probabilite = Integer.parseInt(valeur);
                settingJson.setAutoCommandProbability(probabilite);
            }
            case "allowFeur" -> {
                boolean feur = Boolean.parseBoolean(valeur);
                settingJson.setAllowFeur(feur);
            }
            case "allowReply" -> {
                boolean reply = Boolean.parseBoolean(valeur);
                settingJson.setAllowReply(reply);
            }
            case "allowModify" -> {
                boolean modify = Boolean.parseBoolean(valeur);
                settingJson.setAllowModify(modify);
            }
            case "timeBeforeResetQueue" -> {
                int temps = Integer.parseInt(valeur);
                settingJson.setTimeBeforeResetQueue(temps);
            }
        }

        settingJson.saveJson();

        currentEvent.reply("## :white_check_mark: Le paramètre a bien été changé et enregistré").queue();
        LOGs.sendLog("Paramètre " + parametre + " changé en " + valeur + " pour le serveur " + currentEvent.getGuild().getName(), CustomLogType.FILE_LOADING);
    }

    private boolean parametreValide() {
        return switch (parametre) {
            case "autoCommandProbability", "timeBeforeResetQueue", "allowFeur", "allowReply", "allowModify" -> true;
            default -> false;
        };
    }

    private boolean valeurValide() {
        switch (parametre) {
            case "timeBeforeResetQueue" -> {
                for (int i = 1; i < 11; i++) {
                    if (valeur.equals(String.valueOf(i))) return true;
                    if (valeur.equals(i + " heures")) return true;
                    if (valeur.equals(i + " heure")) return true;
                    if (valeur.equals(i + " h")) return true;
                    if (valeur.equals(i + "h")) return true;
                }
            }
            case "autoCommandProbability" -> {
                for (int i = 0; i < 101; i++) {
                    if (valeur.equals(String.valueOf(i))) return true;
                    if (valeur.equals(i + "%")) return true;
                    if (valeur.equals(i + " %")) return true;
                }
            }
            case "allowFeur", "allowReply", "allowModify" -> {
                if (valeur.equals("true")) return true;
                if (valeur.equals("false")) return true;
            }
        }
        return false;
    }
}
