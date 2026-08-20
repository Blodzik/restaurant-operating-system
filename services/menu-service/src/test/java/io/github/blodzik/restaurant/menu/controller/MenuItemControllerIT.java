package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Category;
import io.github.blodzik.restaurant.menu.entity.Destination;
import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.repository.CategoryRepository;
import io.github.blodzik.restaurant.menu.repository.MenuItemRepository;
import io.github.blodzik.restaurant.menu.service.MenuItemService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class MenuItemControllerIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.11");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateAndFetchMenuItemFromRealDatabase() throws Exception {
        String categoryJson = """
        {
            "name": "Mains",
            "displayOrder": 10,
            "active": true
        }
        """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andDo(print())
                .andExpect(status().isOk());

        String requestJson = """
                {
                    "categoryId": 1,
                    "name": "Duck Breast",
                    "basePrice": 69.00,
                    "active": true,
                    "destination": "KITCHEN",
                    "trackStock": false
                }
                """;

        mockMvc.perform(post("/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Duck Breast"));

    }

    @Test
    void shouldPreventDoubleDecrementWhenTwoWaitersOrderAtTheSameTime() throws InterruptedException {
        Category category = new Category();
        category.setName("Specials");
        categoryRepository.save(category);

        MenuItem item = new MenuItem();
        item.setName("Truffle Burger");
        item.setCategoryId(category.getId());
        item.setActive(true);
        item.setDestination(Destination.KITCHEN);
        item.setTrackStock(true);
        item.setStockCount(1);
        item.setBasePrice(java.math.BigDecimal.valueOf(25.00));

        item = menuItemRepository.save(item);
        Long itemId = item.getId();

        int numberOfWaiters = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfWaiters);

        CountDownLatch startingGun = new CountDownLatch(1);
        CountDownLatch finishLine = new CountDownLatch(numberOfWaiters);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        Runnable waiterAction = () -> {
            try {
                startingGun.await();

                boolean success = menuItemService.decrementStock(itemId);

                if(success) {
                    successfulOrders.incrementAndGet();
                } else {
                    failedOrders.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLine.countDown();
            }
        };

        executor.submit(waiterAction);
        executor.submit(waiterAction);

        startingGun.countDown();

        finishLine.await();

        assertEquals(1, successfulOrders.get(), "Only one waiter should get the burger");
        assertEquals(1, failedOrders.get(), "The other waiter should fail (Conflict)");

        MenuItem finalItem = menuItemRepository.findById(itemId).orElseThrow();
        assertEquals(0, finalItem.getStockCount(), "Stock should be exactly 0, not -1");
    }
}
