package dev.keystonemc.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class ProjectContext {
    private final Path root;

    public ProjectContext(Path root) {
        this.root = Objects.requireNonNull(root, "root").toAbsolutePath().normalize();
    }

    public Path root() {
        return root;
    }

    public Path resolve(String relativePath) {
        return root.resolve(relativePath).normalize();
    }

    public boolean exists(String relativePath) {
        return Files.exists(resolve(relativePath));
    }

    public boolean isGradleProject() {
        return exists("settings.gradle") || exists("settings.gradle.kts") || exists("build.gradle") || exists("build.gradle.kts");
    }
}
