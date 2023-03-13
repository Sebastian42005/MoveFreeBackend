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
import com.example.movefree.port.company.CompanyMemberRolePort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public class CompanyMemberRoleService implements CompanyMemberRolePort {

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

    @Override
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

    @Override
    public void addRoleToMember(UUID id, UUID memberId, Principal principal) throws IdNotFoundException, MemberAlreadyHasRoleException {
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

    @Override
    public void deleteRole(UUID id, Principal principal) throws IdNotFoundException {
        // check
        checkForRoleAuthorization(principal.getName(), id);

        // action
        roleRepository.deleteById(id);
    }

    @Override
    public void removeMemberRole(UUID id, UUID memberId, Principal principal) throws IdNotFoundException {
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

    private void checkForAuthorization(String name, UUID roleId, UUID memberId) throws IdNotFoundException {
        Company company = findCompany(name);
        if (company.getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
        if (company.getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new IdNotFoundException(NotFoundType.MEMBER);
    }

    private void checkForRoleAuthorization(String name, UUID roleId) throws IdNotFoundException {
        Company company = findCompany(name);
        if (company.getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
    }

    private Company findCompany(String name) throws IdNotFoundException {
        return companyRepository.findByName(name).orElseThrow(IdNotFoundException.get(NotFoundType.COMPANY));
    }

    private CompanyMember findMemberById(UUID id) throws IdNotFoundException {
        return memberRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }

    private CompanyMemberRole findRoleById(UUID id) throws IdNotFoundException {
        return roleRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }
}
