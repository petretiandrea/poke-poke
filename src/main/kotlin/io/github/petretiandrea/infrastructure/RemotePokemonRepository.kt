package io.github.petretiandrea.infrastructure

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.Pokemon
import io.github.petretiandrea.domain.pokemon.PokemonRepository
import io.github.petretiandrea.infrastructure.apis.PokeApi

class RemotePokemonRepository(
    private val pokeApi: PokeApi
) : PokemonRepository {
    override suspend fun findPokemon(name: String): Either<FindPokemonError, Pokemon> {
        return pokeApi.getPokemonSpecie(name)
            .map { specie ->
                val description = when {
                    specie.flavorTextEntries.isEmpty() -> ""
                    else -> specie.flavorTextEntries
                        .firstOrNull { it.language.name == "en" }?.flavorText
                        ?: specie.flavorTextEntries.first().flavorText
                }
                Pokemon(
                    name = specie.name,
                    description = description,
                    habitat = specie.habitat,
                    isLegendary = specie.isLegendary
                )
            }
            .mapLeft { error ->
                when (error) {
                    is ApiError.HttpError<*> -> when (error.code) {
                        404 -> FindPokemonError.PokemonNotFound(name)
                        else -> FindPokemonError.UnexpectedError(error.reason)
                    }
                    else -> FindPokemonError.UnexpectedError(error.reason)
                }
            }
    }

}
