package com.example.movefree.controller.company.role;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.port.company.CompanyMemberRolePort;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@Api(tags = "Company Member Role")
@RestController
@RequestMapping("/api/company/members/roles")
public class CompanyMemberRoleController {
    final CompanyMemberRolePort companyMemberRolePort;

    public CompanyMemberRoleController(CompanyMemberRolePort companyMemberRolePort) {
        this.companyMemberRolePort = companyMemberRolePort;
    }

    @NoArgsConstructor
    @Getter
    private static class CompanyMemberRoleRequest {String name;}

    /**
     * 200 - Success
     * 404 - Role not found
     * 404 - Member not found
     */
    @PostMapping("/create")
    public ResponseEntity<CompanyMemberRoleDTO> createRole(@RequestBody CompanyMemberRoleRequest companyMemberRole, Principal principal) {
        try {
            return ResponseEntity.ok(companyMemberRolePort.createRole(companyMemberRole.getName(), principal));
        } catch (IdNotFoundException | UserForbiddenException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Member not found
     * 404 - Role not found
     * 403 - User forbidden
     */
    @PutMapping("/{id}/add/{memberId}")
    public ResponseEntity<String> addRoleToMember(@PathVariable UUID id, @PathVariable UUID memberId, Principal principal) {
        try {
            companyMemberRolePort.addRoleToMember(id, memberId, principal);
            return ResponseEntity.ok("Added Role to Member");
        } catch (IdNotFoundException | MemberAlreadyHasRoleException | UserForbiddenException e) {
            return e.getResponseEntityWithMessage();
        }
    }

    /**
     * 200 - Success
     * 404 - Role not found
     * 404 - User not found
     * 403 - User forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable UUID id, Principal principal) {
        try {
            companyMemberRolePort.deleteRole(id, principal);
        } catch (UserForbiddenException | IdNotFoundException e) {
            return e.getResponseEntity();
        }
        return ResponseEntity.ok("Deleted Role");
    }

    /**
     * 200 - Success
     * 404 - Role not found
     * 404 - Member not found
     * 403 - User forbidden
     */
    @DeleteMapping("/{id}/member/{memberId}")
    public ResponseEntity<String> removeMemberRole(@PathVariable UUID id, @PathVariable UUID memberId, Principal principal) {
        try {
            companyMemberRolePort.removeMemberRole(id, memberId, principal);
        } catch (IdNotFoundException | UserForbiddenException e) {
            return e.getResponseEntityWithMessage();
        }
        return ResponseEntity.ok("Removed");
    }
}
