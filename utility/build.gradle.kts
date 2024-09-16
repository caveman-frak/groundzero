plugins {
    id("application-conventions")
}

dependencies {
    implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("org.jfree:org.jfree.svg:5.0.6")
    implementation("org.apache.xmlgraphics:batik-svggen:1.17")
    implementation("org.apache.xmlgraphics:batik-dom:1.17")
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("org.locationtech.jts:jts-core:1.20.0")
    implementation("javax.media:jai-core:1.1.3")
    implementation("org.geotools:gt-shapefile:31.3") {
        exclude("javax.media", "jai_core")
    }
    implementation("org.geotools:gt-epsg-hsql:31.2") {
        exclude("javax.media", "jai_core")
    }
//    implementation("org.opengis:geoapi:3.0.2")
    implementation("com.javadocmd:simplelatlng:1.4.0")
}

springBoot {
    mainClass.set("uk.co.bluegecko.utility.Main")
}