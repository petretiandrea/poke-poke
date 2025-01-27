package io.github.petretiandrea.infrastructure.apis

import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.common.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.io.IOException

// TODO: code decision
// raw api implementation. This can also be generated using openapi generator.
class PokeApi(
    private val httpClient: HttpClient
) {
    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2"
    }

    suspend fun getPokemonSpecie(pokemonName: String): ApiResponse<PokemonResponse, String> {
        return ApiResponse.catch {
            httpClient.get("pokemon-species/${pokemonName}") {
                expectSuccess = true
            }
        }
        .map { it.body<PokemonResponse>() }
        .mapLeft { error ->
            when (error) {
                is ResponseException -> ApiError.HttpError(
                    error.response.status.value,
                    error.response.bodyAsText(),
                    error
                )
                is IOException -> ApiError.NetworkError(error)
                else -> ApiError.UnexpectedError(error)
            }
        }
    }
}