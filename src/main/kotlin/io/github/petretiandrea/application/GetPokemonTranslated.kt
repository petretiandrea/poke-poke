package io.github.petretiandrea.application

import arrow.core.Either
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.domain.pokemon.PokemonRepository
import io.github.petretiandrea.domain.pokemon.PokemonTranslator
import org.springframework.stereotype.Component

@Component
class GetPokemonTranslated(
    private val pokemonRepository: PokemonRepository,
    private val pokemonTranslator: PokemonTranslator
) {
    suspend operator fun invoke(pokemonName: String): Either<FindPokemonError, PokemonReadModel> {
        return pokemonRepository.findPokemon(pokemonName).map {
            pokemonTranslator.translateDescription(it)
        }
    }
}
