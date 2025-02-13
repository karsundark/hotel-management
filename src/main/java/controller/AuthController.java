package controller;

import model.Role;
import model.User;
import service.UserService;
import security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader("Authorization") String token, @RequestBody User user) {
        // Extract username from token
        String requestingUser = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Optional<User> adminUser = userService.findByUsername(requestingUser);

        // Check if the requester is an ADMIN
        if (adminUser.isEmpty() || !adminUser.get().getRoles().contains(Role.ADMIN)) {
            return ResponseEntity.status(403).body("Access Denied: Only admins can register users.");
        }

        // Assign role USER by default unless explicitly provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(Role.USER));
        }

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent() && userService.verifyPassword(password, user.get().getPassword())) {
            String token = jwtUtil.generateToken(username, user.get().getRoles());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}