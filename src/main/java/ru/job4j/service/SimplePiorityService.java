package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.entity.Priority;
import ru.job4j.repository.PriorityRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePiorityService implements PriorityService {

    private final PriorityRepository priorityRepository;

    @Override
    public Optional<Priority> findById(int id) {
        return priorityRepository.findById(id);
    }

    @Override
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
