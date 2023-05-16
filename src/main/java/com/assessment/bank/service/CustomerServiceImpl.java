package com.assessment.bank.service;

import com.assessment.bank.entities.Customer;
import com.assessment.bank.entities.CustomerStatus;
import com.assessment.bank.entities.SupportingDocument;
import com.assessment.bank.entities.User;
import com.assessment.bank.repositories.CustomerRepository;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveNewCustomer(Customer customer, MultipartFile[] files) {

        customer.setRiskscore(customer.CalculateRiskscore());
        customerRepository.save(customer);

        // Upload files and create supporting documents
        List<SupportingDocument> supportingDocuments = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                String fileName = generateUniqueFileName(originalFileName);
                String uploadDir = "src/main/resources/uploads/";

                try {
                    // Create the upload directory if it doesn't exist
                    File directory = new File(uploadDir);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    // Save the file to the upload directory
                    java.nio.file.Path filePath = Paths.get(uploadDir, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // Create a new supporting document
                    SupportingDocument document = new SupportingDocument();
                    document.setDocumentName(originalFileName);
                    document.setPath(filePath.toString());
                    document.setCustomer(customer);

                    // Add the supporting document to the list
                    supportingDocuments.add(document);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception as needed
                }
            }
        }

        // Set the supporting documents for the customer
        customer.setSupportingDocuments(supportingDocuments);

        // Save the customer with the supporting documents
        return customerRepository.save(customer);

    }

    private String generateUniqueFileName(String fileName) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String timestamp = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String extension = StringUtils.getFilenameExtension(fileName);
        return timestamp + "_" + fileName.replace("." + extension, "") + "." + extension;
    }

    @Override
    public void ApproveCustomer(int customerId, String comments) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            // Update the customer status, approval user, and comments
            customer.setStatus(CustomerStatus.Approved);
            customer.setComments(comments);
            // Save the updated customer
            customerRepository.save(customer);
        }

    }

    @Override
    public void RejectCustomer(int customerId, String comments) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            // Update the customer status, approval user, and comments
            customer.setStatus(CustomerStatus.Rejected);
            customer.setComments(comments);
            // Save the updated customer
            customerRepository.save(customer);
        }
    }

    @Override
    public List<Customer> CustomerApprovedBy(User approvedBy) {
        return customerRepository.findByApprovedByAndStatus(approvedBy, CustomerStatus.Approved);
    }

    @Override
    public List<Customer> customerAwaitingApprovalOf(User approval) {
        return customerRepository.findByApprovedByAndStatus(approval, CustomerStatus.AwaitingForApproval);
    }

    @Override
    public List<Customer> CustomerInitiatedByMeApproved(User initiatedBy) {
        return customerRepository.findByInitiatedByAndStatus(initiatedBy, CustomerStatus.Approved);
    }

    @Override
    public List<Customer> CustomerInitiatedByMe(User initiatedBy) {
        return customerRepository.findByInitiatedBy(initiatedBy);
    }

    @Override
    public Optional<Customer> findByID(int customerID) {
       return customerRepository.findById(customerID);
    }

}
