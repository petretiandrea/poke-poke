package io.github.petretiandrea.domain.pokemon

import arrow.core.Either

interface PokemonRepository {
    suspend fun findPokemon(name: String): Either<FindPokemonError, PokemonReadModel>
}

// can be extended to more explicit error cases if needed
sealed interface FindPokemonError {
    data class PokemonNotFound(val name: String) : FindPokemonError

    data class UnexpectedError(val error: Throwable) : FindPokemonError
}
