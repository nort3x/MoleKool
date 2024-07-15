kotlin{
    sourceSets.jvmMain{
        dependencies {
            implementation(project(":molekule-core"))
            implementation(project(":molekule-visual"))
        }
    }
}