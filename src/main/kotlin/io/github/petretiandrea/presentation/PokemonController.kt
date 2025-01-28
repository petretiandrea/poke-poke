package io.github.petretiandrea.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/pokemon")
interface PokemonController {

    @GetMapping("/{pokemonName}")
    suspend fun getPokemon(@PathVariable pokemonName: String): ResponseEntity<PokemonDto>

    @GetMapping("/{pokemonName}/translated")
    suspend fun getPokemonTranslated(@PathVariable pokemonName: String): ResponseEntity<PokemonDto>
}
