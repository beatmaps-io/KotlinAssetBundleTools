rootProject.name = "kabt"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.nokee.dev/release") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("dev.nokee.")) {
                useModule("${requested.id.id}:${requested.id.id}.gradle.plugin:0.4.0")
            }
        }
    }
}

file("modules")
    .listFiles()!!
    .filter(File::isDirectory)
    .forEach { directory ->
        val name = directory.name

        include(name)

        project(":$name").apply {
            this.projectDir = directory
        }
    }
