plugins {
    id("java-conventions")
    id("application")
}

application.applicationDefaultJvmArgs = setOf("-XX:+EnableDynamicAgentLoading")

dependencies {
    implementation(project(":common"))
    implementation(testFixtures(project(":common")))
}