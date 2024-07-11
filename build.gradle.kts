import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn

plugins {
    kotlin("multiplatform") version "2.0.0"
    id("com.github.node-gradle.node") version "7.0.2"
    id("maven-publish")
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "org.isk"
    version = "1.0.0"
}

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("maven-publish")
        plugin("com.github.node-gradle.node")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
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

