package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.security.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserSec, Long> {

    Optional<UserSec> findUserEntityByCuil(String cuil);
}
