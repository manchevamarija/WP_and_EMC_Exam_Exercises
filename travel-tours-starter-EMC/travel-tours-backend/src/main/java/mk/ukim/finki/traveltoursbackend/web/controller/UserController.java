package mk.ukim.finki.traveltoursbackend.web.controller;

import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.traveltoursbackend.service.application.UserApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<RegisterUserResponseDto> findByUsername(
        @PathVariable String username
    ) {
        return userApplicationService
            .findByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<RegisterUserResponseDto> me(
        @AuthenticationPrincipal User user
    ) {
        return userApplicationService
            .findByUsername(user.getUsername())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(
        @RequestBody RegisterUserRequestDto registerUserRequestDto
    ) {
        return userApplicationService
            .register(registerUserRequestDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(
        @RequestBody LoginUserRequestDto loginUserRequestDto
    ) {
        return userApplicationService
            .login(loginUserRequestDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest().build());
    }
}
