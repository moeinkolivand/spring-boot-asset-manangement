package com.example.demo.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserSeederCommandLine implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSeederCommandLine(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String adminPhone = "09123456789";

        if (userRepository.findByPhoneNumber(adminPhone).isEmpty()) {
            String encodedPassword = passwordEncoder.encode("admin123");

            User admin = new User(
                    adminPhone,
                    encodedPassword,
                    UserRole.ADMIN,
                    "System",
                    "Admin"
            );

            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists. Skipping.");
        }
    }
}