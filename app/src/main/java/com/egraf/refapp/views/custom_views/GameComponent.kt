package com.egraf.refapp.views.custom_views

sealed class GameComponent<out T, out S: Saving<T>> {
    abstract val isEmpty: Boolean
    fun getText(): String = when (this) {
        is Empty -> ""
        is Fill -> value.title
    }

    fun getValueOrThrow(e: Exception): T = when(this) {
        is Empty -> throw e
        is Fill -> value.savedValue
    }

    internal object Empty : GameComponent<Nothing, Nothing>() {
        override val isEmpty: Boolean = true
        override fun toString(): String = "EmptyGameComponent"
    }

    internal class Fill<T, out S: Saving<T>>(internal val value: S) : GameComponent<T, S>() {
        override val isEmpty: Boolean = false
        override fun toString(): String = "GC: $value"
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