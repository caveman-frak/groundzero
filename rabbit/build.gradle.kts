plugins {
    id("application-conventions")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.amqp:spring-rabbit-stream")
    implementation("com.rabbitmq:stream-client:0.16.0")
}

springBoot {
    mainClass.set("uk.co.bluegecko.rabbit.Main")
}