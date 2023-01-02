package com.egraf.refapp.views.custom_views

sealed class GameComponent<out T, out S: Saving<T>> {
    abstract val isEmpty: Boolean
    fun getOrElse(default: () -> @UnsafeVariance S): S = when (this) {
        is Empty -> default()
        is Fill -> value
    }
    fun getText(): String = when (this) {
        is Empty -> ""
        is Fill -> value.title
    }
    fun getValueOrThrow(e: Exception): T = when(this) {
        is Empty -> throw e
        is Fill -> value.savedValue
    }

    abstract fun <B, A: Saving<B>> map(f: (S) -> A): GameComponent<B, A>

    internal object Empty : GameComponent<Nothing, Nothing>() {
        override val isEmpty: Boolean = true
        override fun toString(): String = "EmptyGameComponent"
        override fun <B, A: Saving<B>> map(f: (Nothing) -> A): GameComponent<Nothing, Nothing> = this
    }

    internal class Fill<T, out S: Saving<T>>(internal val value: S) : GameComponent<T, S>() {
        override val isEmpty: Boolean = false
        override fun toString(): String = "GC: $value"
        override fun <B, A : Saving<B>> map(f: (S) -> A): GameComponent<B, A> = Fill(f(value))
    }

    companion object {
        operator fun <T, S: Saving<T>> invoke(): GameComponent<T, S> = Empty
        operator fun <T, S: Saving<T>> invoke(value: S): GameComponent<T, S> = Fill(value)
    }
}

interface Saving<out T> {
    val title: String
    val savedValue: T
}