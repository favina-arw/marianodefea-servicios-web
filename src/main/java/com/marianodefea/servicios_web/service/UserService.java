package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.security.UserSec;
import com.marianodefea.servicios_web.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserSec> findAll() { return userRepository.findAll(); }

    @Override
    public Optional<UserSec> findById(Long id) { return userRepository.findById(id); }

    @Override
    public UserSec save(UserSec userSec) {
        return userRepository.save(userSec);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserSec update(UserSec userSec) {
        return userRepository.save(userSec);
    }

    @Override
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
