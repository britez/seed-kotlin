package com.kiwi.seed.adapter.rest

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kiwi.seed.adapter.rest.PokemonRestClientAdapter.*
import com.kiwi.seed.adapter.rest.exception.NotFoundRestClientException
import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.config.PokemonConfiguration
import com.kiwi.seed.config.TestConfig
import com.kiwi.seed.domain.Ability
import com.kiwi.seed.domain.Pokemon
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.*


@DisplayName("PokemonRestClient Adapter Test")
@Import(value = [TestConfig::class])
@EnableConfigurationProperties(value = [PokemonConfiguration::class])
@RestClientTest(value = [PokemonRestClientAdapter::class])
class PokemonRestClientAdapterTests {

    @Autowired
    private val client: PokemonRestClientAdapter? = null

    @Autowired
    private val server: MockRestServiceServer? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a EmptyOrNullBodyRestClientException")
    fun tesGetPokemonEmptyOrNullBodyRestClientException() {

        //given
        server!!.expect(requestTo(EXPECTED_URI)).andRespond(withNoContent())

        //when
        val thrown: Throwable = catchThrowable { client?.findPokemonByName("pikachu") }

        //then
        assertThat(thrown)
            .isInstanceOf(NotFoundRestClientException::class.java)
            .hasMessage(ErrorCode.POKEMON_NOT_FOUND.reason)
    }

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a NotFoundRestClientException")
    fun testGetPokemonNotFoundRestClientException() {

        //given
        server!!.expect(requestTo(EXPECTED_URI)).andRespond(withStatus(HttpStatus.NOT_FOUND))

        //when
        val thrown = catchThrowable { client?.findPokemonByName("pikachu") }

        //then
        assertThat(thrown)
            .isInstanceOf(NotFoundRestClientException::class.java)
            .hasMessage(ErrorCode.POKEMON_NOT_FOUND.reason)
    }

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a Pokemon domain object")
    @Throws(
        JsonProcessingException::class
    )
    fun testGetPokemonNormalCase() {

        //given
        val expectedResponse = getExpectedDomainForMockedResponse()
        val detailsString = objectMapper!!.writeValueAsString(getMockedResponse())
        server!!.expect(requestTo(EXPECTED_URI))
            .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON))

        //when
        val currentResponse: Pokemon = client!!.findPokemonByName("pikachu")

        //then
        assertThat(currentResponse).isEqualTo(expectedResponse)
    }

    private fun getMockedResponse(): PokemonModel = PokemonModel(
            id = 1L,
            name = "pikachu",
            types = listOf(TypesModel(type = TypeModel("normal"))),
            abilities = listOf(AbilitiesModel(AbilityModel("thunder")))
    )

    private fun getExpectedDomainForMockedResponse(): Pokemon =
        Pokemon(
            id = 1L,
            name = "pikachu",
            type = "normal",
            abilities = listOf(Ability(name = "thunder", id = 0)))

    companion object {
        private const val EXPECTED_URI = "https://pokeapi.co/api/v2/pokemon/pikachu"
    }

}