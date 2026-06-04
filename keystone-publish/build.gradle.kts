plugins {
    `java-library`
}

dependencies {
    api(project(":keystone-core"))
    implementation(project(":keystone-validation"))
}
