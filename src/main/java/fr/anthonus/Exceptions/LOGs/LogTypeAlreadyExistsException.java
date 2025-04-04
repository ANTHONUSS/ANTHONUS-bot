package fr.anthonus.Exceptions.LOGs;

public class LogTypeAlreadyExistsException extends RuntimeException {
    public LogTypeAlreadyExistsException(String type) {
        super("The log type " + type + " already exists.");
    }
}
