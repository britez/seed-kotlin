package com.kiwi.seed.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@Component
@ConfigurationProperties(prefix = "com.kiwi.seed.pokemon")
class PokemonConfiguration {

    @get:NotBlank
    lateinit var baseURL:String

    @get:NotBlank
    lateinit var findByNameURL:String

    fun getFindByNamePath():String = "${baseURL}${findByNameURL}"


}