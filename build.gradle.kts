import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.1.0"
    id("maven-publish")
}

group = "io.beatmaps"
version = System.getenv("BUILD_NUMBER")?.let { "1.0.$it" } ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    jvmToolchain(21)

    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
        withJava()
    }

    mingwX64 {
        val main by compilations.getting

        main.cinterops {
            val unityFileSystemApi by creating {
                defFile("src/nativeMain/cinterop/UnityFileSystemApi.def")
                packageName("io.beatmaps.kabt.unity")
            }
        }
    }

    linuxX64 {
        val main by compilations.getting

        main.cinterops {
            val unityFileSystemApi by creating {
                defFile("src/nativeMain/cinterop/UnityFileSystemApi.def")
                packageName("io.beatmaps.kabt.unity")
            }
        }
    }

    macosX64 {
        val main by compilations.getting

        main.cinterops {
            val unityFileSystemApi by creating {
                defFile("src/nativeMain/cinterop/UnityFileSystemApi.def")
                packageName("io.beatmaps.kabt.unity")
            }
        }
    }

    sourceSets.configureEach {
        languageSettings {
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
    }

    sourceSets {
        jvmMain {
            dependencies {
                implementation(project(":kabt-jni"))
            }
        }
        commonMain {
            dependencies {
                implementation(project(":kabt-base"))
            }
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
