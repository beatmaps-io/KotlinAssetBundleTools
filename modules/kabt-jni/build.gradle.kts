import dev.nokee.platform.jni.JarBinary
import dev.nokee.platform.jni.JniJarBinary
import dev.nokee.platform.jni.JvmJarBinary
import dev.nokee.runtime.nativebase.TargetMachine
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("jvm")
    id("dev.nokee.jni-library")
    id("dev.nokee.c-language")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        main {
            dependencies {
                implementation(project(":kabt-base"))
            }
        }
    }
}

library {
    targetMachines.set(
        listOf(
            machines.linux.x86_64,
            machines.windows.x86_64
        )
    )

    variants.configureEach {
        val osName = System.getProperty("os.name").lowercase().replace(" ", "")
        val osFamily = targetMachine.operatingSystemFamily

        dependencies {
            if (osFamily.isWindows && osName.contains("windows")) {
                nativeLinkOnly(files("ufs/UnityFileSystemApi.lib"))
                nativeRuntimeOnly(files("ufs/UnityFileSystemApi.dll"))
            } else if (osFamily.isLinux && osName.contains("linux")) {
                nativeImplementation(files("ufs/UnityFileSystemApi.so"))
            } else {
                nativeImplementation(files("ufs/UnityFileSystemApi.dylib"))
            }

        }
    }
}

fun asZipTree(): (JarBinary) -> Provider<FileTree> {
    return { jarBinary: JarBinary ->
        jarBinary.jarTask.map { zipTree(it.archiveFile) }
    }
}

fun isHostTargeted(targetMachine: TargetMachine): Boolean {
    val osName = System.getProperty("os.name").lowercase().replace(" ", "")
    val osFamily = targetMachine.operatingSystemFamily
    if (osFamily.isWindows && osName.contains("windows")) {
        return true
    } else if (osFamily.isLinux && osName.contains("linux")) {
        return true
    } else if (osFamily.isMacOs && osName.contains("macos")) {
        return true
    }
    return false
}

tasks.register<Jar>("uberJar") {
    from(library.variants.flatMap { variant ->
        val result = ArrayList<Provider<List<Provider<FileTree>>>>()
        if (isHostTargeted(variant.targetMachine)) {
            result.add(variant.binaries.withType(JniJarBinary::class.java).map(asZipTree()))
        }
        result
    }) {
        exclude("META-INF/**")
    }
    from(library.binaries.withType(JvmJarBinary::class.java).map(asZipTree()))
    val os = DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
    archiveClassifier.set("uber-$os")
}

publishing {
    publications {
        create<MavenPublication>("uberJar") {
            groupId = "io.beatmaps"
            artifactId = project.name
            version = System.getenv("BUILD_NUMBER")?.let { "1.0.$it" } ?: "1.0-SNAPSHOT"
            artifact(tasks["uberJar"])
        }
    }

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
