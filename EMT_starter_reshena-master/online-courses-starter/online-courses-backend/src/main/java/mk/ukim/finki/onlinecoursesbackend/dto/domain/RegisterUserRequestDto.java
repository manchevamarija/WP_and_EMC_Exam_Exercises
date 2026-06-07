package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.model.enumeration.Role;

public record RegisterUserRequestDto(
    String name,
    String surname,
    String email,
    String username,
    String password
) {
    public User toUser(Role role) {
        return new User(name, surname, email, username, password, role);
    }
}
