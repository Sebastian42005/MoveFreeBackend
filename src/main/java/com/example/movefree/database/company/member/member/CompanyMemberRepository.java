package com.example.movefree.database.company.member.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyMemberRepository extends JpaRepository<CompanyMember, UUID> {

}
