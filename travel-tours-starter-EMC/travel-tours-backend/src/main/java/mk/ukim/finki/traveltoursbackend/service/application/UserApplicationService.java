package mk.ukim.finki.traveltoursbackend.service.application;

import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserResponseDto;

public interface UserApplicationService {
    Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<RegisterUserResponseDto> findByUsername(String username);
}
