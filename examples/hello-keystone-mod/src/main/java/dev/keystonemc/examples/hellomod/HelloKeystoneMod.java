package dev.keystonemc.examples.hellomod;

import net.fabricmc.api.ModInitializer;

public final class HelloKeystoneMod implements ModInitializer {
    public static final String MOD_ID = "hello_keystone";

    @Override
    public void onInitialize() {
        System.out.println("Hello from Keystone's example Fabric mod.");
    }
}
