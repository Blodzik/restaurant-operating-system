package io.github.blodzik.restaurant.floor.controller;

import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import io.github.blodzik.restaurant.floor.entity.TableState;
import io.github.blodzik.restaurant.floor.repository.RestaurantTableRepository;
import io.github.blodzik.restaurant.floor.service.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class RestaurantTableControllerIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private RestaurantTableService tableService;

    @BeforeEach
    void setUp() {
        tableRepository.deleteAll();
    }

    @Test
    void seatingFreeTable_returns200Ok() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setLabel("T1");
        table.setCapacity(4);
        table.setTableState(TableState.FREE);
        table = tableRepository.save(table);

        mockMvc.perform(post("/tables/" + table.getId() + "/seat"))
                .andExpect(status().isOk());
    }

    @Test
    void concurrentSeatingAttempts_onlyOneSucceeds() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setLabel("T2");
        table.setCapacity(4);
        table.setTableState(TableState.FREE);
        Long tableId = tableRepository.save(table).getId();

        ExecutorService pool = Executors.newFixedThreadPool(2);

        var results = pool.invokeAll(List.of(
                () -> { try { tableService.seat(tableId); return true; } catch (Exception e) { return false; } },
                () -> { try { tableService.seat(tableId); return true; } catch (Exception e) { return false; } }
        ));

        long successCount = results.stream()
                .filter(f -> { try { return (boolean) f.get(); } catch (Exception e) { return false; } })
                .count();

        assertEquals(1, successCount, "Only one waiter should be able to seat the table");
    }
}
