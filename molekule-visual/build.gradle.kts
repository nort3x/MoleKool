import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

val koolVersion = "0.14.0"
val lwjglVersion = "3.3.3"
val physxJniVersion = "2.3.1"
val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos")

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(File("${rootDir}/dist/js"))
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":molekule-core"))

            api("de.fabmax.kool:kool-core:$koolVersion")
            api("de.fabmax.kool:kool-physics:$koolVersion")

            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }
        val jvmMain by getting {
            dependencies {
                // add additional jvm-specific dependencies here...

                // add required runtime libraries for lwjgl and physx-jni
                for (platform in targetPlatforms) {
                    // lwjgl runtime libs
                    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$platform")
                    listOf("glfw", "opengl", "jemalloc", "nfd", "stb", "vma", "shaderc").forEach { lib ->
                        runtimeOnly("org.lwjgl:lwjgl-$lib:$lwjglVersion:$platform")
                    }

                    // physx-jni runtime libs
                    runtimeOnly("de.fabmax:physx-jni:$physxJniVersion:$platform")
                }
            }
        }
    }
}
