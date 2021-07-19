package com.covid19army.UserService.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.covid19army.UserService.models.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {

	Optional<UserSession> findByLoginrequestid(UUID loginRequestId);
}
