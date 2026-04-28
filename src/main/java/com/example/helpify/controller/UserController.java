    package com.example.helpify.controller;


    import com.example.helpify.entity.User;
    import com.example.helpify.service.UserService;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(
            origins = {
                    "http://127.0.0.1:5500",
                    "http://localhost:5500",
                    "https://helpify-frontend-two.vercel.app"
            },
            allowCredentials = "true"
    )
    public class UserController {
        @Autowired
        private UserService userService;

        @PostMapping("/register")
        public User register(@RequestBody User user) {
            return userService.register(user);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
            try {
                User loggedInUser = userService.login(user.getEmail(), user.getPassword());
                session.setAttribute("user", loggedInUser);
                return ResponseEntity.ok(loggedInUser); // 👈 RETURN USER
            } catch (Exception e) {
                return ResponseEntity
                        .status(400)
                        .body(e.getMessage()); // 👈 send clean error
            }
        }

        // ===== VERIFY OTP =====
        @PostMapping("/verify")
        public String verify(@RequestParam String email, @RequestParam String otp) {
            return userService.verifyOTP(email, otp);
        }

        // ===== CHECK SESSION =====
        @GetMapping("/me")
        public ResponseEntity<?> getUser(HttpSession session) {
            User user = (User) session.getAttribute("user");

            if (user == null) {
                return ResponseEntity.status(401).body("Not logged in");
            }

            return ResponseEntity.ok(user);
        }
        // ===== LOGOUT =====
        @PostMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            return "Logged out";
        }
    }
