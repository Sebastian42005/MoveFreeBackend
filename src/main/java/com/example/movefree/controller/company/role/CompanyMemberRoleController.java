package com.example.movefree.controller.company.role;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.database.company.member.role.CompanyMemberRoleRepository;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;

@RestController
@RequestMapping("/company/members/roles")
public class CompanyMemberRoleController {
    @Autowired
    CompanyMemberRoleRepository roleRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyMemberRepository memberRepository;

    @PostMapping("/create")
    public CompanyMemberRoleDTO createRole(@RequestBody CompanyMemberRoleDTO roleDTO, Principal principal) {
        UserDTO userDTO = findUserByUsername(principal.getName());
        CompanyDTO companyDTO = userDTO.getCompany();
        roleDTO.setCompany(companyDTO);
        CompanyMemberRoleDTO savedRole = roleRepository.save(roleDTO);
        companyDTO.getMemberRoles().add(savedRole);
        companyRepository.save(companyDTO);
        return savedRole;
    }

    @PutMapping("/{id}/add/{memberId}")
    public ResponseEntity<String> addRoleToMember(@PathVariable int id, @PathVariable int memberId, Principal principal) {
        checkForAuthorization(principal.getName(), id, memberId);
        CompanyMemberRoleDTO roleDTO = findRoleById(id);
        if (roleDTO.getMembers().stream().anyMatch(member -> member.getId() == memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member already has this role");
        }
        CompanyMemberDTO memberDTO = findMemberById(memberId);
        roleDTO.getMembers().add(memberDTO);
        memberDTO.getRoles().add(roleDTO);
        roleRepository.save(roleDTO);
        memberRepository.save(memberDTO);
        return ResponseEntity.ok("Added role " + roleDTO.getName() + " to member " + memberDTO.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable int id, Principal principal) {
        checkForRoleAuthorization(principal.getName(), id);
        roleRepository.deleteById(id);
        return ResponseEntity.ok("Deleted Role");
    }

    @DeleteMapping("/{id}/member/{memberId}")
    public ResponseEntity<String> removeMemberFromRole(@PathVariable int id, @PathVariable int memberId, Principal principal) {
        checkForAuthorization(principal.getName(), id, memberId);
        CompanyMemberRoleDTO roleDTO = findRoleById(id);
        CompanyMemberDTO memberDTO = findMemberById(memberId);
        if (memberDTO.getRoles().stream().noneMatch(role -> role.getId() == id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this role");
        memberDTO.getRoles().removeIf(role -> role.getId() == id);
        roleDTO.getMembers().removeIf(memberDTO1 -> memberDTO1.getId() == id);
        roleRepository.save(roleDTO);
        memberRepository.save(memberDTO);
        return ResponseEntity.ok("Removed");
    }

    private void checkForAuthorization(String username, int roleId, int memberId) {
        UserDTO userDTO = findUserByUsername(username);
        if (userDTO.getCompany().getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (userDTO.getCompany().getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkForRoleAuthorization(String username, int roleId) {
        UserDTO userDTO = findUserByUsername(username);
        if (userDTO.getCompany().getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private UserDTO findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private CompanyMemberDTO findMemberById(int id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Member not found"));
    }

    private CompanyMemberRoleDTO findRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Role not found"));
    }
}
