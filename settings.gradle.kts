import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.mavenCentral
import org.gradle.kotlin.dsl.repositories

include(":app", ":opencv_sdk")


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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url=uri("https://jitpack.io") }
    }
}

rootProject.name = "OCRReader02"
project(":opencv_sdk").projectDir = File(rootDir, "app/src/main/lib/OpenCV-android-sdk/opencv_sdk/")