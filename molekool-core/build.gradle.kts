import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvm()
    js(IR) {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(File("$rootDir/dist/js"))
            }
        }
    }

//    androidNativeX64()
//    androidNativeArm64()
//    androidNativeX86()
//    androidNativeArm32()
//
//
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()
//
//    linuxArm64()
//    linuxX64()
//
//    macosArm64()
//    macosX64()
//
//    mingwX64()
}
