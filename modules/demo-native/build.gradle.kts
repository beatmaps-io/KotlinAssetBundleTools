plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    mingwX64 {
        binaries {
            executable()
        }
    }

    sourceSets {
        mingwX64Main {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        nativeMain {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":kabt-base"))
                implementation(rootProject)
            }
        }
    }
}


tasks {
    val copyDebug = register("copyUFSDebug", Copy::class) {
        group = "package"
        description = "Copies the resources into exe directory"

        from("${rootProject.projectDir}/modules/kabt-jni/ufs") {
            include("**/*")
        }

        into(layout.buildDirectory.file("bin/mingwX64/debugExecutable"))
    }
    val runDebugExecutableMingwX64 by getting {
        dependsOn(copyDebug)
    }

    val copyRelease = register("copyUFSRelease", Copy::class) {
        group = "package"
        description = "Copies the resources into exe directory"

        from("${rootProject.projectDir}/modules/kabt-jni/ufs") {
            include("**/*")
        }

        into(layout.buildDirectory.file("bin/mingwX64/releaseExecutable"))
    }
    val runReleaseExecutableMingwX64 by getting {
        dependsOn(copyRelease)
    }
}