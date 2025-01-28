package io.github.petretiandrea.infrastructure

import arrow.core.Either
import io.github.petretiandrea.common.ApiError
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.domain.pokemon.PokemonRepository
import io.github.petretiandrea.infrastructure.apis.pokemon.PokeApi

/**  */
class PokemonRepositoryApiAdapter(
    private val pokeApi: PokeApi,
    private val pokemonApiReadModelAdapter: PokemonApiReadModelAdapter
) : PokemonRepository {

    override suspend fun findPokemon(name: String): Either<FindPokemonError, PokemonReadModel> {
        return pokeApi
            .getPokemonSpecie(name)
            .map { specie -> pokemonApiReadModelAdapter.map(specie) }
            .mapLeft { error ->
                when (error) {
                    is ApiError.HttpError<*> ->
                        when (error.code) {
                            404 -> FindPokemonError.PokemonNotFound(name)
                            else -> FindPokemonError.UnexpectedError(error.reason)
                        }
                    else -> FindPokemonError.UnexpectedError(error.reason)
                }
            }
    }
}
