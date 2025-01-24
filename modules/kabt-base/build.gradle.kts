plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

group = "io.beatmaps"
version = System.getenv("BUILD_NUMBER")?.let { "1.0.$it" } ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    mingwX64()
    linuxX64()

    sourceSets {
        commonMain.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        jvmMain.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        nativeMain.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        linuxX64Main.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        mingwX64Main.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "reposilite"
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
            url = uri("https://artifactory.kirkstall.top-cat.me/")
        }
    }
}
