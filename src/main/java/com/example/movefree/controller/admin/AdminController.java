package com.example.movefree.controller.admin;

import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.port.admin.AdminPort;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Api(tags = "Admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    final AdminPort adminPort;

    public AdminController(AdminPort adminPort) {
        this.adminPort = adminPort;
    }

    @GetMapping("/company-requests")
    public ResponseEntity<List<CompanyRequest>> getAllRequests() {
        return ResponseEntity.ok(adminPort.getAllRequests());
    }

    @DeleteMapping("/company-requests/{id}/decline")
    public ResponseEntity<String> declineRequest(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(adminPort.declineRequest(id));
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        }
    }

    @PatchMapping("/company-requests/{id}/accept")
    public ResponseEntity<String> acceptRequest(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(adminPort.acceptRequest(id));
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        }
    }
}
