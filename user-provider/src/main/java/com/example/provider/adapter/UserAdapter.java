package com.example.provider.adapter;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.UserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageUtil;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.federated.UserFederatedStorageProvider;

import com.example.provider.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAdapter extends AbstractUserAdapter {

	private final UserDto user;
	private final String storageId;

	public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserDto user) {
		super(session, realm, model);
		this.user = user;
		this.storageId = new StorageId(model.getId(), String.valueOf(user.getId())).getId();
	}

	@Override
	public String getId() {
		return storageId;
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public String getLastName() {
		return user.getLastName();
	}

	@Override
	public String getFirstName() {
		return user.getFirstName();
	}

	@Override
	public String getFirstAttribute(String name) {
		List<String> values = getAttributes().get(name);
		return values != null && !values.isEmpty() ? values.get(0) : null;
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
		attributes.add(UserModel.USERNAME, getUsername());
		attributes.add(UserModel.EMAIL, getEmail());
		attributes.add(UserModel.FIRST_NAME, getFirstName());
		attributes.add(UserModel.LAST_NAME, getLastName());
		// attributes.add("Birthday",String.valueOf(user.getBirthday()));

		if (user.getBirthday() != null) {
			String formattedDate = user.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE); // "yyyy-MM-dd"
			attributes.add("Birthday", formattedDate);
		}

		attributes.add("Age", String.valueOf(user.getAge()));
		attributes.add("Nickname", user.getNickname());
		attributes.add("Telephone", user.getTelephone());
		attributes.add("ExternalId", String.valueOf(user.getId()));
		attributes.add("photo-profile", String.valueOf(user.getPhotoProfileId()));

		if (user.getRole() != null) {
			attributes.add("role", user.getRole());
		}
		if (user.getStatus() != null) {
			attributes.add("status", user.getStatus());
		}
		if (user.getQualification() != null) {
			attributes.add("qualification", user.getQualification());
		}
		return attributes;
	}

	@Override
	protected Set<RoleModel> getRoleMappingsInternal() {
		Set<RoleModel> roles = new HashSet<>();
		String role = user.getRole();

		if (realm != null && role != null) {
			// Verifica se il ruolo esiste
			RoleModel mappedRole = realm.getRole(role.toLowerCase());
			if (mappedRole == null) {
				// Il ruolo non esiste: crealo
				log.info("Role '{}' not found in realm '{}'. Creating new role.", role, realm.getName());
				mappedRole = realm.addRole(role.toLowerCase());
				if (mappedRole != null) {
					log.info("Successfully created role '{}' in realm '{}'.", role, realm.getName());
				} else {
					log.error("Failed to create role '{}' in realm '{}'.", role, realm.getName());
					return roles; // Ritorna un set vuoto se il ruolo non può essere creato
				}
			}
			roles.add(mappedRole);
		} else {
			log.warn("Realm or user role is null for user '{}'", user.getUsername());
		}
		return roles;
	}

	@Override
	public Stream<String> getAttributeStream(String name) {
		return getAttributes().containsKey(name) ? getAttributes().get(name).stream() : Stream.empty();
	}

	@Override
	public SubjectCredentialManager credentialManager() {
		return new UserCredentialManager(session, realm, this);
	}

	@Override
	public Stream<String> getRequiredActionsStream() {
		return getFederatedStorage().getRequiredActionsStream(realm, this.getId());
	}

	@Override
	public void addRequiredAction(String action) {
		getFederatedStorage().addRequiredAction(realm, this.getId(), action);
	}

	@Override
	public void removeRequiredAction(String action) {
		getFederatedStorage().removeRequiredAction(realm, this.getId(), action);
	}

	@Override
	public void addRequiredAction(RequiredAction action) {
		getFederatedStorage().addRequiredAction(realm, this.getId(), action.name());
	}

	@Override
	public void removeRequiredAction(RequiredAction action) {
		getFederatedStorage().removeRequiredAction(realm, this.getId(), action.name());
	}

	UserFederatedStorageProvider getFederatedStorage() {
		return UserStorageUtil.userFederatedStorage(session);
	}

	@Override
	public boolean isEmailVerified() {
		return true;
	}

	@Override
	public void setSingleAttribute(String name, String value) {
		// Salva o aggiorna l'attributo specificato
		switch (name) {
		case "nickname":
			user.setNickname(value);
			break;
		case "telephone":
			user.setTelephone(value);
			break;
		case "status":
			user.setStatus(value);
			break;
		// Aggiungi altre gestioni qui
		default:
			throw new UnsupportedOperationException("Attributo non supportato: " + name);
		}
	}

	@Override
	public void removeAttribute(String name) {
		setSingleAttribute(name, null);
	}

}