package dev.keystonemc.validation;

import dev.keystonemc.core.ProjectContext;
import dev.keystonemc.core.Severity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void validatesFabricMetadata() throws Exception {
        Files.writeString(tempDir.resolve("settings.gradle.kts"), "rootProject.name = \"sample\"");
        Files.writeString(tempDir.resolve("build.gradle.kts"), "java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }");
        Path metadata = tempDir.resolve("src/main/resources/fabric.mod.json");
        Files.createDirectories(metadata.getParent());
        Files.writeString(metadata, """
            {
              "schemaVersion": 1,
              "id": "sample",
              "version": "1.0.0",
              "depends": { "minecraft": ">=1.21" }
            }
            """);

        ValidationService.ValidationReport report = new ValidationService().validate(new ProjectContext(tempDir));

        assertEquals(ProjectType.FABRIC, report.projectType());
        assertFalse(report.issues().stream().anyMatch(issue -> issue.severity() == Severity.ERROR));
    }

    @Test
    void reportsMissingGradleAndUnknownLoader() throws Exception {
        ValidationService.ValidationReport report = new ValidationService().validate(new ProjectContext(tempDir));

        assertEquals(ProjectType.UNKNOWN, report.projectType());
        assertTrue(report.issues().stream().anyMatch(issue -> issue.code().equals("gradle.missing")));
        assertTrue(report.issues().stream().anyMatch(issue -> issue.code().equals("minecraft.loader.unknown")));
    }
}
