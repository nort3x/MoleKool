import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("multiplatform") version "2.0.0"
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.9.20"
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.iskportal"
    version = "1.0.0-omega.0"
}

val localProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

fun propertyOrEnv(key: String): String? =
    project.findProperty(key) as String?
        ?: localProperties.getProperty(key)
        ?: System.getenv(key.replace(".", "_").uppercase())

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("maven-publish")
        plugin("org.jetbrains.dokka")
        plugin("signing")
    }


    // docs

    val dokkaOutputDir = layout.buildDirectory.dir("dokka")
    tasks.dokkaHtml { outputDirectory.set(file(dokkaOutputDir)) }
    val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") { delete(dokkaOutputDir) }
    val javadocJar = tasks.create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
        from(dokkaOutputDir)
    }

    publishing {
        repositories {
            maven {
                name = "Sonatype"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = propertyOrEnv("sonatype.username")
                    password = propertyOrEnv("sonatype.password")
                }
            }
        }

        publications {
            publications.withType<MavenPublication> {
                artifact(javadocJar)

                pom {
                    name.set("MoleKule")
                    description.set("various solutions for molecular dynamics")
                    url.set("https://github.com/Independent-Society-of-Knowledge/MoleKule")

                    licenses {
                        license {
                            name.set("GNU General Public License v3.0")
                            url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                        }
                    }

                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("https://github.com/Independent-Society-of-Knowledge/MoleKule/issues")
                    }

                    developers {
                        developer {
                            id.set("nort3x")
                            name.set("Human Ardaki")
                            email.set("humanardaki@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com:Independent-Society-of-Knowledge/MoleKule.git")
                        developerConnection.set("scm:git:ssh://github.com:Independent-Society-of-Knowledge/MoleKule.git")
                        url.set("https://github.com/Independent-Society-of-Knowledge/MoleKule")
                    }
                }
            }
        }
    }

    signing {
        if (propertyOrEnv("signing.gnupg.keyName") != null) {
            useInMemoryPgpKeys(
                propertyOrEnv("signing.key"),
                propertyOrEnv("signing.password"),
            )
            sign(publishing.publications)
            println("singed ${project.name}")
        }
    }

    kotlin {
        withSourcesJar(true)
        jvmToolchain(21)
        jvm()
//        js {
//            browser()
//        }
        sourceSets {
            commonTest.dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
            }
        }
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    val node = rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>()
    node.version = "22.0.0"
    node.download = true
}

kotlin {
    jvm("root")
}

tasks.withType<Jar> {
    enabled = false
}
tasks.withType<AbstractPublishToMaven> {
    enabled = false
}

