package com.example.provider;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.keycloak.utils.StringUtil;

import com.example.provider.client.UserServiceClientHttp;

public class UserServiceStorageProviderFactory implements UserStorageProviderFactory<UserServiceStorageProvider> {

	public static final String PROVIDER_ID = "user-service-storage-provider";

	public static final String BASE_URL = "baseUrl";

	public static final String AUTH_USERNAME = "AuthUsername";

	public static final String AUTH_PASSWORD = "AuthPassword";

	@Override
	public UserServiceStorageProvider create(KeycloakSession session, ComponentModel model) {
		UserServiceClientHttp client = new UserServiceClientHttp(session, model);
		return new UserServiceStorageProvider(session, model, client);
	}

	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	@Override
	public String getHelpText() {
		return "User Service Storage Provider";
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return ProviderConfigurationBuilder.create()
				.property(BASE_URL, "Base URL", "The base URL of the external user service API",
						ProviderConfigProperty.STRING_TYPE, "", null)
				.property(AUTH_USERNAME, "Base Username", "Username for BasicAuth at the API",
						ProviderConfigProperty.STRING_TYPE, "", null)
				.property(AUTH_PASSWORD, "Base Password", "Password for BasicAuth at the API",
						ProviderConfigProperty.PASSWORD, "", null)
				.build();
	}

	@Override
	public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config)
			throws ComponentValidationException {
		if (StringUtil.isBlank(config.get(BASE_URL)) || StringUtil.isBlank(config.get(AUTH_USERNAME))
				|| StringUtil.isBlank(config.get(AUTH_PASSWORD))) {
			throw new ComponentValidationException("The base URL is required for the User Service Storage Provider.");
		}
	}

	public UserServiceStorageProviderFactory() {
	}

}
