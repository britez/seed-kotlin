package com.kiwi.seed.adapter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiwi.seed.application.usecase.FindPokemonUseCase
import com.kiwi.seed.config.TestConfig
import com.kiwi.seed.domain.Pokemon
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
            .`when`(findPokemonUseCase!!.execute("someName"))
            .thenReturn(mockedPokemon)

        mockMvc!!
            .get(url) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON }
            .andExpect { MockMvcResultMatchers.status().isOk }
            .andExpect { MockMvcResultMatchers.content().json(objectMapper!!.writeValueAsString(mockedResponse)) }

    }

    companion object {
        const val url = "/pokemon"
        private fun <T> any(): T = Mockito.any<T>()

    }
}