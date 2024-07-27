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
        languageVersion.set(JavaLanguageVersion.of(22))
        vendor.set(JvmVendorSpec.ORACLE)
    }
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

repositories {
    mavenCentral()
    maven("https://packages.atlassian.com/maven-3rdparty/")
    maven("https://repo.osgeo.org/repository/release/")
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
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")
    testFixturesImplementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.instancio:instancio-junit:4.6.0")
    testFixturesImplementation("net.datafaker:datafaker:2.2.2")
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("org.junit-pioneer:junit-pioneer:2.2.0")
                implementation("net.jqwik:jqwik:1.9.0")
            }
        }

        withType<JvmTestSuite> {
            useJUnitJupiter()
            applySpringTest(this)
        }

        val test by getting(JvmTestSuite::class)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview", "-XX:+EnableDynamicAgentLoading")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}