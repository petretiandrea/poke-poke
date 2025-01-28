package io.github.petretiandrea.presentation

import io.github.petretiandrea.application.GetPokemon
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonControllerImpl(
    private val getPokemonHandler: GetPokemon,
) : PokemonController {

    override suspend fun getPokemon(pokemonName: String): ResponseEntity<PokemonDto> {
        return getPokemonHandler(pokemonName)
            .map { ResponseEntity.ok().body(it.toDto()) }
            .fold(
                {
                    when (it) {
                        is FindPokemonError.PokemonNotFound -> ResponseEntity.notFound().build()
                        is FindPokemonError.UnexpectedError ->
                            ResponseEntity.internalServerError().build()
                    }
                },
                { it }
            )
    }

    private fun PokemonReadModel.toDto(): PokemonDto =
        PokemonDto(
            name = name,
            description = description,
            habitat = habitat,
            isLegendary = isLegendary
        )
}
