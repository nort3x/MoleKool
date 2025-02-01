kotlin{
    sourceSets.jvmMain{
        dependencies {
            implementation(project(":molekool-core"))
            implementation(project(":molekool-visual"))
        }
    }
}