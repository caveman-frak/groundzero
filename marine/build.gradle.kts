plugins {
    `library-conventions`
}

dependencies {
    implementation(project(":common"))
    implementation("com.uber:h3:4.1.1")
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("org.locationtech.jts:jts-core:1.20.0")
    testFixturesImplementation(testFixtures(project(":common")))
    testFixturesImplementation("com.uber:h3:4.1.1")
    testFixturesImplementation("org.locationtech.spatial4j:spatial4j:0.8")
}

springBoot {
    mainClass.set("uk.co.bluegecko.marine.Main")
}