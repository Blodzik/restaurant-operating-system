package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import io.github.blodzik.restaurant.menu.repository.ModifierRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModifierController.class)
public class ModifierControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ModifierRepository modifierRepository;

    @Test
    void shouldReturnAllModifiers() throws Exception {
        Modifier modifier = new Modifier();
        modifier.setId(1L);
        modifier.setName("Cheese");
        modifier.setPriceDelta(BigDecimal.TWO);

        Mockito.when(modifierRepository.findAll()).thenReturn(List.of(modifier));

        mockMvc.perform(get("/modifiers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cheese"));
    }

}
