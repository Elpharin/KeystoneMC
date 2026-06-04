plugins {
    application
}

dependencies {
    implementation(project(":keystone-core"))
    implementation(project(":keystone-validation"))
    implementation(project(":keystone-benchmark"))
    implementation(project(":keystone-test"))
    implementation(project(":keystone-profile"))
    implementation(project(":keystone-compare"))
    implementation(project(":keystone-matrix"))
    implementation(project(":keystone-report"))
    implementation(project(":keystone-publish"))
    implementation(project(":keystone-plugin-api"))
}

application {
    mainClass.set("dev.keystonemc.cli.KeystoneCli")
}
