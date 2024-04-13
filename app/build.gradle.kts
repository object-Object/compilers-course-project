plugins {
    alias(libs.plugins.kotlin)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
    implementation(libs.clikt)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

application {
    mainClass = "MainKt"
}

tasks {
    named<JavaExec>("run") {
        standardInput = System.`in`
        workingDir = rootProject.projectDir
    }
}
