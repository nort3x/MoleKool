# moleKool
various solutions for molecular dynamics and related fields

![screenshot](logo.png)
[![Publisher](https://github.com/nort3x/MoleKool/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/nort3x/MoleKool/actions/workflows/gradle-publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.nort3x/molekool-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%io.github.nort3x.molekool%22%20AND%20a:%22molekool-core%22)

## Install

#### Gradle
To use this library in your Gradle project, add the following to your `build.gradle(.kts)` file:

```gradle
dependencies {
    // Various generators
    implementation("com.github.nort3x:molekool-core:$molekoolVersion")
    // Using Kool engine to visualize environments
    implementation("com.github.nort3x:molekool-visual:$molekoolVersion")
    // Basic bindings to various MD third-party tools (experimental)
    implementation("com.github.nort3x:molekool-bind:$molekoolVersion")
}
```
Replace `$molekoolVersion` with the latest version of the library.

#### Maven
To use this library in your Maven project, add the following to your `pom.xml` file:

```xml
<dependencies>
    <!-- Various generators -->
    <dependency>
        <groupId>com.github.nort3x</groupId>
        <artifactId>molekool-core</artifactId>
        <version>VERSION</version>
    </dependency>
    <!-- Using Kool engine to visualize environments -->
    <dependency>
        <groupId>com.github.nort3x</groupId>
        <artifactId>molekool-visual</artifactId>
        <version>VERSION</version>
    </dependency>
    <!-- Basic bindings to various MD third-party tools (experimental) -->
    <dependency>
        <groupId>com.github.nort3x</groupId>
        <artifactId>molekool-bind</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

Replace `VERSION` with the latest version of the library.
