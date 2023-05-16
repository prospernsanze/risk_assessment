package com.assessment.bank.controllers;

import com.assessment.bank.config.UserInfoDetails;
import com.assessment.bank.entities.Customer;
import com.assessment.bank.entities.CustomerStatus;
import com.assessment.bank.entities.SupportingDocument;
import com.assessment.bank.entities.User;
import com.assessment.bank.service.CustomerService;
import com.assessment.bank.service.DocumentService;
import com.assessment.bank.service.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author kiza
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final UserServices userService;
    private final DocumentService DocumentService;

    @Autowired
    public CustomerController(CustomerService customerService, UserServices userService, DocumentService docService) {
        this.customerService = customerService;
        this.userService = userService;
        this.DocumentService = docService;

    }

    @GetMapping("/new")
    public String showCreateCustomerForm(Model model) {
        Customer customer = new Customer();

        // Set the current user as the initiatedBy user
        User currentUser = getCurrentUser();

        customer.setInitiatedBy(currentUser);

        model.addAttribute("customer", customer);
        // Populate the drop-down with available approval users (excluding the current user)
        List<User> approvalUsers = userService.getAllUsersExcept(currentUser.getId());

        System.out.println("abakinyi " + approvalUsers.size());
        model.addAttribute("approvalUsers", approvalUsers);
        return "create-customer";
    }

    @PostMapping("/new")
    public String createCustomer(@Valid Customer customer,
            @RequestParam("files") MultipartFile[] files, BindingResult bindingResult, Model model) {
        // Perform file upload logic and save the customer
        // Update status to 'Initiated'

        if (bindingResult.hasErrors()) {
            System.out.println("mugoaboa mugabo");
            model.addAttribute("customer", customer);
            model.addAttribute("approvalUsers", userService.getAllUsersExcept(customer.getApprovedBy().getId()));
            model.addAttribute("errors", bindingResult.getAllErrors());

            // Validation errors exist
            return "create-customer";

        }
        User currentUser = getCurrentUser();

        customer.setInitiatedBy(currentUser);
        customer.setStatus(CustomerStatus.AwaitingForApproval);
        customerService.saveNewCustomer(customer, files);
        return "redirect:/customer/initiated";
    }

    @GetMapping("/initiated")
    public String showInitiatedCustomersByCurrentUser(Model model) {
        User currentUser = getCurrentUser();
        List<Customer> initiatedCustomers = customerService.CustomerInitiatedByMe(currentUser);
        model.addAttribute("customers", initiatedCustomers);
        return "initiated";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();

        Optional<User> userCurrent = userService.findByID(user.getId());
        if (userCurrent.isPresent()) {
            return userCurrent.get();
        } else {
            return null;
        }

    }

    @GetMapping("/{id}")
    public String getCustomerDetails(@PathVariable int id, Model model) {
        // Retrieve the customer from the database or any other data source
        try {
            Optional<Customer> optionalCustomer = customerService.findByID(id);
            Customer customer = optionalCustomer.orElseThrow(ResourceNotFoundException::new);
             User currentUser = getCurrentUser();
            model.addAttribute("customer", customer);
            model.addAttribute("user", currentUser);
            return "customer-view";
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found", e);
        }
      
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadSupportingDocument(@PathVariable int documentId, HttpServletResponse response) throws IOException {
        // Retrieve the document from the database or any other data source based on the customerId and documentId
        Optional<SupportingDocument> document = DocumentService.findByID(documentId);

        if (document == null) {
            // Document not found
            return ResponseEntity.notFound().build();
        }

        SupportingDocument doc = document.get();
        File file = new File(doc.getPath());
        byte[] documentBytes = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
    }

//    @DeleteMapping("/supporting-documents/{customerId}/{documentId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public RedirectView deleteSupportingDocument(@PathVariable int customerId, @PathVariable int documentId, HttpServletRequest request) {
//
//        Optional<SupportingDocument> document = DocumentService.findByID(documentId);
//        if (document != null) {
//            // Delete the file associated with the document
//            File file = new File(document.get().getPath());
//            file.delete();
//
//            // Delete the document from the database or any other data source
//            DocumentService.deleteById(documentId);
//        }
//
//        // Redirect back to the same page
//        String referer = request.getHeader("Referer");
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(referer);
//        return redirectView;
//    }
    @PostMapping("/{customerId}/approve")
    public RedirectView approveCustomer(@PathVariable int customerId, @PathVariable String comments, @RequestParam String approval, HttpServletRequest request) {
        // Call the service method to approve the customer

        if (approval.equals("approve")) {

            customerService.ApproveCustomer(customerId, comments);
        } else if (approval.equals("reject")) {
            customerService.RejectCustomer(customerId, comments);
        }

        String referer = request.getHeader("Referer");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(referer);
        return redirectView;
    }

//    @PostMapping("/{customerId}/reject")
//    public RedirectView rejectCustomer(@PathVariable int customerId, @PathVariable String comments, HttpServletRequest request) {
//        // Call the service method to approve the customer
//        customerService.RejectCustomer(customerId, comments);
//        String referer = request.getHeader("Referer");
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(referer);
//        return redirectView;
//    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException() {
            super("Customer not found");
        }
    }
}
