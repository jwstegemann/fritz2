pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}
rootProject.name = "fritz2"

include(
    "core",
    "lenses-annotation-processor",
    "test-server",
    "headless",
    "headless-demo:headless-demo-listbox",
    "headless-demo:headless-demo-checkboxgroup"
)
