package io.github.petretiandrea.domain.pokemon

import io.github.petretiandrea.domain.translation.TranslationType
import io.github.petretiandrea.domain.translation.Translator

class PokemonTranslator(private val translator: Translator) {

    companion object {
        const val HABITAT_CAVE = "cave"
    }

    suspend fun translateDescription(pokemon: PokemonReadModel): PokemonReadModel {
        val translationType =
            when {
                pokemon.habitat == HABITAT_CAVE || pokemon.isLegendary -> TranslationType.YODA
                else -> TranslationType.SHAKESPEARE
            }

        return translator
            .translate(translationType, pokemon.description)
            .fold(ifLeft = { _ -> pokemon }, ifRight = { pokemon.copy(description = it) })
    }
}
