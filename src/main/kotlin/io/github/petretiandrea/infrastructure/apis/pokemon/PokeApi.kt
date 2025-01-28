package io.github.petretiandrea.infrastructure.apis.pokemon

import io.github.petretiandrea.common.ApiResponse
import io.github.petretiandrea.common.adaptToApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.LoggerFactory

// TODO: code decision
// raw api implementation. This can also be generated using openapi generator.
class PokeApi(private val httpClient: HttpClient) {

    private val logger = LoggerFactory.getLogger(PokeApi::class.java)

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }

    suspend fun getPokemonSpecie(pokemonName: String): ApiResponse<PokemonResponse, String> {
        return ApiResponse.catch {
                httpClient.get("pokemon-species/${pokemonName}") { expectSuccess = true }
            }
            .map { it.body<PokemonResponse>() }
            .adaptToApiError { it.bodyAsText() }
            .onLeft {
                logger.error("Failed to get pokemon from pokeapi", it.reason)
            }
    }
}
