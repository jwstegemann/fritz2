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
    "headless-demo",
    "snippets",
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
