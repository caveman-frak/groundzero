plugins {
    id("library-conventions")
}

dependencies {
    api("org.javamoney:moneta:1.4.4")
    api("org.zalando:jackson-datatype-money:1.3.0")
}

springBoot {
    mainClass.set("uk.co.bluegecko.common.Main")
}