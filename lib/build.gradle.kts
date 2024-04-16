plugins {
    alias(libs.plugins.kotlin)
    `java-library`
    antlr
}

repositories {
    mavenCentral()
}

dependencies {
    antlr(libs.antlr)
    implementation(kotlin("reflect"))

    api(libs.lsp4j)
    api(libs.lsp4j.debug)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.engine)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.launcher)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

tasks {
    compileKotlin {
        dependsOn(generateGrammarSource)
    }

    compileTestKotlin {
        dependsOn(generateTestGrammarSource)
    }

    generateGrammarSource {
        outputDirectory = file("$outputDirectory/ca/objectobject/hexlr/parser")
        arguments.addAll(listOf(
            "-package", "ca.objectobject.hexlr.parser",
            "-no-listener",
            "-visitor",
        ))
    }

    named<Test>("test") {
        useJUnitPlatform()
    }
}
