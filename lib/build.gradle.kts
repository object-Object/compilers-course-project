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

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.engine)
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
        val baseOutputDirectory = outputDirectory
        outputDirectory = file("$baseOutputDirectory/ca/objectobject/hexlr/parser")

        arguments.addAll(listOf(
            "-package", "ca.objectobject.hexlr.parser",
            "-no-listener",
            "-visitor",
        ))

        // ensure all files are up to date
        doFirst {
            delete(baseOutputDirectory)
        }
    }

    named<Test>("test") {
        useJUnitPlatform()
    }
}
