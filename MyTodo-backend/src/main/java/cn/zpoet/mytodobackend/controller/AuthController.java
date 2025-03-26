package cn.zpoet.mytodobackend.controller;

import cn.zpoet.mytodobackend.model.Role;
import cn.zpoet.mytodobackend.model.User;
import cn.zpoet.mytodobackend.reporistory.UserRepository;
import cn.zpoet.mytodobackend.security.JwtUtil;
import cn.zpoet.mytodobackend.utils.ApiResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ApiResponse.error(400, "Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.user);
        }
        userRepository.save(user);
        return ApiResponse.success("User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            String token = jwtUtil.generateToken(user.getUsername());
            return ApiResponse.success(new TokenResponse(token));
        } catch (Exception e) {
            return ApiResponse.error(401, "Invalid username or password");
        }
    }

    // 内部类封装 token
    record TokenResponse(String token) {
    }
}
