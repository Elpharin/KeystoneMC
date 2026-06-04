# KeystoneMC

Keystone is a production-minded quality assurance toolkit for Minecraft developers.

It is not a build system, loader, remapper, publishing plugin, or CI platform. Keystone is the layer that sits beside the tools Minecraft developers already use and asks the practical release questions that are easy to forget:

- Is the project metadata valid?
- Is Java 21 clearly configured?
- Do the expected artifacts exist?
- Did the jar size change in a suspicious way?
- Can this project be tested, benchmarked, profiled, and reported on consistently?
- Is the release state understandable before it reaches players?

Keystone is designed for Fabric, NeoForge, and Paper projects that already rely on Gradle, Fabric Loom, NeoForge Gradle tooling, Paperweight, Paper, and GitHub Actions. It integrates with those workflows instead of replacing them.

<img width="550" height="83" alt="KeystoneMC" src="https://github.com/user-attachments/assets/d2112a00-ecb6-40e7-8f84-402502453bbe" />

## Why Keystone Exists

Minecraft projects often grow a private collection of scripts, CI snippets, metadata checks, benchmark commands, release checklists, and one-off validation rules. Those pieces are useful, but they are usually scattered across repositories and hard to compare across loaders.

Keystone gives those workflows a shared shape.

Think of it as a developer QA layer for Minecraft projects: focused, explicit, scriptable, and friendly to CI. Every command is intended to provide standalone value, while still being composable enough to fit into a larger release pipeline.

## What Keystone Does

Keystone currently provides a modular CLI with commands for:

- validating Gradle and Minecraft loader metadata
- running host project tests through Gradle
- benchmarking repeated Gradle task execution
- profiling generated jar artifacts
- comparing artifact sizes
- generating explicit loader/version matrix cells
- writing machine-readable report files
- checking release readiness before publishing

The goal is not to hide the underlying tooling. The goal is to make quality signals easier to run, easier to automate, and easier to trust.

## Commands

```bash
keystone validate
```

Validates project structure and loader metadata. Keystone detects Fabric, NeoForge, Paper, or unknown projects and checks for common release-blocking metadata mistakes.

```bash
keystone test
```

Runs the host project's Gradle test task. You keep your existing Gradle test setup; Keystone gives it a consistent command and result model.

```bash
keystone benchmark --task build --iterations 3
```

Runs a Gradle task repeatedly and reports per-run timing plus an average. This is useful for tracking build and data-generation performance over time.

```bash
keystone profile
```

Inspects generated jars under `build/libs`, reports artifact sizes, and fails on missing or empty artifacts.

```bash
keystone compare <baseline.jar> <candidate.jar>
```

Compares two artifacts and reports size deltas. This is intentionally simple today, but useful as a release sanity check.

```bash
keystone matrix --minecraft 1.21.1,1.21.4 --loaders fabric,neoforge,paper
```

Prints explicit compatibility cells for Minecraft versions and loaders.

```bash
keystone report
```

Writes a CI-friendly JSON report to `build/reports/keystone/report.json`.

```bash
keystone publish
```

Runs release readiness checks, including validation and artifact presence. Publishing remains the responsibility of the host project's existing Gradle and platform tooling.

## Example CI Shape

```yaml
name: Keystone QA

on:
  pull_request:
  push:
    branches: [main]

jobs:
  qa:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21
      - run: ./gradlew test
      - run: keystone validate --json
      - run: keystone profile
      - run: keystone publish
```

## Architecture

Keystone is built as a Java 21 Gradle Kotlin multi-project:

- `keystone-cli`: command-line entry point and output formatting
- `keystone-core`: shared command contracts, issue model, project context, Gradle execution, and JSON utilities
- `keystone-validation`: Gradle and loader metadata validation
- `keystone-benchmark`: repeated Gradle task timing
- `keystone-test`: host project test execution
- `keystone-profile`: artifact profiling
- `keystone-compare`: artifact comparison
- `keystone-matrix`: compatibility matrix generation
- `keystone-report`: report generation
- `keystone-publish`: release readiness checks
- `keystone-plugin-api`: extension point for third-party Keystone commands

## Design Principles

- Integrate with Minecraft tooling; do not replace it.
- Keep commands explicit and discoverable.
- Keep modules isolated and composable.
- Prefer testable behavior over hidden magic.
- Emit useful results for both humans and CI systems.
- Treat release quality as an engineering workflow, not a last-minute checklist.

## Build

```bash
./gradlew test
./gradlew :keystone-cli:installDist
```

On Windows:

```powershell
.\gradlew.bat test
.\gradlew.bat :keystone-cli:installDist
```

## Current Status

Keystone is early, but the foundation is real: the CLI, module boundaries, validation flow, Gradle runner, plugin API, tests, wrapper, docs, and initial commands are in place.

The next phase is deeper Minecraft-specific intelligence: richer metadata parsing, compatibility policy files, benchmark history, report aggregation, GitHub Actions examples, and loader-aware release checks.
