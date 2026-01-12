package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    PasswordEncoder getPasswordEncoder();

    User findUserById(Long userId);

    List<User> allUsers();

    List<Role> allRoles();

    boolean saveUser(User user, PasswordEncoder passwordEncoder);

    boolean deleteUser(Long userId);
}
