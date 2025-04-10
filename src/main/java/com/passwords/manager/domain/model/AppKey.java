package com.passwords.manager.domain.model;

import com.passwords.manager.application.util.PasswordsUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_key")
public class AppKey {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String applicationKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getapplicationKey() {
		return applicationKey;
	}

	public void setapplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	@Override
	public String toString() {
		return "AppKey [id=" + id + ", applicationKey=" + PasswordsUtils.ofuscate(applicationKey) + "]";
	}
}
