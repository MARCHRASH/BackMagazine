package com.magazine.repository;

import org.springframework.data.repository.CrudRepository;

import com.magazine.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	User findByLogin(String login);
}
