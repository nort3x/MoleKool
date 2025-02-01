plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.dokka)
    `maven-publish`
    `signing`
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "io.github.nort3x"
    version = "1.0.0.alpha-1"
}

val notJsSafe = listOf("molekool-bind")

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("maven-publish")
        plugin("org.jetbrains.dokka")
        plugin("signing")
    }


    kotlin {
        withSourcesJar(true)
        jvmToolchain(21)
        jvm()
        if (project.name !in notJsSafe)
            js {
                browser()
            }
        sourceSets {
            commonTest.dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
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

        publications {
            publications.withType<MavenPublication> {
                artifact(javadocJar)

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
                }
            }
        }
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