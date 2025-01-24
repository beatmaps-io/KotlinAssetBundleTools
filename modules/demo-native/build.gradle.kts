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
            executable()
        }
    }

    sourceSets {
        nativeMain {
            dependencies {
                implementation(project(":kabt-base"))
                implementation(rootProject)
            }
        }
    }
}
