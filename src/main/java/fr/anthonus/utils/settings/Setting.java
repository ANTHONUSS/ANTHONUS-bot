package fr.anthonus.utils.settings;

public class Setting {
    public static int autoCommandProbability;

    public static boolean allowFeur;
    public static boolean allowReply;

    public static int timeBeforeResetQueue;

    public Setting(int autoCommandProbability, boolean allowFeur, boolean allowReply, int timeBeforeResetQueue) {
        this.autoCommandProbability = autoCommandProbability;
        this.allowFeur = allowFeur;
        this.allowReply = allowReply;
        this.timeBeforeResetQueue = timeBeforeResetQueue;
    }
}
