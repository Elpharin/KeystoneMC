package dev.keystonemc.report;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;
import dev.keystonemc.core.SimpleJson;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReportCommand implements KeystoneCommand {
    @Override
    public String name() {
        return "report";
    }

    @Override
    public String description() {
        return "Create a Keystone report file for downstream CI publishing.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        Path output = context.resolve(option(args, "--output", "build/reports/keystone/report.json"));
        Files.createDirectories(output.getParent());
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("project", context.root().getFileName().toString());
        payload.put("root", context.root().toString());
        payload.put("schema", "dev.keystonemc.report.v1");
        Files.writeString(output, SimpleJson.stringify(payload) + System.lineSeparator());
        return CommandResult.builder(name()).detail("wrote=" + output).build();
    }

    private String option(List<String> args, String name, String fallback) {
        int index = args.indexOf(name);
        if (index >= 0 && index + 1 < args.size()) {
            return args.get(index + 1);
        }
        return fallback;
    }
}
