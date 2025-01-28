package io.github.petretiandrea

import com.google.gson.FieldNamingPolicy
import io.github.petretiandrea.domain.pokemon.PokemonTranslator
import io.github.petretiandrea.domain.translation.Translator
import io.github.petretiandrea.infrastructure.PokemonApiReadModelAdapter
import io.github.petretiandrea.infrastructure.PokemonRepositoryApiAdapter
import io.github.petretiandrea.infrastructure.apis.funtranslation.FunTranslationApi
import io.github.petretiandrea.infrastructure.apis.pokemon.PokeApi
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DependencyInjection {

    @Bean
    fun pokemonRepository(pokeApi: PokeApi) = PokemonRepositoryApiAdapter(pokeApi, PokemonApiReadModelAdapter())

    @Bean
    fun pokemonTranslator(translator: Translator) = PokemonTranslator(translator)

    @Bean
    fun funTranslator(): Translator = FunTranslationApi(
        HttpClient {
            followRedirects = true
            defaultRequest { url(FunTranslationApi.BASE_URL) }
            install(ContentNegotiation) {
                gson()
            }
        }
    )

    @Bean
    fun pokeApi(): PokeApi {
        return PokeApi(
            HttpClient {
                followRedirects = true
                defaultRequest { url(PokeApi.BASE_URL) }
                install(ContentNegotiation) {
                    gson { setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) }
                }
            }
        )
    }
}
