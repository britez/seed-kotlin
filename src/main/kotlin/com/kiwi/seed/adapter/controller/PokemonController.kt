package com.kiwi.seed.adapter.controller

import com.kiwi.seed.application.usecase.FindPokemonUseCase
import com.kiwi.seed.domain.Pokemon
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pokemon")
class PokemonController(
    private val findPokemonUseCase: FindPokemonUseCase
) {

    @GetMapping("/{name}")
    fun get(@PathVariable name:String):PokemonRest =
        PokemonRest.from(findPokemonUseCase.execute(name))

    data class PokemonRest(
        val name: String,
        val type: String,
        val abilities: List<AbilityRest>) {

        companion object {
            fun from(pokemon: Pokemon): PokemonRest =
                PokemonRest(
                    name = pokemon.name,
                    type = pokemon.type,
                    abilities = pokemon.abilities.map {
                        AbilityRest(name = it.name) })
        }
    }

    data class AbilityRest(
        val name: String) {
        val _self: Link = linkTo(AbilityController::class.java).slash(name).withSelfRel()
    }

}