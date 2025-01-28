package io.github.petretiandrea.infrastructure.apis

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.common.ApiResponse
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.io.IOException

suspend fun <E, T> Either<Throwable, T>.adaptToApiError(
    bodyParse: suspend (HttpResponse) -> E
): ApiResponse<T, E> {
    return mapLeft { error ->
        when (error) {
            is ResponseException ->
                ApiError.HttpError(error.response.status.value, bodyParse(error.response), error)
            is IOException -> ApiError.NetworkError(error)
            else -> ApiError.UnexpectedError(error)
        }
    }
}
