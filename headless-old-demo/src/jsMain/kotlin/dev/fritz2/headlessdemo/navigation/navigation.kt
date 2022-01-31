package dev.fritz2.headlessdemo.navigation

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.routing.Router
import dev.fritz2.routing.routerOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class NavigationDsl


class Navigation(definition: Navigation.() -> Unit) {
    val router: Router<String>
    val currentPage: Flow<Page>

    var defaultPage = ""
    fun defaultPage(value: String) {
        defaultPage = value
    }

    val categories = mutableListOf<Category>()
    fun category(
        name: String,
        displayInOverview: Boolean = true,
        build: Category.() -> Unit
    ) {
        categories += Category(name, displayInOverview).apply(build)
    }

    init {
        definition()

        router = routerOf(defaultPage)
        currentPage = router.data.map { target ->
            categories
                .flatMap { it.pages }
                .flatMap { it.subpages + it }
                .find { it.target == target }!!
        }
    }
}

@NavigationDsl
data class Category(val name: String, val displayInOverview: Boolean = true) {
    val pages = mutableListOf<PageWithSubpages>()
    fun page(
        name: String,
        target: String,
        content: RenderContext.() -> Unit,
        preview: (RenderContext.() -> Unit)? = null,
        build: PageWithSubpages.() -> Unit = {}
    ) {
        pages += PageWithSubpages(name, target, content, preview).apply(build)
    }
}

@NavigationDsl
interface Page {
    val name: String
    val target: String
    val content: RenderContext.() -> Unit
    val preview: (RenderContext.() -> Unit)?
}

@NavigationDsl
data class PageWithSubpages(
    override val name: String,
    override val target: String,
    override val content: RenderContext.() -> Unit,
    override val preview: (RenderContext.() -> Unit)? = null
) : Page {
    val subpages = mutableListOf<Page>()
    fun subpage(name: String, target: String, content: RenderContext.() -> Unit) {
        subpages += PageWithSubpages(name, target, content)
    }
}


fun previewImage(url: String): RenderContext.() -> Unit = {
    img("object-cover") {
        src(url)
    }
}
