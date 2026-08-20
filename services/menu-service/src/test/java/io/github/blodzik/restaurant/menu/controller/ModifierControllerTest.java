package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import io.github.blodzik.restaurant.menu.service.ModifierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModifierController.class)
public class ModifierControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ModifierService modifierService;

    @Test
    void shouldReturnAllModifiers() throws Exception {
        Modifier modifier = new Modifier();
        modifier.setId(1L);
        modifier.setName("Cheese");
        modifier.setPriceDelta(BigDecimal.TWO);

        Mockito.when(modifierService.findAll()).thenReturn(List.of(modifier));

        mockMvc.perform(get("/modifiers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cheese"));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        String badRequestJson = """
        {
            "priceDelta": -5.50
        }
        """;

        mockMvc.perform(post("/modifiers")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenModifierNotFound() throws Exception {
        Mockito.when(modifierService.findById(999L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/modifiers/999"))
                .andExpect(status().isNotFound());
    }

}
