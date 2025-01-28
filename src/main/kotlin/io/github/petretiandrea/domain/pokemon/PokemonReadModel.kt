package io.github.petretiandrea.domain.pokemon

data class PokemonReadModel(
    val name: String,
    val description: String,
    val habitat: String,
    val isLegendary: Boolean
)
