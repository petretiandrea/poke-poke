package io.github.petretiandrea.infrastructure.apis.funtranslation

import arrow.core.Either
import io.github.petretiandrea.common.ApiResponse
import io.github.petretiandrea.common.adaptToApiError
import io.github.petretiandrea.domain.translation.TranslationType
import io.github.petretiandrea.domain.translation.Translator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import org.slf4j.LoggerFactory

class FunTranslationApi(private val httpClient: HttpClient) : Translator {

    private val logger = LoggerFactory.getLogger(FunTranslationApi::class.java)

    companion object {
        const val BASE_URL = "https://api.funtranslations.com/translate"
    }

    // TODO: production tip. Log error
    override suspend fun translate(
        translationType: TranslationType,
        text: String
    ): Either<Error, String> {
        return funTranslate(translationType, text)
            .map { it.contents.translated }
            .mapLeft { Error(it.reason) }
    }

    private suspend fun funTranslate(
        type: TranslationType,
        text: String
    ): ApiResponse<TranslationResponse, String> {
        val requestPath =
            when (type) {
                TranslationType.YODA -> "yoda"
                TranslationType.SHAKESPEARE -> "shakespeare"
            }

        return ApiResponse.catch {
                httpClient.post("/${requestPath}.json") {
                    expectSuccess = true
                    contentType(ContentType.Application.Json)
                    setBody(RequestTranslation(text))
                }
            }
            .map { it.body<TranslationResponse>() }
            .adaptToApiError { it.bodyAsText() }
            .onLeft {
                logger.error("Error during funtranslation", it.reason)
            }
    }

    private data class RequestTranslation(val text: String)
}
