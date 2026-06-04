package dev.keystonemc.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class GradleRunner {
    public ProcessResult run(ProjectContext context, List<String> arguments, Duration timeout) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        Path wrapper = context.resolve(isWindows() ? "gradlew.bat" : "gradlew");
        if (Files.exists(wrapper)) {
            command.add(wrapper.toString());
        } else {
            command.add(isWindows() ? "gradle.bat" : "gradle");
        }
        command.addAll(arguments);

        Process process = new ProcessBuilder(command)
            .directory(context.root().toFile())
            .redirectErrorStream(true)
            .start();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Thread reader = Thread.ofVirtual().start(() -> {
            try (var input = process.getInputStream()) {
                input.transferTo(output);
            } catch (IOException ignored) {
                // Process failures are reported through the exit code and captured output.
            }
        });

        boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            reader.join();
            return new ProcessResult(-1, output.toString(StandardCharsets.UTF_8), true);
        }

        reader.join();
        return new ProcessResult(process.exitValue(), output.toString(StandardCharsets.UTF_8), false);
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public record ProcessResult(int exitCode, String output, boolean timedOut) {
    }
}
