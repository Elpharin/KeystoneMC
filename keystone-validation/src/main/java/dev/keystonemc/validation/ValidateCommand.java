package dev.keystonemc.validation;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.util.List;

public final class ValidateCommand implements KeystoneCommand {
    private final ValidationService validationService = new ValidationService();

    @Override
    public String name() {
        return "validate";
    }

    @Override
    public String description() {
        return "Validate Gradle and Minecraft loader metadata.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        ValidationService.ValidationReport report = validationService.validate(context);
        return CommandResult.builder(name())
            .detail("projectType=" + report.projectType())
            .issues(report.issues())
            .build();
    }
}
