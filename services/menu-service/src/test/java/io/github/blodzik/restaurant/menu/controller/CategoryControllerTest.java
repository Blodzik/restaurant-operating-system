package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Category;
import io.github.blodzik.restaurant.menu.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() throws Exception {
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Starters");
        cat.setActive(true);

        Mockito.when(categoryService.findAll()).thenReturn(List.of(cat));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Starters"));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        String badRequestJson = """
                {
                    "displayOrder": 1,
                    "active": true
                }
                """;

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        Mockito.when(categoryService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());
    }
}
