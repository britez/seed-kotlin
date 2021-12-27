package com.kiwi.seed.application.port.out

import com.kiwi.seed.domain.Pokemon

interface PokemonRepository {

    fun findPokemonByName(name:String): Pokemon
}