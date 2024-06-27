plugins {
    id("application-conventions")
}

dependencies {
    implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")
}

springBoot {
    mainClass.set("uk.co.bluegecko.utility.Main")
}