pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // ✅ updated
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // ✅ JitPack added
    }
}

rootProject.name = "resumegenerator"
include(":app")
