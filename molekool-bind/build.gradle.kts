kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            api(project(":molekool-core"))
        }
        jvmMain.dependencies {
            dependencies {
                api("org.apache.commons:commons-lang3:3.14.0")
                api("org.apache.velocity:velocity-engine-core:2.3")
                implementation("org.slf4j:slf4j-simple:2.0.9")
            }
        }
        jvmTest.dependencies {
            implementation("io.kotest:kotest-assertions-core-jvm:5.6.2")
            implementation("org.jetbrains.kotlin:kotlin-test")
        }
    }
}
