package dev.keystonemc.matrix;

import dev.keystonemc.core.CommandResult;
import dev.keystonemc.core.KeystoneCommand;
import dev.keystonemc.core.ProjectContext;

import java.util.List;

public final class MatrixCommand implements KeystoneCommand {
    @Override
    public String name() {
        return "matrix";
    }

    @Override
    public String description() {
        return "Generate an explicit Minecraft loader/version compatibility matrix.";
    }

    @Override
    public CommandResult run(ProjectContext context, List<String> args) {
        String minecraft = option(args, "--minecraft", "1.21.1,1.21.4");
        String loaders = option(args, "--loaders", "fabric,neoforge,paper");
        CommandResult.Builder builder = CommandResult.builder(name());
        for (String mc : minecraft.split(",")) {
            for (String loader : loaders.split(",")) {
                builder.detail(loader.trim() + ":" + mc.trim());
            }
        }
        return builder.build();
    }

    private String option(List<String> args, String name, String fallback) {
        int index = args.indexOf(name);
        if (index >= 0 && index + 1 < args.size()) {
            return args.get(index + 1);
        }
        return fallback;
    }
}
