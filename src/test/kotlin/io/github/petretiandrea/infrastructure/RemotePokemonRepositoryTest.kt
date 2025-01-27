package io.github.petretiandrea.infrastructure

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.infrastructure.apis.FlavorText
import io.github.petretiandrea.infrastructure.apis.PokeApi
import io.github.petretiandrea.infrastructure.apis.PokemonResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class RemotePokemonRepositoryTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var repository: RemotePokemonRepository

    @BeforeEach
    fun setup() {
        pokeApi = mock()
        repository = RemotePokemonRepository(pokeApi)
    }

    @Test
    fun `findPokemon returns Pokemon when API returns valid Specie`() = runTest {
        // Given
        val name = "pikachu"
        val specie = PokemonResponse(
            name = name,
            flavorTextEntries = listOf(FlavorText("Electric type Pokemon")),
            habitat = "forest",
            isLegendary = false
        )
        whenever(pokeApi.getPokemonSpecie(name)).thenReturn(Either.Right(specie))

        // When
        val result = repository.findPokemon(name)

        // Then
        assert(result.isRight())
        result.onRight {
            assertEquals(name, it.name)
            assertEquals("Electric type Pokemon", it.description)
            assertEquals("forest", it.habitat)
            assertEquals(false, it.isLegendary)
        }
        verify(pokeApi).getPokemonSpecie(name)
    }

    @Test
    fun `findPokemon returns PokemonNotFound when API returns 404 error`() = runTest {
        // Given
        val name = "unknown"
        whenever(pokeApi.getPokemonSpecie(name)).thenReturn(
            Either.Left(ApiError.HttpError(404, "Not Found", Error()))
        )

        // When
        val result = repository.findPokemon(name)

        // Then
        verify(pokeApi).getPokemonSpecie(name)

        assert(result.isLeft())

        val error = (result as Either.Left).value
        assert(error is FindPokemonError.PokemonNotFound)
        assertEquals(name, (error as FindPokemonError.PokemonNotFound).name)
    }

    @Test
    fun `findPokemon returns UnexpectedError for other HTTP errors`() = runTest {
        // Given
        val name = "bulbasaur"
        whenever(pokeApi.getPokemonSpecie(name)).thenReturn(
            Either.Left(ApiError.HttpError(500, "Internal Server Error", Error()))
        )

        // When
        val result = repository.findPokemon(name)

        // Then
        verify(pokeApi).getPokemonSpecie(name)
        assert(result.isLeft())
        result.onLeft {
            assert(it is FindPokemonError.UnexpectedError)
            assertEquals("Internal Server Error", (it as FindPokemonError.UnexpectedError).error)
        }
    }

    @Test
    fun `findPokemon returns UnexpectedError for non-HTTP errors`() = runTest {
        // Given
        val name = "charmander"
        whenever(pokeApi.getPokemonSpecie(name)).thenReturn(
            Either.Left(ApiError.UnexpectedError(Error("Network issue")))
        )

        // When
        val result = repository.findPokemon(name)

        // Then
        verify(pokeApi).getPokemonSpecie(name)
        assert(result.isLeft())
        val error = (result as Either.Left).value
        assert(error is FindPokemonError.UnexpectedError)
        assertEquals("Network issue", (error as FindPokemonError.UnexpectedError).error)
    }
}