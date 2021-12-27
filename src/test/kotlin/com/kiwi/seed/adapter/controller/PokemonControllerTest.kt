package com.kiwi.seed.adapter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiwi.seed.adapter.rest.exception.NotFoundRestClientException
import com.kiwi.seed.application.usecase.FindPokemonUseCase
import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.config.TestConfig
import com.kiwi.seed.domain.Pokemon
import org.bouncycastle.asn1.cms.CMSAttributes.contentType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@DisplayName("Tests cases for pokemon controller")
@WebMvcTest(PokemonController::class)
@Import(TestConfig::class)
@AutoConfigureWebClient(registerRestTemplate = true)
class PokemonControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @MockBean
    private val findPokemonUseCase: FindPokemonUseCase? = null

    @Test
    @DisplayName("attempts to find pokemon with name")
    fun testFindPokemon() {
        val someName = "someName"
        val someType = "someType"
        val mockedPokemon = Pokemon(id = 1, name = someName, type = someType, abilities = emptyList())
        val mockedResponse = PokemonController.PokemonRest(name = someName, type = someType, emptyList())

        Mockito
            .`when`(findPokemonUseCase!!.execute(someName))
            .thenReturn(mockedPokemon)

        mockMvc!!.get("/pokemon/{name}", someName) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(objectMapper!!.writeValueAsString(mockedResponse)) }
        }

    }

    @Test
    @DisplayName("when name is invalid will return not found")
    fun testPokemonNotFound() {
        val invalidName = "invalidName"
        Mockito
            .`when`(findPokemonUseCase!!.execute(invalidName))
            .thenThrow(NotFoundRestClientException(ErrorCode.POKEMON_NOT_FOUND))

        mockMvc!!
            .get(url, invalidName) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON }
            .andExpect {
                status { isNotFound() }
            }

    }

    @Test
    @DisplayName("when unexpected error occurs an internal error will be retrieved")
    fun testInternalError() {
        val invalidName = "invalidName"
        Mockito
            .`when`(findPokemonUseCase!!.execute(invalidName))
            .thenThrow(RuntimeException())

        mockMvc!!
            .get(url, invalidName) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON }
            .andExpect {
                status { isInternalServerError() }
            }

    }

    companion object {
        const val url = "/pokemon/{name}"
        private fun <T> any(): T = Mockito.any<T>()

    }
}