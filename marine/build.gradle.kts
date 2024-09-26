plugins {
    `library-conventions`
}

dependencies {
    implementation(project(":common"))
    implementation("com.uber:h3:4.1.1")
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("org.locationtech.jts:jts-core:1.20.0")
    implementation("net.agkn:hll:1.6.0")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("io.vavr:vavr:0.10.4")
    testFixturesImplementation(testFixtures(project(":common")))
    testFixturesImplementation("com.uber:h3:4.1.1")
    testFixturesImplementation("org.locationtech.spatial4j:spatial4j:0.8")
}

springBoot {
    mainClass.set("uk.co.bluegecko.marine.Main")
}