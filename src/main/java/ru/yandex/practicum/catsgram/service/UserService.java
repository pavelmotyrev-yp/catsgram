package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.InternalServerErrorException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public User post(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        checkEmail(user.getEmail());

        Long id = generateId();
        user.setId(id);
        user.setRegistrationDate(Instant.now());
        users.put(id, user);
        return user;
    }

    public User put(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        User user = users.getOrDefault(newUser.getId(), null);

        if (user == null) {
            throw new ConditionsNotMetException("Пользователь не найден");
        }

        checkEmail(newUser.getEmail());

        if (newUser.getPassword() != null) {
            user.setPassword(newUser.getPassword());
        }
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            user.setUsername(newUser.getUsername());
        }
        return user;
    }

    public boolean isExist(Long id) {
        User user = users.getOrDefault(id, null);
        return user != null;
    }

    public User getUser(Long userId) {
        throw new InternalServerErrorException("Not allowed yet");
    }

    private void checkEmail(String newEmail) {
        boolean isEmailExist = isEmailExist(newEmail);
        if (!isEmailExist) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
    }

    private boolean isEmailExist(String newEmail) {
        Optional<String> optionalEmail = users.values().stream()
                .map(User::getEmail)
                .filter(email -> email.equals(newEmail))
                .findAny();
        return optionalEmail.isEmpty();
    }

    private Long generateId() {
        long id = users.keySet().stream()
                .mapToLong(currentId -> currentId)
                .max()
                .orElse(0);
        return ++id;
    }
}
