package com.egraf.refapp.dialogs.search_entity

import com.egraf.refapp.database.entities.League

fun <E> List<E>.toSearchList(): SearchList<E> =
    this.foldRight(SearchList.Nil as SearchList<E>) { e, acc -> acc.cons(e) }

sealed class SearchList<out E> {
    abstract fun isEmpty(): Boolean
    override fun equals(other: Any?): Boolean = when {
        other == null -> false
        other.javaClass == this.javaClass -> Companion.equals(this, other as SearchList<E>)
        else -> false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun filter(p: (E) -> Boolean): SearchList<E> = foldLeft(Nil as SearchList<E>) { acc ->
        { e -> if (p(e)) acc.cons(e) else acc }
    }.reverse()

    fun cons(e: @UnsafeVariance E): SearchList<E> = Cons(e, this)
    fun toList(): List<E> = toList(this, listOf())
    fun reverse(): SearchList<E> =
        this.foldLeft(Nil as SearchList<E>) { acc -> { e -> acc.cons(e) } }

    fun <B> foldLeft(identity: B, f: (B) -> (E) -> B): B {
        tailrec fun foldLeft(acc: B, sl: SearchList<E>, f: (B) -> (E) -> B): B = when (sl) {
            is Nil -> acc
            is Cons -> foldLeft(f(acc)(sl.head), sl.tail, f)
        }
        return foldLeft(identity, this, f)
    }


    internal object Nil : SearchList<Nothing>() {
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
        tailrec fun <E> equals(sl1: SearchList<E>, sl2: SearchList<E>): Boolean = when (sl1) {
            is Nil -> when (sl2) {
                is Nil -> true
                is Cons -> false
            }
            is Cons -> when (sl2) {
                is Nil -> false
                is Cons -> when {
                    sl1.head != sl2.head -> false
                    else -> equals(sl1.tail, sl2.tail)
                }
            }
        }
    }
}

fun main() {
    val ls = listOf(League(name = "Some"), League(name = "Body"))
    println(ls.toSearchList())
}