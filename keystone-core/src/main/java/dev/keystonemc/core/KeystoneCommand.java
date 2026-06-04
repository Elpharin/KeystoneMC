package dev.keystonemc.core;

import java.util.List;

public interface KeystoneCommand {
    String name();

    String description();

    CommandResult run(ProjectContext context, List<String> args) throws Exception;
}
