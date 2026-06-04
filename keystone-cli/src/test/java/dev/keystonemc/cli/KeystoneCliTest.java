package dev.keystonemc.cli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeystoneCliTest {
    @Test
    void helpSucceeds() {
        KeystoneCli cli = new KeystoneCli(List.of());

        assertEquals(0, cli.execute(List.of("--help")));
    }

    @Test
    void unknownCommandFailsAsUsageError() {
        KeystoneCli cli = new KeystoneCli(List.of());

        assertEquals(2, cli.execute(List.of("missing")));
    }
}
