package io.github.blodzik.restaurant.menu.service;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import io.github.blodzik.restaurant.menu.repository.ModifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifierService {
    private final ModifierRepository modifierRepository;

    public List<Modifier> findAll() {
        return modifierRepository.findAll();
    }

    public Optional<Modifier> findById(Long id) {
        return modifierRepository.findById(id);
    }

    @Transactional
    public Modifier create(Modifier modifier) {
        return modifierRepository.save(modifier);
    }

    @Transactional
    public Optional<Modifier> update(Long id, Modifier updatedData) {
        Optional<Modifier> result = modifierRepository.findById(id);

        if(result.isPresent()) {
            Modifier existing = result.get();

            existing.setName(updatedData.getName());
            existing.setPriceDelta(updatedData.getPriceDelta());

            Modifier saved = modifierRepository.save(existing);

            return Optional.of(saved);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        if(modifierRepository.existsById(id)) {
            modifierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
