package dev.keystonemc.validation;

import dev.keystonemc.core.Issue;
import dev.keystonemc.core.ProjectContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ValidationService {
    private static final Pattern JAVA_21_PATTERN = Pattern.compile("(JavaLanguageVersion\\.of\\(21\\)|sourceCompatibility\\s*=\\s*(JavaVersion\\.)?VERSION_21|options\\.release\\s*=\\s*21)");

    private final ProjectDetector detector = new ProjectDetector();

    public ValidationReport validate(ProjectContext context) throws IOException {
        List<Issue> issues = new ArrayList<>();
        ProjectType type = detector.detect(context);

        if (!context.isGradleProject()) {
            issues.add(Issue.error("gradle.missing", "Project does not expose a Gradle build file or settings file.", context.root()));
        }

        validateJavaVersion(context, issues);

        switch (type) {
            case FABRIC -> validateFabric(context, issues);
            case NEOFORGE -> validateNeoForge(context, issues);
            case PAPER -> validatePaper(context, issues);
            case UNKNOWN -> issues.add(Issue.warning("minecraft.loader.unknown", "No Fabric, NeoForge, or Paper metadata file was found.", context.root()));
        }

        return new ValidationReport(type, issues);
    }

    private void validateJavaVersion(ProjectContext context, List<Issue> issues) throws IOException {
        Path kotlinBuild = context.resolve("build.gradle.kts");
        Path groovyBuild = context.resolve("build.gradle");
        Path buildFile = Files.exists(kotlinBuild) ? kotlinBuild : groovyBuild;
        if (!Files.exists(buildFile)) {
            return;
        }
        String content = Files.readString(buildFile);
        if (!JAVA_21_PATTERN.matcher(content).find()) {
            issues.add(Issue.warning("java.version.unconfirmed", "Build file does not explicitly declare Java 21 compilation.", buildFile));
        }
    }

    private void validateFabric(ProjectContext context, List<Issue> issues) throws IOException {
        Path metadata = context.resolve("src/main/resources/fabric.mod.json");
        String content = Files.readString(metadata);
        require(content, "\"id\"", "fabric.id.missing", "fabric.mod.json is missing an id.", metadata, issues);
        require(content, "\"version\"", "fabric.version.missing", "fabric.mod.json is missing a version.", metadata, issues);
        require(content, "\"schemaVersion\"", "fabric.schema.missing", "fabric.mod.json is missing schemaVersion.", metadata, issues);
        require(content, "\"depends\"", "fabric.depends.missing", "fabric.mod.json should declare dependency constraints.", metadata, issues);
    }

    private void validateNeoForge(ProjectContext context, List<Issue> issues) throws IOException {
        Path metadata = context.exists("src/main/resources/META-INF/neoforge.mods.toml")
            ? context.resolve("src/main/resources/META-INF/neoforge.mods.toml")
            : context.resolve("src/main/resources/META-INF/mods.toml");
        String content = Files.readString(metadata);
        require(content, "modId", "neoforge.modid.missing", "NeoForge metadata is missing modId.", metadata, issues);
        require(content, "version", "neoforge.version.missing", "NeoForge metadata is missing version.", metadata, issues);
        require(content, "displayName", "neoforge.display.missing", "NeoForge metadata is missing displayName.", metadata, issues);
        require(content, "[[dependencies", "neoforge.dependencies.missing", "NeoForge metadata should declare dependency constraints.", metadata, issues);
    }

    private void validatePaper(ProjectContext context, List<Issue> issues) throws IOException {
        Path metadata = context.exists("src/main/resources/paper-plugin.yml")
            ? context.resolve("src/main/resources/paper-plugin.yml")
            : context.resolve("src/main/resources/plugin.yml");
        String content = Files.readString(metadata);
        require(content, "name:", "paper.name.missing", "Paper plugin metadata is missing name.", metadata, issues);
        require(content, "version:", "paper.version.missing", "Paper plugin metadata is missing version.", metadata, issues);
        require(content, "main:", "paper.main.missing", "Paper plugin metadata is missing main class.", metadata, issues);
        if (!content.contains("api-version:")) {
            issues.add(Issue.warning("paper.api-version.missing", "Paper plugin metadata should declare api-version.", metadata));
        }
    }

    private void require(String content, String token, String code, String message, Path path, List<Issue> issues) {
        if (!content.contains(token)) {
            issues.add(Issue.error(code, message, path));
        }
    }

    public record ValidationReport(ProjectType projectType, List<Issue> issues) {
    }
}
