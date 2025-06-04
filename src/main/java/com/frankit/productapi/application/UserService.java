package com.frankit.productapi.application;


import com.frankit.productapi.domain.User;
import com.frankit.productapi.domain.UserRepository;
import com.frankit.productapi.global.support.JwtTokenValidator;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import com.frankit.productapi.model.LoginCommand;
import com.frankit.productapi.model.SignupCommand;
import com.frankit.productapi.model.UserUpdateCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtTokenValidator jwtTokenValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenValidator = jwtTokenValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public String signup(SignupCommand command) {
        User foundUser = userRepository.findByEmail(command.email());
        if (foundUser != null) {
            throw new CustomException(ErrorType.USER_ALREADY_EXISTS_ERROR);
        }

        User user = new User(command.name(), command.email(), passwordEncoder.encode(command.password()));
        user.loggedIn();
        userRepository.save(user);

        return jwtTokenValidator.generateToken(user.getId());
    }

    public String login(LoginCommand command) {
        User user = userRepository.findByEmail(command.email());

        // 유저 로그인 정보가 일치하지 않나요?
        if (user == null || !passwordEncoder.matches(command.password(), user.getPassword()) || user.isDeleted()) {
            throw new CustomException(ErrorType.USER_NOT_FOUND_ERROR);
        }

        user.loggedIn();
        userRepository.save(user);

        return jwtTokenValidator.generateToken(user.getId());
    }

    public User findUserById(Long id) {
        return getActiveUserById(id);
    }

    public User updateUser(Long userId, UserUpdateCommand command) {
        User user = getActiveUserById(userId);
        User existingUser = userRepository.findByEmail(command.email());

        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new CustomException(ErrorType.USER_ALREADY_EXISTS_ERROR);
        }

        user.changeMyInformation(command.name(), command.email());

        // 유저가 비밀번호를 변경했나요?
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            user.changePassword(passwordEncoder.encode(command.password()));
        }

        userRepository.save(user);
        return user;
    }

    public void markUserAsDeleted(Long userId) {
        User user = getActiveUserById(userId);
        user.deleted();
        userRepository.delete(user);
    }

    private User getActiveUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorType.USER_NOT_FOUND_ERROR));

        if (user.isDeleted()) {
            throw new CustomException(ErrorType.USER_NOT_FOUND_ERROR);
        }

        return user;
    }
}
