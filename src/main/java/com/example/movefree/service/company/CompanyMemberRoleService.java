package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTOMapper;
import com.example.movefree.database.company.member.role.CompanyMemberRoleRepository;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.exception.enums.NotFoundType;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CompanyMemberRoleService {

    final CompanyMemberRoleRepository roleRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final CompanyMemberRepository memberRepository;

    final CompanyMemberRoleDTOMapper companyMemberRoleDTOMapper = new CompanyMemberRoleDTOMapper();

    public CompanyMemberRoleService(CompanyMemberRoleRepository roleRepository, CompanyRepository companyRepository, UserRepository userRepository, CompanyMemberRepository memberRepository) {
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    
    public CompanyMemberRoleDTO createRole(String role, Principal principal) throws IdNotFoundException {
        CompanyMemberRole companyMemberRole = new CompanyMemberRole();
        companyMemberRole.setName(role);
        Company company = findCompany(principal.getName());
        companyMemberRole.setCompany(company);
        CompanyMemberRole savedRole = roleRepository.save(companyMemberRole);
        company.getMemberRoles().add(savedRole);

        //save
        companyRepository.save(company);
        return companyMemberRoleDTOMapper.apply(savedRole);
    }

    
    public void addRoleToMember(Integer id, Integer memberId, Principal principal) throws IdNotFoundException, MemberAlreadyHasRoleException {
        // check
        checkForAuthorization(principal.getName(), id, memberId);
        CompanyMemberRole role = findRoleById(id);
        if (role.getMembers().stream().anyMatch(member -> member.getId() == memberId)) throw new MemberAlreadyHasRoleException();

        // setup
        CompanyMember member = findMemberById(memberId);
        role.getMembers().add(member);
        member.getRoles().add(role);

        //save
        roleRepository.save(role);
        memberRepository.save(member);
    }

    
    public void deleteRole(Integer id, Principal principal) throws IdNotFoundException {
        // check
        checkForRoleAuthorization(principal.getName(), id);

        // action
        roleRepository.deleteById(id);
    }

    
    public void removeMemberRole(Integer id, Integer memberId, Principal principal) throws IdNotFoundException {
        //check
        checkForAuthorization(principal.getName(), id, memberId);

        //setup
        CompanyMemberRole role = findRoleById(id);
        CompanyMember member = findMemberById(memberId);
        if (member.getRoles().stream().noneMatch(current -> current.getId() == id)) throw new IdNotFoundException(NotFoundType.ROLE);
        member.getRoles().removeIf(current -> current.getId() == id);
        role.getMembers().removeIf(current -> current.getId() == id);

        //setup
        roleRepository.save(role);
        memberRepository.save(member);
    }

    private void checkForAuthorization(String name, Integer roleId, Integer memberId) throws IdNotFoundException {
        Company company = findCompany(name);
        if (company.getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
        if (company.getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new IdNotFoundException(NotFoundType.MEMBER);
    }

    private void checkForRoleAuthorization(String name, Integer roleId) throws IdNotFoundException {
        Company company = findCompany(name);
        if (company.getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
    }

    private Company findCompany(String name) throws IdNotFoundException {
        return companyRepository.findByName(name).orElseThrow(IdNotFoundException.get(NotFoundType.COMPANY));
    }

    private CompanyMember findMemberById(Integer id) throws IdNotFoundException {
        return memberRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }

    private CompanyMemberRole findRoleById(Integer id) throws IdNotFoundException {
        return roleRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }
}
