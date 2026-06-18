package com.cavi.stocky.config;

import com.cavi.stocky.model.Usuario;
import com.cavi.stocky.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Crea automáticamente un usuario ADMIN al iniciar la aplicación,
 * si todavía no existe. Así cualquier persona que levante el proyecto
 * (en cualquier computador) tiene un admin disponible sin pasos manuales.
 */
@Configuration
public class AdminSeeder {

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Bean
    public CommandLineRunner seedAdminUser(UsuarioRepository usuarioRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ROLE_ADMIN");
                usuarioRepository.save(admin);
            }
        };
    }
}