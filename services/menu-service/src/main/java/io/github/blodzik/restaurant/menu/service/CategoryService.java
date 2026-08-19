package io.github.blodzik.restaurant.menu.service;

import io.github.blodzik.restaurant.menu.entity.Category;
import io.github.blodzik.restaurant.menu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Optional<Category> update(Long id,Category updateData) {
        Optional<Category> result = categoryRepository.findById(id);

        if (result.isPresent()) {

            Category existing = result.get();

            existing.setName(updateData.getName());
            existing.setDisplayOrder(updateData.getDisplayOrder());
            existing.setActive(updateData.isActive());

            Category saved = categoryRepository.save(existing);

            return Optional.of(saved);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        if(categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
