package com.assessment.bank.entities;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Digits(integer = 16, fraction = 0)
    @Pattern(regexp = "\\d{16}")
    private String accountNumber;
    
    @NotNull
    @NotBlank
    private String names;

    private boolean IsPEP;
    private boolean IsRwandan;
    private boolean IsResident;

    @NotNull
    private AccountOpenedMethod accountOpenedMethod;

    @NotNull
    private NatureBusiness natureBusiness;

    @NotNull
    private PurposeAccount purposeAccount;

    @NotNull
    private TransactionNature transactionNature;

    private CustomerStatus status = CustomerStatus.AwaitingForApproval;

    @NotNull
    @NotBlank
    private String mobileNumber;

    private int riskscore = 0;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<SupportingDocument> supportingDocuments;

    @ManyToOne
    private User approvedBy;

    @ManyToOne
    private User initiatedBy;

    @Column(columnDefinition = "text")
    private String comments;

    public int CalculateRiskscore() {
        int riskScore = 0;

        if (!this.IsResident) {
            riskScore += 5;
        }
        if (!this.IsRwandan) {
            riskScore += 18;
        }

        if (this.IsPEP) {
            riskScore += 30;
        }
        riskscore += this.accountOpenedMethod.getRiskScore() + this.natureBusiness.getRiskScore() + this.purposeAccount.getRiskScore() + this.transactionNature.getRiskScore();
        return riskScore;
    }

    public String getScoreInWords() {
        if (this.riskscore < 20) {
            return "Low Risk";
        } else if (this.riskscore < 30) {
            return "Medium Risk";
        } else {
            return "High Risk";
        }

    }
    
    
    

}
