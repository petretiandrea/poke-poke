package io.github.petretiandrea.presentation

import io.github.petretiandrea.application.GetPokemon
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.Pokemon
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


class PokemonController(
    private val getPokemonHandler: GetPokemon
) {

    @GetMapping("/{pokemonName}")
    suspend fun getPokemon(
        @PathVariable pokemonName: String
    ) : ResponseEntity<Pokemon> {
        return getPokemonHandler(pokemonName)
            .fold(
                ifLeft = {
                    when(it) {
                        is FindPokemonError.PokemonNotFound -> ResponseEntity.notFound().build()
                        is FindPokemonError.UnexpectedError -> ResponseEntity.internalServerError().build()
                    }
                 },
                ifRight = { pokemon ->
                    ResponseEntity.ok().body(pokemon)
                }
            )
    }


}