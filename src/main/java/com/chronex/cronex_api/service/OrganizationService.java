package com.chronex.cronex_api.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.entity.Organization;
import com.chronex.cronex_api.entity.OrganizationMember;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.enums.OrganizationRole;
import com.chronex.cronex_api.repository.OrganizationMemberRepository;
import com.chronex.cronex_api.repository.OrganizationRepository;

@Service
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationService(
        OrganizationRepository organizationRepository, 
        OrganizationMemberRepository organizationMemberRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationMemberRepository = organizationMemberRepository;
    }

    /**
     * Cria a organização pessoal de um usuário recém-cadastrado e o vincula
     * como owner. método chamado no momento do cadastro do usuário.
     * 
     * @param user O usuário recém-cadastrado.
     * @return A organização pessoal criada.
     */
    public Organization createPersonalOrganization(User user) {
        Organization organization = new Organization();
        organization.setName("Workspace de " + user.getName());
        organization.setOwnerId(user.getId());
        organization.setCreatedAt(Instant.now());
        organizationRepository.save(organization);

        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(organization.getId());
        member.setUserId(user.getId());
        member.setRole(OrganizationRole.OWNER);
        member.setJoinedAt(Instant.now());
        organizationMemberRepository.save(member);

        return organization;
    }
}
