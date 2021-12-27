package com.kiwi.seed.application.usecase

import com.kiwi.seed.application.port.out.PokemonRepository
import com.kiwi.seed.domain.Pokemon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@DisplayName("Test case for find pokemon use case")
@ExtendWith(MockitoExtension::class)
class FindPokemonUseCaseTests {

    private val mockedPokemonRepository: PokemonRepository = Mockito.mock(PokemonRepository::class.java)

    @Test
    @DisplayName("test find pokemon")
    fun testFindPokemon() {
        //given
        val someName = "someName"
        val mockedPokemon = Pokemon(1, someName, "someType", emptyList())

        Mockito
            .`when`(mockedPokemonRepository.findPokemonByName(someName))
            .thenReturn(mockedPokemon)

        //when
        val result = FindPokemonUseCase(mockedPokemonRepository).execute(someName)

        //then
        assertNotNull(result)
        assertEquals(result, mockedPokemon)
    }
}