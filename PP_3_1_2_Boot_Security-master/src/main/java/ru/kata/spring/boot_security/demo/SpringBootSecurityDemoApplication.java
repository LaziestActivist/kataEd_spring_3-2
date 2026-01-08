package ru.kata.spring.boot_security.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      RoleRepository roleRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Создаем роли, если их нет
            Role roleUser = roleRepository.findById(1L).orElseGet(() -> {
                Role role = new Role(1L, "ROLE_USER");
                return roleRepository.save(role);
            });

            Role roleAdmin = roleRepository.findById(2L).orElseGet(() -> {
                Role role = new Role(2L, "ROLE_ADMIN");
                return roleRepository.save(role);
            });

            // Создаем admin пользователя
            User existingAdmin = userRepository.findByUsername("admin");
            if (existingAdmin == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRoles(Set.of(roleAdmin));
                userRepository.save(admin);
                System.out.println("✓ Created admin user: admin/admin");
            } else {
                // Обновляем пароль если он не BCrypt
                if (!existingAdmin.getPassword().startsWith("$2a$")) {
                    existingAdmin.setPassword(passwordEncoder.encode("admin"));
                    userRepository.save(existingAdmin);
                    System.out.println("✓ Updated admin password to BCrypt hash");
                }
            }

            // Создаем обычного пользователя
            User existingUser = userRepository.findByUsername("user");
            if (existingUser == null) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRoles(Set.of(roleUser));
                userRepository.save(user);
                System.out.println("✓ Created regular user: user/user");
            } else {
                // Обновляем пароль если он не BCrypt
                if (!existingUser.getPassword().startsWith("$2a$")) {
                    existingUser.setPassword(passwordEncoder.encode("user"));
                    userRepository.save(existingUser);
                    System.out.println("✓ Updated user password to BCrypt hash");
                }
            }
        };
    }
}