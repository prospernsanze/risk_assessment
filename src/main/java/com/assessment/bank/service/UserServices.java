/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assessment.bank.service;

import com.assessment.bank.entities.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author kiza
 */
public interface UserServices {
    
    List<User> listUsers();
    List<User> getAllUsersExcept(int id);
    Optional<User> findByID(int id);
    Optional<User> findByEmail(String email);

    User saveNewUser(User user);

    
}
