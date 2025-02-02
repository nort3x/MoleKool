pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "molekool"

include(
    "molekool-core",
    "molekool-bind",
    "molekool-visual",
    "examples",
)
