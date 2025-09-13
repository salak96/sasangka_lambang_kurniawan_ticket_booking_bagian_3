package event.booking.system.development.service.impl;

import event.booking.system.development.entity.managementuser.Role;
import event.booking.system.development.entity.managementuser.User;
import event.booking.system.development.model.request.LoginRequest;
import event.booking.system.development.model.request.RegisterRequest;
import event.booking.system.development.model.response.AuthResponse;
import event.booking.system.development.repository.RoleRepository;
import event.booking.system.development.repository.UserRepository;
import event.booking.system.development.security.JwtTokenProvider;
import event.booking.system.development.service.managementuser.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwt;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        // Normalisasi input
        final String email    = req.email().trim().toLowerCase();
        final String username = req.username().trim().toLowerCase();
        final String name     = req.name().trim();

        // Validasi unik
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Ambil ROLE_USER
        Role userRole = roleRepo.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not seeded"));

        // Buat user & simpan
        User u = User.builder()
                .email(email)
                .username(username)
                .name(name)
                .passwordHash(encoder.encode(req.password()))
                .isActive(true)
                .roles(Set.of(userRole))
                .build();
        userRepo.save(u);

        // Auto-login setelah register -> JWT
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password()));
        String token = jwt.generateToken(auth);
        Instant exp = jwt.getExpiry(token);

        // Ambil roles dari DB (bukan hardcode)
        var roles = u.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return AuthResponse.of(token, exp, u.getId().toString(), u.getEmail(), u.getName(), roles);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        // Login by email (sesuai DTO). Kalau mau dukung username, ubah DTO & auth provider-nya.
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        String token = jwt.generateToken(auth);
        Instant exp = jwt.getExpiry(token);

        var u = userRepo.findByEmail(req.email().trim().toLowerCase()).orElseThrow();
        var roles = u.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return AuthResponse.of(token, exp, u.getId().toString(), u.getEmail(), u.getName(), roles);
    }
}
