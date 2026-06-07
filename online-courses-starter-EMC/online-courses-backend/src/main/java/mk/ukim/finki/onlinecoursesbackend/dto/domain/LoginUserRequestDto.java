package mk.ukim.finki.onlinecoursesbackend.dto.domain;

public record LoginUserRequestDto(
    String username,
    String password
) {
}
