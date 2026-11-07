package br.com.sup08.unidade.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UnidadeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveCadastrarUnidadeEretornar201() throws Exception {
        // JSON que seria enviado pelo Swagger
        String json = "{\"nome\": \"Unidade de Teste\", \"sigla\": \"UT\"}";

        // Faz a requisição e verifica se o status foi 201 Created
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Unidade de Teste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sigla").value("UT"));
    }
}