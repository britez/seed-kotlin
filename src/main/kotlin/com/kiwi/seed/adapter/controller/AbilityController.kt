package com.kiwi.seed.adapter.controller

import com.kiwi.seed.application.usecase.FindPokemonUseCase
import com.kiwi.seed.domain.Pokemon
import org.springframework.hateoas.Link
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/abilities")
class AbilityController(
    private val findPokemonUseCase: FindPokemonUseCase
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id:Long):String =
        "temp"

}