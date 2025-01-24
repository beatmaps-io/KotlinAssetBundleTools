plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        main {
            dependencies {
                implementation(project(":kabt-base"))
                implementation(rootProject)
            }
        }
    }
}

application {
    mainClass = "io.beatmaps.kabt.DemoKt"
    // Won't be required if you depend on the uber jar
    applicationDefaultJvmArgs += listOf(
        "-Djava.library.path=${rootProject.projectDir}/modules/kabt-jni/build/libs/main/windows;${rootProject.projectDir}/modules/kabt-jni/ufs"
    )
}
