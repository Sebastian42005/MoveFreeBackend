package com.example.movefree.controller.company.role;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.service.company.CompanyMemberRoleService;
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

@Api(tags = "Company Member Role")
@RestController
@RequestMapping("/api/company/members/roles")
public class CompanyMemberRoleController {
    final CompanyMemberRoleService companyMemberRoleService;

    public CompanyMemberRoleController(CompanyMemberRoleService companyMemberRoleService) {
        this.companyMemberRoleService = companyMemberRoleService;
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
            return ResponseEntity.ok(companyMemberRoleService.createRole(companyMemberRole.getName(), principal));
        } catch (IdNotFoundException e) {
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
    public ResponseEntity<String> addRoleToMember(@PathVariable Integer id, @PathVariable Integer memberId, Principal principal) {
        try {
            companyMemberRoleService.addRoleToMember(id, memberId, principal);
            return ResponseEntity.ok("Added Role to Member");
        } catch (IdNotFoundException | MemberAlreadyHasRoleException e) {
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
    public ResponseEntity<String> deleteRole(@PathVariable Integer id, Principal principal) {
        try {
            companyMemberRoleService.deleteRole(id, principal);
        } catch (IdNotFoundException e) {
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
    public ResponseEntity<String> removeMemberRole(@PathVariable Integer id, @PathVariable Integer memberId, Principal principal) {
        try {
            companyMemberRoleService.removeMemberRole(id, memberId, principal);
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        }
        return ResponseEntity.ok("Removed");
    }
}
