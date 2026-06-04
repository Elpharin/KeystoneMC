package dev.keystonemc.validation;

import dev.keystonemc.core.ProjectContext;

public final class ProjectDetector {
    public ProjectType detect(ProjectContext context) {
        if (context.exists("src/main/resources/fabric.mod.json")) {
            return ProjectType.FABRIC;
        }
        if (context.exists("src/main/resources/META-INF/neoforge.mods.toml")
            || context.exists("src/main/resources/META-INF/mods.toml")) {
            return ProjectType.NEOFORGE;
        }
        if (context.exists("src/main/resources/paper-plugin.yml")
            || context.exists("src/main/resources/plugin.yml")) {
            return ProjectType.PAPER;
        }
        return ProjectType.UNKNOWN;
    }
}
