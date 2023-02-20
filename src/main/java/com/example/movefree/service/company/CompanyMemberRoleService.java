package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTOMapper;
import com.example.movefree.database.company.member.role.CompanyMemberRoleRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.port.company.CompanyMemberRolePort;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CompanyMemberRoleService implements CompanyMemberRolePort {

    final CompanyMemberRoleRepository roleRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final CompanyMemberRepository memberRepository;

    CompanyMemberRoleDTOMapper companyMemberRoleDTOMapper = new CompanyMemberRoleDTOMapper();

    public CompanyMemberRoleService(CompanyMemberRoleRepository roleRepository, CompanyRepository companyRepository, UserRepository userRepository, CompanyMemberRepository memberRepository) {
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CompanyMemberRoleDTO createRole(String role, Principal principal) throws IdNotFoundException, UserForbiddenException {
        // check
        User user = findUserByUsername(principal.getName());
        if (user.getCompany() == null) throw new UserForbiddenException();

        // setup
        CompanyMemberRole companyMemberRole = new CompanyMemberRole();
        companyMemberRole.setName(role);
        Company company = user.getCompany();
        companyMemberRole.setCompany(company);
        CompanyMemberRole savedRole = roleRepository.save(companyMemberRole);
        company.getMemberRoles().add(savedRole);

        //save
        companyRepository.save(company);
        return companyMemberRoleDTOMapper.apply(savedRole);
    }

    @Override
    public void addRoleToMember(int id, int memberId, Principal principal) throws IdNotFoundException, MemberAlreadyHasRoleException, UserForbiddenException {
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
    public void deleteRole(int id, Principal principal) throws UserForbiddenException, IdNotFoundException {
        // check
        checkForRoleAuthorization(principal.getName(), id);

        // action
        roleRepository.deleteById(id);
    }

    @Override
    public void removeMemberRole(int id, int memberId, Principal principal) throws IdNotFoundException, UserForbiddenException {
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

    private void checkForAuthorization(String username, int roleId, int memberId) throws IdNotFoundException, UserForbiddenException {
        User userDTO = findUserByUsername(username);
        if (userDTO.getCompany() == null) throw new UserForbiddenException();
        if (userDTO.getCompany().getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
        if (userDTO.getCompany().getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new IdNotFoundException(NotFoundType.MEMBER);
    }

    private void checkForRoleAuthorization(String username, int roleId) throws UserForbiddenException, IdNotFoundException {
        User user = findUserByUsername(username);
        if (user.getCompany() == null) throw new UserForbiddenException();
        if (user.getCompany().getMemberRoles().stream().noneMatch(role -> role.getId() == roleId)) throw new IdNotFoundException(NotFoundType.ROLE);
    }

    private User findUserByUsername(String username) throws IdNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }

    private CompanyMember findMemberById(int id) throws IdNotFoundException {
        return memberRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }

    private CompanyMemberRole findRoleById(int id) throws IdNotFoundException {
        return roleRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }
}
