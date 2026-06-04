package dev.keystonemc.compare;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.Issue;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CompareCommand implements KeystoneCommand {
    @Override
    public String name() {
        return "compare";
    }

    @Override
    public String description() {
        return "Compare two artifact files by size and timestamp.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) throws Exception {
        CommandResult.Builder builder = CommandResult.builder(name());
        if (args.size() < 2) {
            return builder.issue(Issue.error("compare.arguments", "Expected two file paths: keystone compare <baseline> <candidate>.", context.root())).build();
        }
        Path baseline = context.root().resolve(args.get(0)).normalize();
        Path candidate = context.root().resolve(args.get(1)).normalize();
        if (!Files.isRegularFile(baseline)) {
            builder.issue(Issue.error("compare.baseline.missing", "Baseline file does not exist.", baseline));
        }
        if (!Files.isRegularFile(candidate)) {
            builder.issue(Issue.error("compare.candidate.missing", "Candidate file does not exist.", candidate));
        }
        if (builder.build().hasErrors()) {
            return builder.build();
        }
        long baselineBytes = Files.size(baseline);
        long candidateBytes = Files.size(candidate);
        long delta = candidateBytes - baselineBytes;
        return builder.detail("baseline=" + baselineBytes + " bytes")
            .detail("candidate=" + candidateBytes + " bytes")
            .detail("delta=" + delta + " bytes")
            .build();
    }
}
