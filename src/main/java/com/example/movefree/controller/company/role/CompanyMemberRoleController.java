package com.example.movefree.controller.company.role;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.port.company.CompanyMemberRolePort;
import io.swagger.annotations.Api;
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
    CompanyMemberRolePort companyMemberRolePort;

    public CompanyMemberRoleController(CompanyMemberRolePort companyMemberRolePort) {
        this.companyMemberRolePort = companyMemberRolePort;
    }

    private record CompanyMemberRoleRequest(String name) {}

    /**
     * 200 - Success
     * 404 - Role not found
     * 404 - Member not found
     */
    @PostMapping("/create")
    public ResponseEntity<CompanyMemberRoleDTO> createRole(@RequestBody CompanyMemberRoleRequest companyMemberRole, Principal principal) {
        try {
            return ResponseEntity.ok(companyMemberRolePort.createRole(companyMemberRole.name(), principal));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        } catch (UserForbiddenException e) {
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
    public ResponseEntity<String> addRoleToMember(@PathVariable int id, @PathVariable int memberId, Principal principal) {
        try {
            companyMemberRolePort.addRoleToMember(id, memberId, principal);
            return ResponseEntity.ok("Added Role to Member");
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        } catch (MemberAlreadyHasRoleException e) {
            return e.getResponseEntity();
        } catch (UserForbiddenException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Role not found
     * 404 - User not found
     * 403 - User forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable int id, Principal principal) {
        try {
            companyMemberRolePort.deleteRole(id, principal);
        } catch (UserForbiddenException e) {
            return e.getResponseEntity();
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
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
    public ResponseEntity<String> removeMemberRole(@PathVariable int id, @PathVariable int memberId, Principal principal) {
        try {
            companyMemberRolePort.removeMemberRole(id, memberId, principal);
        } catch (IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        } catch (UserForbiddenException e) {
            return e.getResponseEntity();
        }
        return ResponseEntity.ok("Removed");
    }
}
