package io.github.petretiandrea

import com.google.gson.FieldNamingPolicy
import io.github.petretiandrea.infrastructure.apis.PokeApi
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking


fun main() {

    val client = HttpClient {
        followRedirects = true
        defaultRequest {
            url("https://pokeapi.co/api/v2/")
        }
        install(ContentNegotiation) {
            gson {
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
        }
    }

    val pokeApi = PokeApi(client);

    runBlocking {
        pokeApi.getPokemonSpecie("aegislash")
            .onRight {
                println(it)
            }
            .onLeft {
                println(it)
            }
    }
}