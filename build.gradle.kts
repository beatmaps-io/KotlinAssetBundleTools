import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.1.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

/*dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}*/

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    jvmToolchain(21)

    /*val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }*/

    // configureLinux()
    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
        withJava()
    }

    mingwX64("native") {
        val main by compilations.getting

        main.cinterops {
            val unityFileSystemApi by creating {
                defFile("src/nativeMain/cinterop/UnityFileSystemApi.def")
                packageName("io.beatmaps.kabt.unity")
            }
        }

        binaries {
            executable()
        }
    }

    sourceSets {
        nativeMain.languageSettings {
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        jvmMain.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
        commonMain.languageSettings {
            optIn("kotlin.ExperimentalUnsignedTypes")
        }
    }
}

application {
    mainClass = "io.beatmaps.kabt.MainKt"
    applicationDefaultJvmArgs += listOf(
        "-Djava.library.path=${projectDir}/build/native/"
    )
}

val nativeFile = "io_beatmaps_kabt_UFSJNI"
val gccPath = if (Os.isFamily(Os.FAMILY_UNIX)) {
    "/usr/bin"
} else  {
    "F:\\Program Files\\mingw-w64\\x86_64-8.1.0-posix-seh-rt_v6-rev0\\mingw64\\bin"
}

tasks.getByName<Jar>("jvmJar") {
    dependsOn("nativeLink")
}

tasks.register<Exec>("nativeBuild") {
    val cArgs = mutableListOf("-c", "-fPIC")
    val jvmHome = System.getenv("JAVA_HOME")
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        cArgs.addAll(listOf("-I", "${jvmHome}/include"))
        cArgs.addAll(listOf("-I", "${jvmHome}/include/linux"))
    } else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        cArgs.addAll(listOf("-I", "${jvmHome}/include"))
        cArgs.addAll(listOf("-I", "${jvmHome}/include/win32"))
    }
    environment["PATH"] = gccPath
    commandLine = listOf("${gccPath}\\gcc") + cArgs + listOf("native/$nativeFile.c", "-o", "build/native/$nativeFile.o")
    doFirst {
        File("build/native").mkdirs()
    }
}

tasks.register<Exec>("nativeLink") {
    dependsOn("nativeBuild")

    val cArgs = mutableListOf<String>()
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        cArgs.addAll(listOf("-shared", "-fPIC", "-o", "build/native/kabt-wrapper.so", "build/native/$nativeFile.o", "native/UnityFileSystemApi.dll", "-lc"))
    } else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        cArgs.addAll(listOf("-shared", "-o", "build/native/kabt-wrapper.dll", "build/native/$nativeFile.o", "native/UnityFileSystemApi.dll"))
    }
    environment["PATH"] = gccPath
    commandLine = listOf("${gccPath}\\gcc") + cArgs
}
