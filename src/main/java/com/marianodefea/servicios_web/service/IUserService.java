package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.security.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserSec> findAll();
    Optional<UserSec> findById(Long id);
    UserSec save(UserSec userSec);
    void deleteById(Long id);
    UserSec update(UserSec userSec);
    String encryptPassword(String password);
    boolean existsByCuil(String cuil);
}
