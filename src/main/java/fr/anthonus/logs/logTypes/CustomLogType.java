package fr.anthonus.logs.logTypes;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.exceptions.LogTypeNameException;
import fr.anthonus.logs.exceptions.RVBFormatException;

import java.awt.*;

public enum CustomLogType implements LogType {
    LOADING("LOADING", LOGs.createAnsiCode(53, 74, 255, false, false, false)),
    FILE_LOADING("FILE_LOADING", LOGs.createAnsiCode(0, 0, 0, 130, 0, 255, false, false, false)),
    AUTOCOMMAND("AUTOCOMMAND", LOGs.createAnsiCode(193, 92, 255, false, false, false)),
    COMMAND("COMMAND", LOGs.createAnsiCode(255, 172, 53, false, false, false)),
    API("API", LOGs.createAnsiCode(53, 255, 255, false, false, false)),
    DOWNLOAD("DOWNLOAD", LOGs.createAnsiCode(0, 0, 0, 141, 255, 252, false, false, false)),
    DEBUG("DEBUG", LOGs.createAnsiCode(255, 171, 247, false, false, false));

    private final String name;
    private final String ansiCode;

    CustomLogType(String name, String ansiCode) {
        if (name == null || name.isEmpty())
            throw new LogTypeNameException("The log type name cannot be null or empty.");
        this.name = name;

        if (ansiCode == null || ansiCode.isEmpty()) {
            throw new RVBFormatException("The ANSI code cannot be null or empty.");
        }

        if (!ansiCode.matches(LOGs.ANSI_REGEX)) {
            throw new RVBFormatException("The ANSI code is not valid for " + name + ".");
        }

        this.ansiCode = ansiCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAnsiCode() {
        return ansiCode;
    }
}
