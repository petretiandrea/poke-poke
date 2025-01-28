package io.github.petretiandrea.domain.pokemon

import arrow.core.Either
import io.github.petretiandrea.domain.translation.TranslationType
import io.github.petretiandrea.domain.translation.Translator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class PokemonTranslatorTest {

    private lateinit var translator: Translator
    private lateinit var pokemonTranslator: PokemonTranslator

    @BeforeEach
    fun setUp() {
        translator = mock()
        pokemonTranslator = PokemonTranslator(translator)
    }

    @Test
    fun `translateDescription should use YODA translation for cave habitat`() = runBlocking {
        // Given
        val pokemon = PokemonReadModel(
            name = "Zubat",
            habitat = PokemonTranslator.HABITAT_CAVE,
            isLegendary = false,
            description = "A cave-dwelling bat Pokémon."
        )

        whenever(translator.translate(any(), any())).thenReturn(Either.Right("yoda_description"))

        // When
        val result = pokemonTranslator.translateDescription(pokemon)

        // Then
        assertEquals("yoda_description", result.description)
        val translationType = argumentCaptor<TranslationType> {
            verify(translator, times(1)).translate(capture(), any())
        }
        assertEquals(TranslationType.YODA, translationType.lastValue)
    }

    @Test
    fun `translateDescription should use YODA translation for legendary pokemon`() = runBlocking {
        // Given
        val pokemon = PokemonReadModel(
            name = "Mewtwo",
            habitat = "any_habitat",
            isLegendary = true,
            description = "Legendary pokemon"
        )

        whenever(translator.translate(any(), any())).thenReturn(Either.Right("yoda_description"))

        // When
        val result = pokemonTranslator.translateDescription(pokemon)

        // Then
        assertEquals("yoda_description", result.description)
        val translationType = argumentCaptor<TranslationType> {
            verify(translator, times(1)).translate(capture(), any())
        }
        assertEquals(TranslationType.YODA, translationType.lastValue)
    }

    @Test
    fun `translateDescription should use SHAKESPEARE translation for other pokemons`() = runBlocking {
        // Given
        val pokemon = PokemonReadModel(
            name = "Bulbasaur",
            habitat = "any_habitat",
            isLegendary = false,
            description = "Starter pokemon"
        )

        whenever(translator.translate(any(), any())).thenReturn(Either.Right("shakespeare_description"))

        // When
        val result = pokemonTranslator.translateDescription(pokemon)

        // Then
        assertEquals("shakespeare_description", result.description)
        val translationType = argumentCaptor<TranslationType> {
            verify(translator, times(1)).translate(capture(), any())
        }
        assertEquals(TranslationType.SHAKESPEARE, translationType.lastValue)
    }

    @Test
    fun `should fallback to default description if translator fails`(): Unit = runBlocking {
        // Given
        val initialDescription = "Starter pokemon"
        val pokemon = PokemonReadModel(
            name = "Bulbasaur",
            habitat = "any_habitat",
            isLegendary = false,
            description = initialDescription
        )

        whenever(translator.translate(any(), any())).thenReturn(Either.Left(Error("Any error")))

        // When
        val result = pokemonTranslator.translateDescription(pokemon)

        // Then
        assertEquals(initialDescription, result.description)
        verify(translator, times(1)).translate(any(), any())
    }
}