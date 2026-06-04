package dev.keystonemc.profile;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class ProfileCommand implements KeystoneCommand {
    @Override
    public String name() {
        return "profile";
    }

    @Override
    public String description() {
        return "Profile generated jar artifacts for size and presence.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        Path libs = context.resolve("build/libs");
        CommandResult.Builder builder = CommandResult.builder(name());
        if (!Files.isDirectory(libs)) {
            return builder.issue(Issue.error("profile.artifacts.missing", "No build/libs directory exists. Run the host build first.", libs)).build();
        }

        try (Stream<Path> files = Files.list(libs)) {
            List<Path> jars = files.filter(path -> path.getFileName().toString().endsWith(".jar"))
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .toList();
            if (jars.isEmpty()) {
                builder.issue(Issue.error("profile.jars.missing", "No jar artifacts were found in build/libs.", libs));
            }
            for (Path jar : jars) {
                long bytes = Files.size(jar);
                builder.detail(jar.getFileName() + "=" + bytes + " bytes");
                if (bytes == 0) {
                    builder.issue(Issue.error("profile.jar.empty", "Jar artifact is empty.", jar));
                }
            }
        }
        return builder.build();
    }
}
