plugins {
    id("application-conventions")
}

dependencies {
    implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("org.jfree:org.jfree.svg:5.0.6")
}

springBoot {
    mainClass.set("uk.co.bluegecko.utility.Main")
}