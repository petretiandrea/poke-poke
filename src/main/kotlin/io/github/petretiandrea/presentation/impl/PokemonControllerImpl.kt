package io.github.petretiandrea.presentation.impl

import io.github.petretiandrea.application.GetPokemon
import io.github.petretiandrea.application.GetPokemonTranslated
import io.github.petretiandrea.domain.pokemon.FindPokemonError
import io.github.petretiandrea.domain.pokemon.PokemonReadModel
import io.github.petretiandrea.presentation.PokemonController
import io.github.petretiandrea.presentation.PokemonDto
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonControllerImpl(
    private val getPokemonHandler: GetPokemon,
    private val getPokemonTranslatedHandler: GetPokemonTranslated,
) : PokemonController {

    override suspend fun getPokemon(pokemonName: String): ResponseEntity<PokemonDto> {

        return getPokemonHandler(pokemonName)
            .map { ResponseEntity.ok().body(it.toDto()) }
            .fold({ ResponseEntity.of(createProblemDetail(it)).build() }, { it })
    }

    override suspend fun getPokemonTranslated(pokemonName: String): ResponseEntity<PokemonDto> {
        return getPokemonTranslatedHandler(pokemonName)
            .map { ResponseEntity.ok().body(it.toDto()) }
            .fold({ ResponseEntity.of(createProblemDetail(it)).build() }, { it })
    }

    private fun createProblemDetail(error: FindPokemonError): ProblemDetail =
        when (error) {
            is FindPokemonError.PokemonNotFound ->
                ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND,
                    "Pokemon not found with name${error.name}"
                )
            is FindPokemonError.UnexpectedError ->
                ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error happens ${error.error.localizedMessage}"
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
