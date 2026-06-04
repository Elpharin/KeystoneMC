package dev.keystonemc.core;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public final class Issue {
    private final Severity severity;
    private final String code;
    private final String message;
    private final Path path;

    public Issue(Severity severity, String code, String message, Path path) {
        this.severity = Objects.requireNonNull(severity, "severity");
        this.code = Objects.requireNonNull(code, "code");
        this.message = Objects.requireNonNull(message, "message");
        this.path = path;
    }

    public static Issue info(String code, String message) {
        return new Issue(Severity.INFO, code, message, null);
    }

    public static Issue warning(String code, String message, Path path) {
        return new Issue(Severity.WARNING, code, message, path);
    }

    public static Issue error(String code, String message, Path path) {
        return new Issue(Severity.ERROR, code, message, path);
    }

    public Severity severity() {
        return severity;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Optional<Path> path() {
        return Optional.ofNullable(path);
    }
}
