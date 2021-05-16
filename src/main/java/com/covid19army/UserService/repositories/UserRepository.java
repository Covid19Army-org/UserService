/**
 * 
 */
package com.covid19army.UserService.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.covid19army.UserService.models.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	boolean existsByMobilenumber(String mobileNumber);
	Optional<User> findByMobilenumber(String mobileNumber);
}
