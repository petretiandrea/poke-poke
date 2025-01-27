package io.github.petretiandrea.common

import arrow.core.Either

typealias ApiResponse<T, E> = Either<ApiError<out E>, T>

sealed interface ApiError<E> {
    val reason: Throwable

    data class HttpError<E>(
        val code: Int,
        val errorBody: E?,
        override val reason: Throwable
    ) : ApiError<E>

    data class NetworkError(
        override val reason: Throwable
    ) : ApiError<Nothing>

    data class UnexpectedError(
        override val reason: Throwable
    ) : ApiError<Nothing>
}