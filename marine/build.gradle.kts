plugins {
    `library-conventions`
}

dependencies {
    implementation(project(":common"))
    implementation("com.uber:h3:4.1.1")
    testFixturesImplementation(testFixtures(project(":common")))
}

springBoot {
    mainClass.set("uk.co.bluegecko.marine.Main")
}