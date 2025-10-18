package com.magpie.controllers;

import com.magpie.entities.User;
import com.magpie.dtos.UserCreateTemplate;
import com.magpie.dtos.UserDTO;
import com.magpie.services.JWTService;
import com.magpie.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/r/{publicId}")
    public ResponseEntity<UserDTO> getPublicUserProfile(@PathVariable("publicId") UUID publicId) {

        try {
            // Fetch and map to DTO
            UserDTO userDTO = userService.findByPublicId(publicId);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/r")
    public ResponseEntity<?> getPrivateUserProfile(@RequestHeader(value = "Authorization", required = false) String auth) {
        try {
            if (auth == null || !auth.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("Unauthorized", "Missing or invalid Authorization header"));
            }
            String accessToken = auth.substring(7); // Remove "Bearer " prefix
            String username = jwtService.extractUsername(accessToken);

            if (!jwtService.validateToken(accessToken, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("Unauthorized", "Invalid or expired token"));
            }

            UserDTO profile = userService.findByUsername(username);
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("Not Found", "User not found"));
            }
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("Unauthorized", "Token validation failed: " + e.getMessage()));
        }
    }

    @PostMapping("/c")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody UserCreateTemplate body) {
        try {
            // Validate input
            if ((body.username() == null || body.username().isEmpty())) {
                throw new IllegalArgumentException("Username is required");
            }
            if ((body.password() == null || body.password().isEmpty())) {
                throw new IllegalArgumentException("Password is required");
            }
            if ((body.firstName() == null || body.firstName().isEmpty()) &&
                    (body.lastName() == null || body.lastName().isEmpty())) {
                throw new IllegalArgumentException("User must have at least a first or last name");
            }
            if (body.email() == null || body.email().isEmpty()) {
                throw new IllegalArgumentException("An email is required");
            }

            // Convert DTO to entity
            User user = new User();
            user.setUsername(body.username());
            user.setFirstName(body.firstName());
            user.setLastName(body.lastName());
            user.setEmail(body.email());
            user.setProfilePicture(body.profilePicture());
            user.setPassword(passwordEncoder.encode(body.password()));

            // Save the user
            userService.createUser(user);

            // Return success message
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Failed to create user: " + e.getMessage()
            ));
        }
    }
}
