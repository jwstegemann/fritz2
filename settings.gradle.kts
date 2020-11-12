pluginManagement {
  repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    gradlePluginPortal()
    mavenLocal()
  }
}
rootProject.name = "fritz2"

include(
    "core",
    "lenses-annotation-processor",
    "test-server",
    "styling",
    "components"
)

enableFeaturePreview("GRADLE_METADATA")