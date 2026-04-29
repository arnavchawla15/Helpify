    package com.example.helpify.controller;


    import com.example.helpify.entity.User;
    import com.example.helpify.service.JwtService;
    import com.example.helpify.service.UserService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    public class UserController {
        @Autowired
        private UserService userService;
        @Autowired
        private JwtService jwtService;

        // ===== REGISTER =====
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody User user) {
            try {
                User newUser = userService.register(user);
                return ResponseEntity.ok(newUser);
            } catch (Exception e) {
                return ResponseEntity
                        .status(400)
                        .body(e.getMessage()); // 👈 sends "User already exists"
            }
        }
        // ===== LOGIN (JWT) =====
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody User user) {
            try {
                User loggedInUser = userService.login(user.getEmail(), user.getPassword());

                String token = jwtService.generateToken(loggedInUser.getEmail());

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "user", loggedInUser
                ));

            } catch (Exception e) {
                return ResponseEntity.status(400).body(e.getMessage());
            }
        }

        // ===== VERIFY OTP =====
        @PostMapping("/verify")
        public String verify(@RequestParam String email, @RequestParam String otp) {
            return userService.verifyOTP(email, otp);
        }

        // ===== GET CURRENT USER (JWT BASED) =====
        @GetMapping("/me")
        public ResponseEntity<?> me(HttpServletRequest req) {
            String email = (String) req.getAttribute("userEmail");

            if (email == null) {
                return ResponseEntity.status(401).body("Not logged in");
            }

            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        }
        // ===== LOGOUT =====
        @PostMapping("/logout")
        public String logout() {
            return "Logged out (client should delete token)";
        }

        //Update
        @PutMapping("/update")
        public ResponseEntity<?> updateProfile(
                @RequestBody User updatedUser,
                HttpServletRequest req
        ) {
            try {
                String email = (String) req.getAttribute("userEmail");

                if (email == null) {
                    return ResponseEntity.status(401).body("Not logged in");
                }

                User user = userService.updateProfile(email, updatedUser);
                return ResponseEntity.ok(user);

            } catch (Exception e) {
                return ResponseEntity.status(400).body(e.getMessage());
            }
        }
    }
