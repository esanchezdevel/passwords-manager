package com.passwords.manager.infrastructure.db.repository;

import com.passwords.manager.core.cdi.annotation.Repository;
import com.passwords.manager.domain.model.Credential;

@Repository
public interface CredentialRepository extends H2Repository<Credential> {

}
