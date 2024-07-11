plugins {
    kotlin("multiplatform") version "2.0.0"
    id("maven-publish")
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "org.isk"
    version = "1.0.0-xi.0"
}

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("maven-publish")
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Independent-Society-of-Knowledge/MoleKule")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["kotlin"])
            }
        }
    }


    kotlin {
        withSourcesJar(true)
        jvmToolchain(21)
        jvm()
        js {
            browser()
        }
        sourceSets {
            commonTest.dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
            }
        }
    }
}
kotlin {
    jvm("root")
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    val node = rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>()
    node.version = "22.0.0"
    node.download = true
}
