package com.example.movefree.database.company.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company_requests")
public class CompanyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String username;

    @NotNull
    String message;

    @Data
    @NoArgsConstructor
    public static class Request {
        String message;
    }
}
