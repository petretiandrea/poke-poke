package io.github.petretiandrea.application

import arrow.core.Either
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.domain.pokemon.PokemonRepository
import org.springframework.stereotype.Component

@Component
class GetPokemon(private val pokemonRepository: PokemonRepository) {
    suspend operator fun invoke(pokemonName: String): Either<FindPokemonError, PokemonReadModel> {
        return pokemonRepository.findPokemon(pokemonName)
    }
}
