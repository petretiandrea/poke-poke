package io.github.petretiandrea.application

import arrow.core.Either
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.domain.pokemon.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class GetPokemonTest {

    private val pokemonRepository = mock(PokemonRepository::class.java)
    private val getPokemon = GetPokemon(pokemonRepository)

    @Test
    fun `should return PokemonReadModel when repository finds pokemon`(): Unit = runBlocking {
        val pokemonName = "Pikachu"
        val expectedPokemon =
            PokemonReadModel(
                name = "Pikachu",
                description = "An electric-type Pokémon.",
                habitat = "Forest",
                isLegendary = false
            )

        `when`(pokemonRepository.findPokemon(pokemonName)).thenReturn(Either.Right(expectedPokemon))

        val result = getPokemon(pokemonName)

        assertTrue(result.isRight())
        assertEquals(expectedPokemon, result.getOrNull())
        verify(pokemonRepository).findPokemon(pokemonName)
    }

    @Test
    fun `should return FindPokemonError when repository fails to find pokemon`(): Unit =
        runBlocking {
            val pokemonName = "NonExistentPokemon"
            val expectedError = FindPokemonError.PokemonNotFound(pokemonName)

            `when`(pokemonRepository.findPokemon(pokemonName))
                .thenReturn(Either.Left(expectedError))

            val result = getPokemon(pokemonName)

            assertTrue(result.isLeft())
            assertEquals(expectedError, result.swap().getOrNull())
            verify(pokemonRepository).findPokemon(pokemonName)
        }
}
