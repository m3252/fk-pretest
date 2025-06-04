package com.frankit.productapi.ui;

import com.frankit.productapi.application.UserService;
import com.frankit.productapi.domain.User;
import com.frankit.productapi.global.support.api.ApiResponse;
import com.frankit.productapi.model.LoginCommand;
import com.frankit.productapi.model.SignupCommand;
import com.frankit.productapi.model.UserUpdateCommand;
import com.frankit.productapi.ui.dto.LoginRequest;
import com.frankit.productapi.ui.dto.SignupRequest;
import com.frankit.productapi.ui.dto.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody SignupRequest request) {
        SignupCommand signupCommand = new SignupCommand(request.name(), request.email(), request.password());
        String token = userService.signup(signupCommand);
        return ApiResponse.success(token);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        LoginCommand loginCommand = new LoginCommand(request.email(), request.password());
        String token = userService.login(loginCommand);
        return ApiResponse.success(token);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        User user = userService.findUserById(userId);

        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail());
        return ApiResponse.success(userResponse);
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateMe(Authentication authentication, @RequestBody SignupRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        UserUpdateCommand command = new UserUpdateCommand(request.name(), request.email(), request.password());

        User updatedUser = userService.updateUser(userId, command);
        UserResponse userResponse = new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
        return ApiResponse.success(userResponse);
    }

    @DeleteMapping("/me")
    public void deleteMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.markUserAsDeleted(userId);
    }
}
