package com.magazine.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazine.model.Product;
import com.magazine.model.User;
import com.magazine.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository repository;
	
	@PostMapping("/registration")
	public ResponseEntity registerUser(@RequestBody User user) {
		if(repository.findByLogin(user.getLogin())!=null) {
			return ResponseEntity.status(401).body("Такой пользователь уже есть");
		}
		else {
			user.setRole("user");
			User _user = repository.save(new User(user.getLogin(), user.getPassword(), user.getRole()));
			return ResponseEntity.ok(_user);
		}
	}

	@PostMapping("/login")
	public ResponseEntity findByAuthor(@RequestBody User user) {
		System.out.println("Полученные данные : "+user.getLogin());
		User users = new User();
		if(repository.findByLogin(user.getLogin())!=null) {
			users = repository.findByLogin(user.getLogin());
		}
		List<User> userout = new ArrayList<>();
		repository.findAll().forEach(userout::add);
		if(users.getPassword().equals(user.getPassword()))
			return ResponseEntity.ok(users);
		else
			return ResponseEntity.status(401).body("Такого пользователя нет");
	}
}
