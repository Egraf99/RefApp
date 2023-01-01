package com.egraf.refapp.views.custom_views

sealed class GameComponent<out S: Saving> {
    abstract val isEmpty: Boolean
    fun getText(): String = when (this) {
        is Empty -> ""
        is Fill -> value.title
    }

    internal object Empty : GameComponent<Nothing>() {
        override val isEmpty: Boolean = true
        override fun toString(): String = "EmptyGameComponent"
    }

    internal class Fill<out S: Saving>(internal val value: S) : GameComponent<S>() {
        override val isEmpty: Boolean = false
        override fun toString(): String = "GC: $value"
    }

    companion object {
        operator fun <S: Saving> invoke(): GameComponent<S> = Empty
        operator fun <S: Saving> invoke(value: S): GameComponent<S> = Fill(value)
    }
}

interface Saving {
    val title: String
}