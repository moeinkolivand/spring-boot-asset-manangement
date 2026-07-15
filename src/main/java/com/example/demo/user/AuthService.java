package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
                request.phoneNumber(),
                passwordEncoder.encode(request.password()),
                UserRole.MANUAL
        );

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, user.getPhoneNumber(), user.getRole().name());
    }

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.phoneNumber(), request.password())
        );

        UserDetails userDetails = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(userDetails);
        return new AuthResponseDto(
                token,
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );
    }
}