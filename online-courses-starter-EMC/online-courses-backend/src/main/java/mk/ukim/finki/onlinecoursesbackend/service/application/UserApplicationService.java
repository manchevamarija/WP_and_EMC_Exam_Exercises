package mk.ukim.finki.onlinecoursesbackend.service.application;

import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.RegisterUserResponseDto;

public interface UserApplicationService {
    Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<RegisterUserResponseDto> findByUsername(String username);
}
