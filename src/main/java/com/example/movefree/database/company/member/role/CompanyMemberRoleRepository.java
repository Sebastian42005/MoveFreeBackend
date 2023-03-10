package com.example.movefree.database.company.member.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyMemberRoleRepository extends JpaRepository<CompanyMemberRole, UUID> {

}
