package com.kiwi.seed.application.usecase

import com.kiwi.seed.application.port.`in`.FindPokemonPortIn
import com.kiwi.seed.application.port.out.PokemonRepository
import com.kiwi.seed.domain.Pokemon
import org.springframework.stereotype.Component

@Component
class FindPokemonUseCase(
    private val pokemonRepository: PokemonRepository): FindPokemonPortIn {

    override fun execute(name: String): Pokemon = pokemonRepository.findPokemonByName(name)
}