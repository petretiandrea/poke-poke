package io.github.petretiandrea.infrastructure.apis.pokemon

data class PokemonResponse(
    val name: String,
    val flavorTextEntries: List<FlavorText>,
    val habitat: Habitat?,
    val isLegendary: Boolean
)

data class FlavorText(val flavorText: String, val language: Language)

data class Language(val name: String)

data class Habitat(val name: String, val url: String)
