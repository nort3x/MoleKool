import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos")

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(File("$rootDir/dist/js"))
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":molekool-core"))
            api(libs.kool.core)
            api(libs.kotlin.coroutine)
        }
        val jvmMain by getting {
            dependencies {
                for (platform in targetPlatforms) {
                    runtimeOnly("org.lwjgl:lwjgl:${libs.versions.lwjgl}:$platform")
                    listOf("glfw", "opengl", "jemalloc", "nfd", "stb", "vma", "shaderc").forEach { lib ->
                        runtimeOnly("org.lwjgl:lwjgl-$lib:${libs.versions.lwjgl.get()}:$platform")
                    }
                }
            }
        }
    }
}
