package com.cavi.stocky.controller;
import com.cavi.stocky.dto.AuthRequest;
import com.cavi.stocky.dto.AuthResponse;
import com.cavi.stocky.model.Usuario;
import com.cavi.stocky.repository.UsuarioRepository;
import com.cavi.stocky.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario con rol USER.
     * La contraseña se almacena encriptada con BCrypt.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole("ROLE_USER");
        usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }
    /**
     * Autentica al usuario y devuelve un JWT válido por 24 horas.
     * El token debe enviarse en el header Authorization de los siguientes requests:
     *   Authorization: Bearer <token>
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
             String role = auth.getAuthorities().stream()
                     .findFirst()
                     .map(GrantedAuthority::getAuthority)
                     .orElse("ROLE_USER");
              String token = jwtUtil.generateToken(request.getUsername(), role);
                return ResponseEntity.ok(new AuthResponse(token));

            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }

