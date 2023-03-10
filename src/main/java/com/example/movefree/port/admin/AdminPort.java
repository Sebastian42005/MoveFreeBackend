package com.example.movefree.port.admin;

import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.exception.IdNotFoundException;

import java.util.List;
import java.util.UUID;

public interface AdminPort {
    List<CompanyRequest> getAllRequests();
    String declineRequest(UUID id) throws IdNotFoundException;
    String acceptRequest(UUID id) throws IdNotFoundException;
}
