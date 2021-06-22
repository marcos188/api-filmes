package com.api.filmes;

import com.api.filmes.models.Filme;
import com.api.filmes.repository.FilmeRepository;
import com.api.filmes.resources.FilmeResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(FilmeResource.class)
public class FilmeTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FilmeRepository filmeRepository;

    Filme FILME_1 = new Filme(1L, "filme 1", "sinopse", "1992", "Livre", "Ação");
    Filme FILME_2 = new Filme(2L, "filme 2", "sinopse", "1998", "+10", "Drama");

    @Test
    public void DeveListarFilmes() throws Exception {
        List<Filme> filmes = new ArrayList<>(Arrays.asList(FILME_1, FILME_2));

        Mockito.when(filmeRepository.findAll()).thenReturn(filmes);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/filmes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome", Matchers.is("filme 2")));
    }

    @Test
    public void DeveCriarFilmes() throws Exception {
        Filme filme = Filme.builder()
                .nome("filme 1")
                .sinopse("sinopse")
                .ano("1992")
                .classificacaoIndicativa("Livre")
                .genero("Ação")
                .build();

        Mockito.when(filmeRepository.save(filme)).thenReturn(filme);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/filmes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(filme));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("filme 1")));
    }

    @Test
    public void DeveAtualizarFilme() throws Exception {
        Filme atualizacaoFilme = Filme.builder()
                .nome("filme atualizado")
                .sinopse("sinopse")
                .ano("1992")
                .classificacaoIndicativa("Livre")
                .genero("Ação")
                .build();

        Filme filmeAtualizado = Filme.builder()
                .id(1L)
                .nome("filme atualizado")
                .sinopse("sinopse")
                .ano("1992")
                .classificacaoIndicativa("Livre")
                .genero("Ação")
                .build();

        Mockito.when(filmeRepository.findById(FILME_1.getId())).thenReturn(FILME_1);
        Mockito.when(filmeRepository.save(filmeAtualizado)).thenReturn(filmeAtualizado);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/filmes/" + FILME_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(atualizacaoFilme));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("filme atualizado")));
    }

    @Test
    public void DeveListarFilmeUnico() throws Exception {

        Mockito.when(filmeRepository.findById(FILME_1.getId())).thenReturn(FILME_1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/filmes/" + FILME_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("filme 1")));
    }

    @Test
    public void DeveRemoverFilmeUnico() throws Exception {
        Mockito.when(filmeRepository.findById(FILME_1.getId())).thenReturn(FILME_1);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/filmes/" + FILME_1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
