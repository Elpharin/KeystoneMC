package dev.keystonemc.pluginapi;

import dev.keystonemc.core.KeystoneCommand;

import java.util.List;

public interface KeystonePlugin {
    String id();

    String displayName();

    List<KeystoneCommand> commands();
}
