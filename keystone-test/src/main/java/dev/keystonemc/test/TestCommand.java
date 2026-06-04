package dev.keystonemc.test;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.GradleRunner;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.time.Duration;
import java.util.List;

public final class TestCommand implements KeystoneCommand {
    private final GradleRunner gradleRunner = new GradleRunner();

    @Override
    public String name() {
        return "test";
    }

    @Override
    public String description() {
        return "Run the host project's Gradle tests.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        String task = args.isEmpty() ? "test" : args.getFirst();
        GradleRunner.ProcessResult process = gradleRunner.run(context, List.of(task), Duration.ofMinutes(20));
        CommandResult.Builder builder = CommandResult.builder(name()).detail("task=" + task).detail("exitCode=" + process.exitCode());
        if (process.timedOut()) {
            builder.issue(Issue.error("test.timeout", "Gradle test task timed out.", context.root()));
        } else if (process.exitCode() != 0) {
            builder.issue(Issue.error("test.failed", "Gradle test task failed with exit code " + process.exitCode() + ".", context.root()));
        }
        return builder.build();
    }
}
