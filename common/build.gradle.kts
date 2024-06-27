plugins {
    id("library-conventions")
}

dependencies {
    api("org.javamoney:moneta:1.4.4")
}

springBoot {
    mainClass.set("uk.co.bluegecko.common.Main")
}