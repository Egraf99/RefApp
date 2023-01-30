package com.egraf.refapp.utils

//class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//    companion object {
//        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)
//
//        fun <T> error(data: T?, message: String): Resource<T> =
//            Resource(status = Status.ERROR, data = data, message = message)
//
//        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, message = null)
//    }
//}

sealed class Resource<out T> private constructor(val status: Status) {
    abstract val data: () -> T
    abstract val exception: () -> Exception

    object Loading : Resource<Nothing>(Status.LOADING) {
        override val data: () -> Nothing =
            { throw IllegalStateException("get data from loading resource") }
        override val exception: () -> Exception =
            { throw IllegalStateException("get exception from loading resource") }
    }

    class Error(override val exception: () -> Exception) : Resource<Nothing>(Status.ERROR) {
        override val data: Nothing
            get() = throw IllegalStateException("get data from error resource")
    }

    class Success<out T>(override val data: () -> T) : Resource<T>(Status.SUCCESS) {
        override val exception: () -> Exception
            get() = throw IllegalStateException("get exception from success resource")
    }


    companion object {
        fun <T> success(data: T): Resource<T> = Success { data }
        fun loading(): Resource<Nothing> = Loading
        fun error(exception: Exception): Resource<Nothing> = Error { exception }
        fun error(message: String): Resource<Nothing> = error(RuntimeException(message))
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
