package mk.ukim.finki.traveltoursbackend.service.application.impl;

import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.helper.JwtHelper;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.traveltoursbackend.model.enumeration.Role;
import mk.ukim.finki.traveltoursbackend.service.application.UserApplicationService;
import mk.ukim.finki.traveltoursbackend.service.domain.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;
    private final JwtHelper jwtHelper;

    public UserApplicationServiceImpl(UserService userService, JwtHelper jwtHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto) {
        User user = userService.register(registerUserRequestDto.toUser(Role.ROLE_CUSTOMER));
        RegisterUserResponseDto displayUserDto = RegisterUserResponseDto.from(user);
        return Optional.of(displayUserDto);
    }

    @Override
    public Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto) {
        User user = userService.login(loginUserRequestDto.username(), loginUserRequestDto.password());
        String token = jwtHelper.generateToken(user);
        return Optional.of(new LoginUserResponseDto(token));
    }

    @Override
    public Optional<RegisterUserResponseDto> findByUsername(String username) {
        return userService
            .findByUsername(username)
            .map(RegisterUserResponseDto::from);
    }
}

