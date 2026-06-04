package dev.keystonemc.publish;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;
import dev.keystonemc.validation.ValidationService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PublishCommand implements KeystoneCommand {
    private final ValidationService validationService = new ValidationService();

    @Override
    public String name() {
        return "publish";
    }

    @Override
    public String description() {
        return "Prepare release metadata and fail fast on invalid project state.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        CommandResult.Builder builder = CommandResult.builder(name()).issues(validationService.validate(context).issues());
        Path libs = context.resolve("build/libs");
        if (!Files.isDirectory(libs)) {
            builder.issue(Issue.error("publish.artifacts.missing", "No build artifacts found. Run the host build before publishing.", libs));
        }
        if (!context.exists("README.md")) {
            builder.issue(Issue.warning("publish.readme.missing", "README.md is recommended before publishing.", context.root()));
        }
        if (!context.exists("LICENSE")) {
            builder.issue(Issue.warning("publish.license.missing", "LICENSE is recommended before publishing.", context.root()));
        }
        return builder.detail("dryRun=" + !args.contains("--execute")).build();
    }
}
