plugins {
    id("application-conventions")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
//    latest version "22.0.1" or LTS "21.0.4" won't run on MacOS 10.x, need to downgrade to "17.0.12"
    version = "17.0.12"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {

}

application {
    mainClass = "uk.co.bluegecko.ui.geometry.javafx.GeometryDisplay"
}

springBoot {
    mainClass.set("uk.co.bluegecko.ui.Main")
}