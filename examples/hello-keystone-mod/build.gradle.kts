plugins {
    java
}

group = "dev.keystonemc.examples"
version = "1.0.0"

base {
    archivesName.set("hello-keystone-mod")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

dependencies {
    compileOnly("net.fabricmc:fabric-loader:0.16.14")
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}
