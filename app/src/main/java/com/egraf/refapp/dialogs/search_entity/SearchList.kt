package com.egraf.refapp.dialogs.search_entity

sealed class SearchList<out E> {
    abstract fun isEmpty(): Boolean

    fun cons(e: @UnsafeVariance E): SearchList<E> = Cons(e, this)
    fun toList(): List<E> = toList(this, listOf())
    fun <B> foldLeft(identity: B, f: (B) -> (E) -> B): B {
        tailrec fun foldLeft(acc: B, sl: SearchList<E>, f: (B) -> (E) -> B): B = when (sl) {
            is Nil -> acc
            is Cons -> foldLeft(f(acc)(sl.head), sl.tail, f)
        }
        return foldLeft(identity, this, f)
    }


    private object Nil : SearchList<Nothing>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "[NIL]"
    }

    private class Cons<E>(
        internal val head: E,
        internal val tail: SearchList<E>
    ) : SearchList<E>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String {
            tailrec fun toString(acc: String, list: SearchList<E>): String = when (list) {
                is Nil -> acc
                is Cons -> toString("$acc${list.head}, ", list.tail)
            }
            return "${toString("", this)}[NIL]"
        }
    }

    companion object {
        operator fun <E> invoke(vararg ez: E): SearchList<E> =
            ez.foldRight(Nil as SearchList<E>) { e, acc -> Cons(e, acc) }

        fun <E> toList(sList: SearchList<E>, list: List<E>): List<E> =
            sList.foldLeft(list) { l ->
                { e ->
                    println("$l $e ${listOf(e) + l}")
                    l + listOf(e)
                }
            }
    }
}