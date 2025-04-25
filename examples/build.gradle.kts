tasks.withType(PublishToMavenRepository::class.java).forEach { it.enabled = false }
tasks.withType(PublishToMavenLocal::class.java).forEach { it.enabled = false }

kotlin {
    sourceSets.jvmMain {
        dependencies {
            implementation(project(":molekool-core"))
            implementation(project(":molekool-visual"))
            implementation(project(":molekool-bind"))
        }
    }
}
