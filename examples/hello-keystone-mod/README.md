# Hello Keystone Mod

This is a tiny Fabric-style mod used to demonstrate Keystone against a real jar-producing project.

It intentionally depends only on Fabric Loader's public API. Full Fabric projects should normally use Fabric Loom for Minecraft dependencies, remapping, run configs, and production jars.

## Build

From the repository root:

```bash
./gradlew -p examples/hello-keystone-mod build
./gradlew :keystone-cli:installDist
./keystone-cli/build/install/keystone/bin/keystone --project examples/hello-keystone-mod validate
./keystone-cli/build/install/keystone/bin/keystone --project examples/hello-keystone-mod profile
./keystone-cli/build/install/keystone/bin/keystone --project examples/hello-keystone-mod report
```

On Windows:

```powershell
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.7.6-hotspot"
$env:GRADLE_USER_HOME = Join-Path $env:TEMP "keystone-gradle-user-home"
.\gradlew.bat -p examples\hello-keystone-mod build --no-daemon
.\gradlew.bat :keystone-cli:installDist --no-daemon
.\keystone-cli\build\install\keystone\bin\keystone.bat --project examples\hello-keystone-mod validate
.\keystone-cli\build\install\keystone\bin\keystone.bat --project examples\hello-keystone-mod profile
.\keystone-cli\build\install\keystone\bin\keystone.bat --project examples\hello-keystone-mod publish
```
