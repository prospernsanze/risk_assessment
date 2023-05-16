/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assessment.bank.service;

import com.assessment.bank.entities.SupportingDocument;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author kiza
 */


public interface DocumentService {
    
      Optional<SupportingDocument> findByID(int id);
      void deleteById(int id);
    
}
