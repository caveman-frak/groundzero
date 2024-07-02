@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("idea")
    id("jvm-test-suite")
    id("java-test-fixtures")
    id("org.springframework.boot")
    id("io.freefair.lombok")
    id("com.adarshr.test-logger")
}

group = "co.uk.bluegecko.groundzero"
version = "1.0"
description = "Ground Zero Testing Area"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ORACLE)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.apache.commons:commons-lang3")
    runtimeOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
    testFixturesImplementation("org.instancio:instancio-junit:4.6.0")
    testFixturesImplementation("net.datafaker:datafaker:2.2.2")
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }

        withType<JvmTestSuite> {
            useJUnitJupiter()
            applySpringTest(this)
        }

        val test by getting(JvmTestSuite::class)
    }
}

tasks.withType<Test> {
    jvmArgs(setOf("-XX:+EnableDynamicAgentLoading"))
}