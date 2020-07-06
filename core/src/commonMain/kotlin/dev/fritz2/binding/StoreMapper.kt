package dev.fritz2.binding

/**
 * StoreMapper
 */
interface StoreMapper<P, T> {
    val _id: String
    //Lens Get Parent/Source
    fun get(value: P): T
    //Lens Set Destination
    fun set(parent: P, value: T): P

    operator fun <X> plus(other: StoreMapper<T, X>): StoreMapper<P, X> = object :
        StoreMapper<P, X> {
        override val _id = "${this@StoreMapper._id}.${other._id}"
        override fun get(parent: P): X = other.get(this@StoreMapper.get(parent))
        override fun set(parent: P, value: X): P = this@StoreMapper.set(parent, other.set(this@StoreMapper.get(parent), value))
    }

    fun apply(parent: P, mapper: (T) -> T): P = set(parent, mapper(get(parent)))

}