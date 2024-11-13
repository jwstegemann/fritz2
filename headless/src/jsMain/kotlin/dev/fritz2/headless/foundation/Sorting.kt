package dev.fritz2.headless.foundation

enum class SortDirection {
    NONE, ASC, DESC
}

data class Sorting<T>(val comparatorAscending: Comparator<T>, val comparatorDescending: Comparator<T>)

data class SortingOrder<T>(val sorting: Sorting<T>, val direction: SortDirection)
