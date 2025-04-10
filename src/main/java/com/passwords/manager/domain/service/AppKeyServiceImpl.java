package com.passwords.manager.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.util.PasswordsUtils;
import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.model.AppKey;
import com.passwords.manager.infrastructure.db.repository.AppKeyRespository;

import jakarta.persistence.NoResultException;

@Component
public class AppKeyServiceImpl implements AppKeyService {

	private static final Logger logger = LogManager.getLogger(AppKeyServiceImpl.class);

	@Inject
	private AppKeyRespository appKeyRepository;

	@Override
	public boolean isAppKeyRegistered() {
		boolean isRegistered = false;
		try {
			List<AppKey> appKeys = appKeyRepository.findAll();

			if (appKeys != null && !appKeys.isEmpty())
				isRegistered = true;
		} catch (NoResultException e) {
			logger.warn("App Key not found in database");
		}
		logger.debug("App is registered: {}", isRegistered);
		return isRegistered;
	}

	@Override
	public void store(String applicationKey) {
		AppKey appKey = new AppKey();
		appKey.setapplicationKey(PasswordsUtils.hashText(applicationKey));
		appKeyRepository.store(appKey);
	}
}
