package com.assessment.bank.entities;

import com.assessment.bank.config.PasswordEncoderUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(length = 100)
    private String name;

    @NonNull
    @NotNull
    @NotBlank
    @Email
    private String email;

    @NonNull
    @NotNull
    @NotBlank
    @Size(min = 8)
    @Column(columnDefinition = "text")
    private String password;

    @NonNull
    @NotNull
    @NotBlank
    private String roles;

    @OneToMany(mappedBy = "initiatedBy")
    private List<Customer> initiatedCustomers;

    @OneToMany(mappedBy = "approvedBy")
    private List<Customer> approvedCustomers;

    public User(String name, String email, String password, String roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = PasswordEncoderUtil.encodePassword(password);
    }

}
