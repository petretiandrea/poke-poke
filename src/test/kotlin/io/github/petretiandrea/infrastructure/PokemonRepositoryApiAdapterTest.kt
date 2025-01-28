package io.github.petretiandrea.infrastructure

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.infrastructure.apis.pokemon.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class PokemonRepositoryApiAdapterTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var repository: PokemonRepositoryApiAdapter

    @BeforeEach
    fun setup() {
        pokeApi = mock()
        repository = PokemonRepositoryApiAdapter(pokeApi, PokemonApiReadModelAdapter())
    }

    @Test
    fun `findPokemon returns PokemonReadModel when API returns valid Pokemon`() = runTest {
        val name = "pikachu"
        val specie =
            PokemonResponse(
                name = name,
                flavorTextEntries = emptyList(),
                habitat = Habitat("forest", ""),
                isLegendary = false
            )
        whenever(pokeApi.getPokemonSpecie(name)).thenReturn(Either.Right(specie))

        val result = repository.findPokemon(name)

        assert(result.isRight())
        verify(pokeApi).getPokemonSpecie(name)
    }

    @Test
    fun `findPokemon returns PokemonNotFound when API returns 404 error`() = runTest {
        // Given
        val name = "unknown"
        whenever(pokeApi.getPokemonSpecie(name))
            .thenReturn(Either.Left(ApiError.HttpError(404, "Not Found", Error())))

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
        whenever(pokeApi.getPokemonSpecie(name))
            .thenReturn(Either.Left(ApiError.HttpError(500, "Internal Server Error", Error())))

        // When
        val result = repository.findPokemon(name)

        // Then
        verify(pokeApi).getPokemonSpecie(name)
        assert(result.isLeft())
        result.onLeft {
            assert(it is FindPokemonError.UnexpectedError)
        }
    }

    @Test
    fun `findPokemon returns UnexpectedError for non-HTTP errors`() = runTest {
        // Given
        val name = "charmander"
        whenever(pokeApi.getPokemonSpecie(name))
            .thenReturn(Either.Left(ApiError.UnexpectedError(Error("Network issue"))))

        // When
        val result = repository.findPokemon(name)

        // Then
        verify(pokeApi).getPokemonSpecie(name)
        assert(result.isLeft())
        result.onLeft {
            assert(it is FindPokemonError.UnexpectedError)
        }
    }
}
