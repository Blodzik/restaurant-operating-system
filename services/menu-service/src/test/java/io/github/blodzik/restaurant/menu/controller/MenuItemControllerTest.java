package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Destination;
import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
public class MenuItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuItemRepository menuItemRepository;

    @Test
    void shouldReturnAllMenuItems() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setCategoryId(1L);
        menuItem.setName("Duck Breast");
        menuItem.setActive(true);
        menuItem.setDestination(Destination.KITCHEN);
        menuItem.setTrackStock(false);

        Mockito.when(menuItemRepository.findAll()).thenReturn(List.of(menuItem));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Duck Breast"));
    }
}
