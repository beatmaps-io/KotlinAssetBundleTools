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

    macosX64 {
        binaries {
            executable {
                linkerOpts += "${rootProject.projectDir}/modules/kabt-jni/ufs/libUnityFileSystemApi.dylib"
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }

    sourceSets {
        nativeMain {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":kabt-base"))
                implementation(rootProject)
            }
        }
    }
}


tasks {
    setOf("mingwX64", "macosX64").forEach { target ->
        setOf("Debug", "Release").forEach { buildType ->
            val copyTask = register("copyUFS$target$buildType", Copy::class) {
                group = "package"
                description = "Copies the resources into exe directory"

                from("${rootProject.projectDir}/modules/kabt-jni/ufs") {
                    include("**/*")
                }

                into(layout.buildDirectory.file("bin/$target/debugExecutable"))
            }
            getByName("run${buildType}Executable${target.replaceFirstChar { it.uppercaseChar() }}") {
                dependsOn(copyTask)
            }
        }
    }
}