package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.ZoneDto;
import ru.job4j.entity.User;
import ru.job4j.repository.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {

        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public Collection<ZoneDto> getAllZones() {
        List<TimeZone> zones = new ArrayList<>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }

        return zones.stream()
                .map(zone -> new ZoneDto(zone.getID(),
                        zone.getID() + " : " + zone.getDisplayName()))
                .toList();
    }

}
