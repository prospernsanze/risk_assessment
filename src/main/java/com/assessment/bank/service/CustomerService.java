
package com.assessment.bank.service;

import com.assessment.bank.entities.Customer;
import com.assessment.bank.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kiza
 */
public interface CustomerService {

    Customer saveNewCustomer(Customer customer, MultipartFile[] files);

    void ApproveCustomer(int customerId, String comments);

    void RejectCustomer(int customerId, String comments);

    List<Customer> CustomerApprovedBy(User approvedBy);

    List<Customer> customerAwaitingApprovalOf(User approval);

    List<Customer> CustomerInitiatedByMeApproved(User initiatedBy);

    List<Customer> CustomerInitiatedByMe(User initiatedBy);

    Optional<Customer> findByID(int customerID);

}
