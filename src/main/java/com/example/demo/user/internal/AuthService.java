package com.example.demo.user.internal;


import com.example.demo.currency.CurrencyApiImpl;
import com.example.demo.wallet.WalletApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WalletApi walletRespository;
    private final CurrencyApiImpl currencyApi;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            WalletApi walletRespository,
            CurrencyApiImpl currencyApi
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.walletRespository = walletRespository;
        this.currencyApi = currencyApi;
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User(
                request.phoneNumber(),
                passwordEncoder.encode(request.password()),
                UserRole.MANUAL
        );

        userRepository.save(user);
        walletRespository.createWallet("USDT", BigDecimal.ZERO, user.getId(), currencyApi.getReferenceById(1L));
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, user.getPhoneNumber(), user.getRole().name());
    }

    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.phoneNumber(), request.password())
        );

        UserDetails userDetails = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(userDetails);
        return new LoginResponseDto(
                token
        );
    }

    public UserProfileDto userProfile(User currentUser) {
        return new UserProfileDto(currentUser.getName(), currentUser.getLastName(), currentUser.getPhoneNumber());
    }

    public UserProfileDto updateUserProfile(User currentUser, UserProfileDto userProfileDto) {
        currentUser.setName(userProfileDto.name());
        currentUser.setLastName(userProfileDto.lastName());
        currentUser.setPhoneNumber(userProfileDto.phoneNumber());
        User updateUser = userRepository.save(currentUser);
        return new UserProfileDto(updateUser.getName(), updateUser.getLastName(), updateUser.getPhoneNumber());
    }
}