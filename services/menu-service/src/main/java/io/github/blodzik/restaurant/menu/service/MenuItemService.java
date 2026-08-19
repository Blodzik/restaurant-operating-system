package io.github.blodzik.restaurant.menu.service;

import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Transactional
    public MenuItem create(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public Optional<MenuItem> update(Long id, MenuItem updatedData) {
        Optional<MenuItem> result = menuItemRepository.findById(id);

        if(result.isPresent()) {
            MenuItem existing = result.get();

            existing.setName(updatedData.getName());
            existing.setActive(updatedData.isActive());
            existing.setTrackStock(updatedData.isTrackStock());
            existing.setDescription(updatedData.getDescription());
            existing.setDestination(updatedData.getDestination());
            existing.setCategoryId(updatedData.getCategoryId());
            existing.setStockCount(updatedData.getStockCount());
            existing.setBasePrice(updatedData.getBasePrice());
            existing.setModifiers(updatedData.getModifiers());

            MenuItem saved = menuItemRepository.save(existing);

            return Optional.of(saved);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        if(menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean decrementStock(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item with ID " + id + " does not exist"));

        if(!menuItem.isTrackStock()) {
            return true;
        }

        int updatedRows = menuItemRepository.decrementStock(id);

        return updatedRows > 0;
    }
}
