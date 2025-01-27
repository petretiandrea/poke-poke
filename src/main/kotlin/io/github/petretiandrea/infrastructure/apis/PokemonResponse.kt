package io.github.petretiandrea.infrastructure.apis

data class PokemonResponse(
    val name: String,
    val flavorTextEntries: List<FlavorText>,
    val habitat: String?,
    val isLegendary: Boolean
)

data class FlavorText(
    val flavorText: String,
    val language: Language
)

data class Language(
    val name: String
)