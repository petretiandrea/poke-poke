package io.github.petretiandrea.application

import arrow.core.Either
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.Pokemon
import io.github.petretiandrea.domain.pokemon.PokemonRepository

data class GetPokemonQuery(
    val pokemonName: String,
    val requireFunTranslation: Boolean = false
)

class GetPokemon(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(pokemonName: String): Either<FindPokemonError, Pokemon> {
        return pokemonRepository.findPokemon(pokemonName)
    }
}