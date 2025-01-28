package io.github.petretiandrea.infrastructure.apis.pokemon

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.infrastructure.apis.adaptToApiError
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class KtorApiResponseExtensionTest {

    @Test
    fun `should return HttpError when ktor return ResponseException`(): Unit = runBlocking {
        val error =
            ResponseException(mock { on { status }.thenReturn(HttpStatusCode.BadRequest) }, "")
        val result = Either.Left(error).adaptToApiError { "body_error" }
        result
            .mapLeft { it as ApiError.HttpError }
            .onLeft {
                assertEquals(HttpStatusCode.BadRequest.value, it.code)
                assertEquals("body_error", it.errorBody)
            }
    }

    @Test
    fun `should return NetworkError when ktor return IOException`(): Unit = runBlocking {
        val error = IOException("Network error")
        val result = Either.Left(error).adaptToApiError { "body_error" }
        result.mapLeft { it as ApiError.NetworkError }.onLeft { assert(it.reason is IOException) }
    }

    @Test
    fun `should return UnexpectedError when ktor return unhandled error`(): Unit = runBlocking {
        val error = SerializationException("Failed to serialize")
        val result = Either.Left(error).adaptToApiError { "body_error" }
        result
            .mapLeft { it as ApiError.UnexpectedError }
            .onLeft { assert(it.reason is SerializationException) }
    }
}
