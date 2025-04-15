package me.zayedbinhasan.travelblog.util

typealias ErrorInterface = Error
typealias EmptyResult<E> = Result<Unit, E>

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : ErrorInterface>(val error: E) : Result<Nothing, E>
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> = map { }

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(map(data))
    is Result.Error -> this
}

inline fun <T, E : Error, R> Result<T, E>.flatMap(map: (T) -> Result<R, E>): Result<R, E> =
    when (this) {
        is Result.Success -> map(data)
        is Result.Error -> this
    }

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}


inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}