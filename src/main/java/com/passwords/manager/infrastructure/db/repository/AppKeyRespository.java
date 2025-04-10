package com.passwords.manager.infrastructure.db.repository;

import com.passwords.manager.core.cdi.annotation.Repository;
import com.passwords.manager.domain.model.AppKey;

@Repository
public interface AppKeyRespository extends H2Repository<AppKey> {

}
