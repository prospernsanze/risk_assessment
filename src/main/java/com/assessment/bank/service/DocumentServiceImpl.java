/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assessment.bank.service;

import com.assessment.bank.entities.SupportingDocument;
import com.assessment.bank.repositories.DocumentRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;
    
    @Autowired
    public DocumentServiceImpl(DocumentRepository repository1) {
        this.repository = repository1;
    }
    
    @Override
    public Optional<SupportingDocument> findByID(int id) {
        return repository.findById(id);
    }
    
    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }
    
}
