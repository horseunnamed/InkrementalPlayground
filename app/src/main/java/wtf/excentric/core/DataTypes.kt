package wtf.excentric.core


sealed class Try<out T> {
    data class Ok<T>(val value: T) : Try<T>()
    data class Nope(val error: Throwable) : Try<Nothing>()
}