package com.example.movefree.port.company;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.MemberAlreadyHasRoleException;
import com.example.movefree.exception.UserForbiddenException;

import java.security.Principal;

public interface CompanyMemberRolePort {
    CompanyMemberRoleDTO createRole(String role, Principal principal) throws IdNotFoundException, UserForbiddenException;

    void addRoleToMember(Integer id, Integer memberId, Principal principal) throws IdNotFoundException, MemberAlreadyHasRoleException, UserForbiddenException;

    void deleteRole(Integer id, Principal principal) throws UserForbiddenException, IdNotFoundException;

    void removeMemberRole(Integer id, Integer memberId, Principal principal) throws IdNotFoundException, UserForbiddenException;
}
