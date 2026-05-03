package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dto.LoginRequest;
import com.pearl.nov25.springproj1.dto.LoginResponse;
import com.pearl.nov25.springproj1.dto.RegisterRequest;
import com.pearl.nov25.springproj1.models.User;
import com.pearl.nov25.springproj1.repositories.UserRepository;
import com.pearl.nov25.springproj1.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Update last login
            User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            if (user != null) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
            }

            LoginResponse response = new LoginResponse(
                token,
                refreshToken,
                jwtTokenUtil.getExpirationTime(token),
                user
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Authentication failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if user already exists
            if (userRepository.existsByUsername(registerRequest.username())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists"));
            }

            if (userRepository.existsByEmail(registerRequest.email())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists"));
            }

            // Create new user
            User user = new User();
            user.setUsername(registerRequest.username());
            user.setEmail(registerRequest.email());
            user.setFirstName(registerRequest.firstName());
            user.setLastName(registerRequest.lastName());
            user.setPassword(passwordEncoder.encode(registerRequest.password()));
            user.setRole(registerRequest.role() != null ? registerRequest.role() : "USER");
            user.setEnabled(true);

            userRepository.save(user);

            // Generate tokens
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getAuthorities()
            );

            String token = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            LoginResponse response = new LoginResponse(
                token,
                refreshToken,
                jwtTokenUtil.getExpirationTime(token),
                user
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Registration failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Refresh token is required"));
            }

            String newToken = jwtTokenUtil.refreshToken(refreshToken);
            if (newToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired refresh token"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("expiration", jwtTokenUtil.getExpirationTime(newToken));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token refresh failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Logout failed", "message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                User user = userRepository.findByUsername(username).orElse(null);
                
                if (user != null) {
                    // Remove sensitive information
                    user.setPassword(null);
                    return ResponseEntity.ok(user);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "User not authenticated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get current user", "message", e.getMessage()));
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                boolean isValid = jwtTokenUtil.validateToken(token);
                
                Map<String, Object> response = new HashMap<>();
                response.put("valid", isValid);
                response.put("username", isValid ? jwtTokenUtil.getUsernameFromToken(token) : null);
                response.put("expiration", isValid ? jwtTokenUtil.getExpirationTime(token) : null);
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid authorization header"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Token validation failed", "message", e.getMessage()));
        }
    }
}
