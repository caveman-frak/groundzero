plugins {
    id("library-conventions")
}

dependencies {
    api("org.javamoney:moneta:1.4.4")
    api("org.zalando:jackson-datatype-money:1.3.0")
    api("tech.units:indriya:2.2")
    api("tech.uom.lib:uom-lib-jackson:2.1")
    api("tech.uom.lib:uom-lib-assertj:2.1")
}

springBoot {
    mainClass.set("uk.co.bluegecko.common.Main")
}