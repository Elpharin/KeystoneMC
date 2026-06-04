# KeystoneMC

Keystone is a Java 21 quality assurance layer for Minecraft developers. It sits on top of existing Gradle, Fabric Loom, NeoForge, Paperweight, and Paper workflows instead of replacing them.

<img width="550" height="83" alt="image" src="https://github.com/user-attachments/assets/d2112a00-ecb6-40e7-8f84-402502453bbe" />

## Commands

- `keystone validate`: validate Gradle and Minecraft loader metadata.
- `keystone test`: run the host project's Gradle tests.
- `keystone benchmark --task build --iterations 3`: measure repeated Gradle task execution.
- `keystone profile`: inspect generated jar artifacts.
- `keystone compare <baseline> <candidate>`: compare artifact sizes.
- `keystone matrix --minecraft 1.21.1,1.21.4 --loaders fabric,neoforge,paper`: print compatibility cells.
- `keystone report`: write a CI-friendly report file.
- `keystone publish`: perform release readiness checks.

## Build

```bash
gradle test
gradle :keystone-cli:installDist
```
