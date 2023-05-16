package com.assessment.bank.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.bank.entities.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>{

	 Optional<User> findByEmail(String email);
         
      
         List<User> findAllByIdNot(int id);

}
