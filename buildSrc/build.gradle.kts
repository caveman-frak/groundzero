plugins {
    `kotlin-dsl`
    `groovy-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    maven {
        url = uri("https://maven.xillio.com/artifactory/libs-release")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.3.0")
    implementation("io.freefair.gradle:lombok-plugin:8.6")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
}