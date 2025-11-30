<<<<<<< HEAD
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
=======
// settings.gradle.kts
pluginManagement {
    repositories {
        google()
>>>>>>> 63228a2d8bbe1d61bbf25d77e157d602bc5a0005
        mavenCentral()
        gradlePluginPortal()
    }
}
<<<<<<< HEAD
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BookCare_MarketPlace"
include(":app")
 
=======

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT) // ✅ Allow project repositories
}

rootProject.name = "BookCare"
include(":app")
>>>>>>> 63228a2d8bbe1d61bbf25d77e157d602bc5a0005
