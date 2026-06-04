package dev.keystonemc.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class CommandResult {
    private final String command;
    private final boolean successful;
    private final List<Issue> issues;
    private final List<String> details;

    public CommandResult(String command, boolean successful, List<Issue> issues, List<String> details) {
        this.command = Objects.requireNonNull(command, "command");
        this.successful = successful;
        this.issues = List.copyOf(issues);
        this.details = List.copyOf(details);
    }

    public static Builder builder(String command) {
        return new Builder(command);
    }

    public String command() {
        return command;
    }

    public boolean successful() {
        return successful;
    }

    public List<Issue> issues() {
        return issues;
    }

    public List<String> details() {
        return details;
    }

    public boolean hasErrors() {
        return issues.stream().anyMatch(issue -> issue.severity() == Severity.ERROR);
    }

    public static final class Builder {
        private final String command;
        private final List<Issue> issues = new ArrayList<>();
        private final List<String> details = new ArrayList<>();

        private Builder(String command) {
            this.command = command;
        }

        public Builder issue(Issue issue) {
            issues.add(Objects.requireNonNull(issue, "issue"));
            return this;
        }

        public Builder issues(List<Issue> issues) {
            this.issues.addAll(issues);
            return this;
        }

        public Builder detail(String detail) {
            details.add(Objects.requireNonNull(detail, "detail"));
            return this;
        }

        public CommandResult build() {
            boolean successful = issues.stream().noneMatch(issue -> issue.severity() == Severity.ERROR);
            return new CommandResult(command, successful, Collections.unmodifiableList(issues), Collections.unmodifiableList(details));
        }
    }
}
