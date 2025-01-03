pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "molekule"

include(
    "molekule-core",
    "molekule-bind",
    "molekule-visual",
    "examples"
)
