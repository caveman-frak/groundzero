plugins {
    id("application-conventions")
}

dependencies {
    implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("org.jfree:org.jfree.svg:5.0.6")
    implementation("org.apache.xmlgraphics:batik-svggen:1.17")
    implementation("org.apache.xmlgraphics:batik-dom:1.17")

}

springBoot {
    mainClass.set("uk.co.bluegecko.utility.Main")
}