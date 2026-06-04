# Keystone Architecture

Keystone is a Java 21 quality assurance layer for Minecraft projects. It integrates with existing Gradle, Fabric Loom, NeoForge, Paperweight, and Paper workflows instead of replacing them.

## Modules

- `keystone-cli`: command-line entry point and output formatting.
- `keystone-core`: shared command contracts, issue model, project context, Gradle execution, and JSON output utilities.
- `keystone-validation`: Gradle and loader metadata validation for Fabric, NeoForge, and Paper projects.
- `keystone-benchmark`: repeated Gradle task timing.
- `keystone-test`: host project test execution.
- `keystone-profile`: artifact profiling for generated jars.
- `keystone-compare`: baseline and candidate artifact comparison.
- `keystone-matrix`: explicit compatibility matrix generation.
- `keystone-report`: machine-readable report generation.
- `keystone-publish`: release readiness checks.
- `keystone-plugin-api`: extension point for third-party Keystone commands.

## Non-goals

Keystone does not own compilation, remapping, publishing, or runtime loading. Those concerns remain in the host project's existing Minecraft and Gradle tooling.
