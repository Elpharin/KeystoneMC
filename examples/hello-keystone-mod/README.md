# Hello Keystone Mod

This is a tiny Fabric-style mod used to demonstrate Keystone against a real jar-producing project.

It intentionally depends only on Fabric Loader's public API. Full Fabric projects should normally use Fabric Loom for Minecraft dependencies, remapping, run configs, and production jars.

## Build

From the repository root:

```bash
./gradlew -p examples/hello-keystone-mod build
./gradlew :keystone-cli:installDist
./keystone-cli/build/install/keystone-cli/bin/keystone-cli --project examples/hello-keystone-mod validate
./keystone-cli/build/install/keystone-cli/bin/keystone-cli --project examples/hello-keystone-mod profile
./keystone-cli/build/install/keystone-cli/bin/keystone-cli --project examples/hello-keystone-mod report
```

On Windows, use `gradlew.bat` and `keystone-cli.bat`.
