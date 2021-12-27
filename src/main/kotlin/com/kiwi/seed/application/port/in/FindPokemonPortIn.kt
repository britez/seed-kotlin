package com.kiwi.seed.application.port.`in`

import com.kiwi.seed.domain.Pokemon

interface FindPokemonPortIn {

    fun execute(name:String):Pokemon

}
