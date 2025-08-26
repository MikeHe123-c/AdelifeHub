package com.adlifehub.adlife.service;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserService(UserMapper userMapper, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public User register(String username, String email, String raw) {
        if (userMapper.findByUsername(username) != null)
            throw new IllegalArgumentException("username taken");
        if (email != null && userMapper.findByEmail(email) != null)
            throw new IllegalArgumentException("email taken");
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(raw));
        u.setRolesJson("[\"USER\"]");
        userMapper.insert(u);
        return u;
    }

    public boolean checkPassword(User u, String raw) {
        return encoder.matches(raw, u.getPasswordHash());
    }

    public void updateProfile(User u) {
        userMapper.updateProfile(u);
    }
}
