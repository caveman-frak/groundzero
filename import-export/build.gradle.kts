plugins {
    id("application-conventions")
}

dependencies {
    implementation("com.github.beanio:beanio:3.0.2")
    implementation("com.opencsv:opencsv:5.9")
    implementation("org.apache.commons:commons-csv:1.11.0")
    implementation("de.siegmar:fastcsv:3.2.0")
    implementation("net.sf.flatpack:flatpack:4.0.18")
    implementation("org.csveed:csveed:0.8.1")
}

springBoot {
    mainClass.set("uk.co.bluegecko.csv.Main")
}