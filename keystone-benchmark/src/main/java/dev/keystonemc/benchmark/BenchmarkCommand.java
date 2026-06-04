package dev.keystonemc.benchmark;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.GradleRunner;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class BenchmarkCommand implements KeystoneCommand {
    private final GradleRunner gradleRunner = new GradleRunner();

    @Override
    public String name() {
        return "benchmark";
    }

    @Override
    public String description() {
        return "Measure repeated Gradle task execution time.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        String task = option(args, "--task", "build");
        int iterations = Integer.parseInt(option(args, "--iterations", "3"));
        List<Long> measurements = new ArrayList<>();
        CommandResult.Builder result = CommandResult.builder(name()).detail("task=" + task).detail("iterations=" + iterations);

        for (int i = 1; i <= iterations; i++) {
            long start = System.nanoTime();
            GradleRunner.ProcessResult process = gradleRunner.run(context, List.of(task), Duration.ofMinutes(20));
            long elapsedMillis = Duration.ofNanos(System.nanoTime() - start).toMillis();
            measurements.add(elapsedMillis);
            result.detail("iteration " + i + "=" + elapsedMillis + "ms");
            if (process.timedOut()) {
                result.issue(Issue.error("benchmark.timeout", "Gradle task timed out during iteration " + i + ".", context.root()));
                break;
            }
            if (process.exitCode() != 0) {
                result.issue(Issue.error("benchmark.gradle.failed", "Gradle task failed during iteration " + i + " with exit code " + process.exitCode() + ".", context.root()));
                break;
            }
        }

        if (!measurements.isEmpty()) {
            long average = Math.round(measurements.stream().mapToLong(Long::longValue).average().orElse(0));
            result.detail("average=" + average + "ms");
        }
        return result.build();
    }

    private String option(List<String> args, String name, String fallback) {
        int index = args.indexOf(name);
        if (index >= 0 && index + 1 < args.size()) {
            return args.get(index + 1);
        }
        return fallback;
    }
}
