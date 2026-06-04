package dev.keystonemc.cli;

import dev.keystonemc.benchmark.BenchmarkCommand;
import dev.keystonemc.compare.CompareCommand;
import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;
import dev.keystonemc.core.SimpleJson;
import dev.keystonemc.matrix.MatrixCommand;
import dev.keystonemc.profile.ProfileCommand;
import dev.keystonemc.publish.PublishCommand;
import dev.keystonemc.report.ReportCommand;
import dev.keystonemc.test.TestCommand;
import dev.keystonemc.validation.ValidateCommand;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class KeystoneCli {
    private final Map<String, KeystoneCommand> commands;

    public KeystoneCli(List<KeystoneCommand> commands) {
        this.commands = new LinkedHashMap<>();
        for (KeystoneCommand command : commands) {
            this.commands.put(command.name(), command);
        }
    }

    public static void main(String[] args) {
        int exitCode = new KeystoneCli(defaultCommands()).execute(List.of(args));
        System.exit(exitCode);
    }

    public int execute(List<String> rawArgs) {
        if (rawArgs.isEmpty() || rawArgs.contains("--help") || rawArgs.contains("-h")) {
            printHelp();
            return 0;
        }

        List<String> args = new ArrayList<>(rawArgs);
        boolean json = args.remove("--json");
        Path projectRoot = Path.of(".");
        int projectIndex = args.indexOf("--project");
        if (projectIndex >= 0 && projectIndex + 1 < args.size()) {
            projectRoot = Path.of(args.get(projectIndex + 1));
            args.remove(projectIndex + 1);
            args.remove(projectIndex);
        }

        String commandName = args.removeFirst();
        KeystoneCommand command = commands.get(commandName);
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            printHelp();
            return 2;
        }

        try {
            CommandResult result = command.run(new ProjectContext(projectRoot), args);
            printResult(result, json);
            return result.successful() ? 0 : 1;
        } catch (Exception exception) {
            System.err.println("Keystone command failed: " + exception.getMessage());
            return 1;
        }
    }

    private void printHelp() {
        System.out.println("Keystone - Minecraft developer quality assurance");
        System.out.println();
        System.out.println("Usage: keystone [--project <path>] [--json] <command> [args]");
        System.out.println();
        System.out.println("Commands:");
        for (KeystoneCommand command : commands.values()) {
            System.out.printf("  %-10s %s%n", command.name(), command.description());
        }
    }

    private void printResult(CommandResult result, boolean json) {
        if (json) {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("command", result.command());
            payload.put("successful", result.successful());
            payload.put("details", result.details());
            payload.put("issues", result.issues().stream().map(this::issueJson).toList());
            System.out.println(SimpleJson.stringify(payload));
            return;
        }

        System.out.println(result.command() + ": " + (result.successful() ? "PASS" : "FAIL"));
        for (String detail : result.details()) {
            System.out.println("  " + detail);
        }
        for (Issue issue : result.issues()) {
            String location = issue.path().map(path -> " [" + path + "]").orElse("");
            System.out.println("  " + issue.severity() + " " + issue.code() + ": " + issue.message() + location);
        }
    }

    private Map<String, Object> issueJson(Issue issue) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("severity", issue.severity().name());
        payload.put("code", issue.code());
        payload.put("message", issue.message());
        payload.put("path", issue.path().map(Path::toString).orElse(null));
        return payload;
    }

    private static List<KeystoneCommand> defaultCommands() {
        return List.of(
            new ValidateCommand(),
            new BenchmarkCommand(),
            new TestCommand(),
            new ProfileCommand(),
            new CompareCommand(),
            new MatrixCommand(),
            new ReportCommand(),
            new PublishCommand()
        );
    }
}
