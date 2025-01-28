package io.github.petretiandrea.infrastructure

import io.github.petretiandrea.infrastructure.apis.pokemon.FlavorText
import io.github.petretiandrea.infrastructure.apis.pokemon.Habitat
import io.github.petretiandrea.infrastructure.apis.pokemon.Language
import io.github.petretiandrea.infrastructure.apis.pokemon.PokemonResponse
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// Lot of decision about description flavor selection
class PokemonApiReadModelAdapterTest {
    private val adapter = PokemonApiReadModelAdapter()

    private fun generateFlavorTexts(vararg languages: String): List<FlavorText> {
        return languages.map { lang -> FlavorText(lang, Language(lang)) }
    }

    @Test
    fun `should select English flavor text`() {
        // Given
        val response = PokemonResponse(
            name = "charizard",
            flavorTextEntries = generateFlavorTexts("en", "fr"),
            habitat = Habitat(name = "mountain", ""),
            isLegendary = false
        )

        // When
        val result = adapter.map(response)

        // Then
        assertEquals(response.name, result.name)
        assertEquals("en", result.description)
        assertEquals("mountain", result.habitat)
        assertEquals(false, result.isLegendary)
    }

    @Test
    fun `should use the first flavor text if no English entry exists`() {
        // Given
        val response = PokemonResponse(
            name = "mew",
            flavorTextEntries = generateFlavorTexts("it", "fr"),
            habitat = Habitat(name = "mountain", ""),
            isLegendary = true
        )

        // When
        val result = adapter.map(response)

        // Then
        assertEquals("mew", result.name)
        assertEquals("it", result.description)
        assertEquals("forest", result.habitat)
        assertEquals(true, result.isLegendary)
    }

    @Test
    fun `should return empty description if flavorTextEntries is empty`() {
        // Given
        val response = PokemonResponse(
            name = "bulbasaur",
            flavorTextEntries = emptyList(),
            habitat = Habitat(name = "grassland", ""),
            isLegendary = false
        )

        // When
        val result = adapter.map(response)

        // Then
        assertEquals("bulbasaur", result.name)
        assertEquals("", result.description)
        assertEquals("grassland", result.habitat)
        assertEquals(false, result.isLegendary)
    }
}