package io.fritz2.lenses

interface Lens<P, T> {
    val _id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
    fun apply(parent: P, mapper: (T) -> T): P = set(parent, mapper(get(parent)))


    operator fun <X> plus(other: Lens<T, X>): Lens<P, X> = object :
        Lens<P, X> {
        override val _id = "${this@Lens._id}.${other._id}"
        override fun get(parent: P): X = other.get(this@Lens.get(parent))
        override fun set(parent: P, value: X): P = this@Lens.set(parent, other.set(this@Lens.get(parent), value))
    }
}

inline fun <P, T> buildLens(id: String, crossinline getter: (P) -> T, crossinline setter: (P, T) -> P) = object :
    Lens<P, T> {
    override val _id = id
    override fun get(parent: P): T = getter(parent)
    override fun set(parent: P, value: T): P = setter(parent, value)
}