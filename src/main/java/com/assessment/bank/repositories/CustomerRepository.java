
package com.assessment.bank.repositories;

import com.assessment.bank.entities.Customer;
import com.assessment.bank.entities.CustomerStatus;
import com.assessment.bank.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kiza
 */
public interface CustomerRepository  extends JpaRepository<Customer, Integer>{
    List<Customer> findByApprovedByAndStatus(User approvedBy, CustomerStatus status);
    List<Customer> findByInitiatedByAndStatus(User initiatedBy, CustomerStatus status);
    List<Customer> findByInitiatedBy(User initiatedBy);
    
}
