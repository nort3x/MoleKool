import com.vanniktech.maven.publish.SonatypeHost
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.atomicfu)
    signing
}

allprojects {

    repositories {
        mavenCentral()
    }

    group = "io.github.nort3x"
    version = "1.0.0.alpha-2"
}
val kotestVersion = libs.versions.kotest.get()

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("com.vanniktech.maven.publish")
        plugin("org.jetbrains.kotlinx.atomicfu")
        plugin("signing")
    }

    ktlint {
        ignoreFailures = false
        verbose = true
        outputToConsole = true
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
        }
    }

    kotlin {
        withSourcesJar(true)
        jvmToolchain(21)
        jvm()
        sourceSets {
            commonTest.dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
            }
        }
        targets.all {
            compilations.all {
                compileTaskProvider {
                    compilerOptions {
                        freeCompilerArgs.add("-Xexpect-actual-classes")
                    }
                }
            }
        }
    }

    mavenPublishing {
        coordinates(group.toString(), project.name, version.toString())
        pom {
            name.set("moleKool")
            description.set("various solutions for molecular dynamics")
            url.set("https://github.com/nort3x/moleKool")

            licenses {
                license {
                    name.set("GNU General Public License v3.0")
                    url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                }
            }

            issueManagement {
                system.set("GitHub Issues")
                url.set("https://github.com/nort3x/moleKool/issues")
            }

            developers {
                developer {
                    id.set("nort3x")
                    name.set("Human Ardaki")
                    email.set("humanardaki@gmail.com")
                }
            }

            scm {
                url.set("https://github.com/nort3x/moleKool")
            }
        }

        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    val node = rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>()
    node.version = "22.0.0"
}

tasks.withType<Jar> {
    enabled = false
}
tasks.withType<AbstractPublishToMaven> {
    enabled = false
}

kotlin {
    jvm()
}
tasks.withType(PublishToMavenRepository::class.java).forEach { it.enabled = false }
tasks.withType(PublishToMavenLocal::class.java).forEach { it.enabled = false }
