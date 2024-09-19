plugins {
    id("library-conventions")
}

dependencies {
    implementation("com.github.beanio:beanio:3.1.0")
}

springBoot {
    mainClass.set("uk.co.bluegecko.override.Main")
}