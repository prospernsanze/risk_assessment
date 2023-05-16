/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assessment.bank.repositories;

import com.assessment.bank.entities.SupportingDocument;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kiza
 */
public interface DocumentRepository extends JpaRepository<SupportingDocument, Integer>{
    
}
