pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "keystone"

include(
    "keystone-cli",
    "keystone-core",
    "keystone-validation",
    "keystone-benchmark",
    "keystone-test",
    "keystone-profile",
    "keystone-compare",
    "keystone-matrix",
    "keystone-report",
    "keystone-publish",
    "keystone-plugin-api",
)
