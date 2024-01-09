pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}
rootProject.name = "fritz2"

plugins {
    id("de.fayard.refreshVersions") version "0.60.3"
}

refreshVersions {
}

include(
    "core",
    "lenses-annotation-processor",
    "test-server",
    "headless",
    "headless-demo",
    "snippets",
    "serialization",
    // examples:
    ":examples:gettingstarted",
    ":examples:nestedmodel",
    ":examples:performance",
    ":examples:remote",
    ":examples:masterdetail",
    ":examples:routing",
    ":examples:tictactoe",
    ":examples:todomvc",
    ":examples:validation",
    ":examples:webcomponent",
)
