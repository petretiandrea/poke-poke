package io.github.petretiandrea.infrastructure

import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.infrastructure.apis.pokemon.PokemonResponse

class PokemonApiReadModelAdapter {
    fun map(specie: PokemonResponse): PokemonReadModel {
        val description =
            when {
                specie.flavorTextEntries.isEmpty() -> ""
                else ->
                    specie.flavorTextEntries.firstOrNull { it.language.name == "en" }?.flavorText
                        ?: specie.flavorTextEntries.first().flavorText
            }
        return PokemonReadModel(
            name = specie.name,
            description = description,
            habitat = specie.habitat?.name ?: "",
            isLegendary = specie.isLegendary
        )
    }
}
