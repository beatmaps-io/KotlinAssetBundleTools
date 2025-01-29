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

    linuxX64 {
        binaries {
            executable {
                linkerOpts += "${project(":kabt-jni").projectDir}/ufs/UnityFileSystemApi.so"
            }
        }
    }

    macosX64 {
        binaries {
            executable {
                linkerOpts += "${project(":kabt-jni").projectDir}/ufs/UnityFileSystemApi.dylib"
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
    setOf("mingwX64", "macosX64", "linuxX64").forEach { target ->
        setOf("Debug", "Release").forEach { buildType ->
            val targetPath = layout.buildDirectory.file("bin/$target/${buildType.lowercase()}Executable")
            val sourcePath = "${project(":kabt-jni").projectDir}/ufs"
            val copyTask = register("copyUFS$target$buildType", Copy::class) {
                group = "package"
                description = "Copies the resources into exe directory"

                from(sourcePath) {
                    include("**/*.dll")
                }

                into(targetPath)
            }
            getByName<Exec>("run${buildType}Executable${target.replaceFirstChar { it.uppercaseChar() }}") {
                environment("LD_LIBRARY_PATH", sourcePath)
                environment("DYLD_LIBRARY_PATH", sourcePath)
                dependsOn(copyTask)
            }
        }
    }
}
