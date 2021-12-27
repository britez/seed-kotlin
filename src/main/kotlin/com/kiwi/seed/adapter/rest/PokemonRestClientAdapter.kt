package com.kiwi.seed.adapter.rest

import com.kiwi.seed.adapter.rest.exception.NotFoundRestClientException
import com.kiwi.seed.adapter.rest.exception.TimeoutRestClientException
import com.kiwi.seed.adapter.rest.handler.RestTemplateErrorHandler
import com.kiwi.seed.application.port.out.PokemonRepository
import com.kiwi.seed.config.PokemonConfiguration
import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.domain.Ability
import com.kiwi.seed.domain.Pokemon
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PokemonRestClientAdapter(
    private val restTemplate:RestTemplate,
    private val pokemonConfiguration: PokemonConfiguration
):PokemonRepository {

    private val log = LoggerFactory.getLogger(PokemonRestClientAdapter::class.java)

    init {
        restTemplate.errorHandler = RestTemplateErrorHandler(mapOf(
            HttpStatus.NOT_FOUND to NotFoundRestClientException(ErrorCode.POKEMON_NOT_FOUND),
            HttpStatus.REQUEST_TIMEOUT to TimeoutRestClientException(ErrorCode.POKEMON_TIMEOUT)))
    }

    override fun findPokemonByName(name: String): Pokemon =
        log
            .info("Attempt to execute http request for pokemon $name")
            .let { restTemplate.getForObject(pokemonConfiguration.getFindByNamePath(), PokemonModel::class.java, name) }
            .also { log.info("Response for http request for pokemon $name {}", it) }
            .let { it?.toDomain() ?: throw NotFoundRestClientException(ErrorCode.POKEMON_NOT_FOUND) }

    data class PokemonModel(
        val id:Long,
        val name:String,
        val abilities:List<AbilitiesModel>,
        val types:List<TypesModel>) {
        fun toDomain():Pokemon =
            Pokemon(
                id = id,
                name = name,
                type = types.firstOrNull()?.type?.name ?: "",
                abilities = abilities.map { Ability(name = it.ability.name, id = 0) })
    }

    data class AbilitiesModel(val ability:AbilityModel)
    data class AbilityModel(val name:String)
    data class TypesModel(val type:TypeModel)
    data class TypeModel(val name:String)
}