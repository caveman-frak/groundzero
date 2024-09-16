plugins {
    id("application-conventions")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":marine"))
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.amqp:spring-rabbit-stream")
    implementation("com.rabbitmq:stream-client:0.17.0")
    testFixturesImplementation(testFixtures(project(":common")))
    testFixturesImplementation(testFixtures(project(":marine")))
}

springBoot {
    mainClass.set("uk.co.bluegecko.rabbit.Main")
}