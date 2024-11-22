package com.photodom.keycloak.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import com.photodom.keycloak.mapping.Admin;
import com.photodom.keycloak.mapping.User;
//
//public class CustomUserAdapter extends AbstractUserAdapter {
//
//    private final User user;
//
//    public CustomUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, User user) {
//        super(session, realm, storageProviderModel);
//        this.user = user;
//        setUsername(user.getUsername());
//    }
//
//    @Override
//    public String getId() {
//        return String.valueOf(user.getId());
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername(); // Restituisce il nome utente dal modello User
//    }
//    @Override
//    public Map<String, List<String>> getAttributes() {
//        Map<String, List<String>> attributes = new HashMap<>();
//        attributes.put("id", Collections.singletonList(String.valueOf(user.getId())));
//        attributes.put("role", Collections.singletonList(user.getRole().name()));
//
//        if (user instanceof Admin) {
//            Admin admin = (Admin) user;
//            attributes.put("qualification", Collections.singletonList(admin.getQualification().name()));
//        }
//
//        return attributes;
//    }
//
//
//    @Override
//    public SubjectCredentialManager credentialManager() {
//        return new SubjectCredentialManager() {
//            @Override
//            public boolean isConfiguredFor(String type) {
//                return PasswordCredentialModel.TYPE.equals(type);
//            }
//
//            @Override
//            public boolean isValid(List<CredentialInput> inputs) {
//                return inputs.stream()
//                             .anyMatch(input -> input.getType().equals(PasswordCredentialModel.TYPE));
//            }
//
//			@Override
//			public boolean updateCredential(CredentialInput input) {
//				return false;
//			}
//
//			@Override
//			public void updateStoredCredential(CredentialModel cred) {
//				
//			}
//
//			@Override
//			public CredentialModel createStoredCredential(CredentialModel cred) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public boolean removeStoredCredentialById(String id) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public CredentialModel getStoredCredentialById(String id) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public Stream<CredentialModel> getStoredCredentialsStream() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public Stream<CredentialModel> getStoredCredentialsByTypeStream(String type) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public CredentialModel getStoredCredentialByNameAndType(String name, String type) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public boolean moveStoredCredentialTo(String id, String newPreviousCredentialId) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public void updateCredentialLabel(String credentialId, String credentialLabel) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void disableCredentialType(String credentialType) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public Stream<String> getDisableableCredentialTypesStream() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public boolean isConfiguredLocally(String type) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public Stream<String> getConfiguredUserStorageCredentialTypesStream() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public CredentialModel createCredentialThroughProvider(CredentialModel model) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//        };
//    }
//}

public class CustomUserAdapter extends AbstractUserAdapter {

	private final User user;

	public CustomUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel,
			User user) {
		super(session, realm, storageProviderModel);
		this.user = user;
		setUsername(user.getUsername());
	}

	@Override
	public String getId() {
		return String.valueOf(user.getId());
	}

	@Override
	public String getUsername() {
		return user.getUsername(); // Restituisce il nome utente dal modello User
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("id", Collections.singletonList(String.valueOf(user.getId())));
		attributes.put("role", Collections.singletonList(user.getRole().name()));

		if (user instanceof Admin) {
			Admin admin = (Admin) user;
			attributes.put("qualification", Collections.singletonList(admin.getQualification().name()));
		}

		return attributes;
	}

	@Override
	public SubjectCredentialManager credentialManager() {
		// Se Keycloak richiede un CredentialManager, restituiamo un'implementazione di
		// default
		return new SubjectCredentialManager() {
			@Override
			public boolean isConfiguredFor(String type) {
				return PasswordCredentialModel.TYPE.equals(type);
			}

			@Override
			public boolean isValid(List<CredentialInput> inputs) {
				// Il controllo viene gestito nel CustomUserStorageProvider
				return false; // Restituiamo false per indicare che il controllo è esterno
			}

			@Override
			public boolean updateCredential(CredentialInput input) {
				// Non gestiamo aggiornamenti lato Keycloak
				return false;
			}

			@Override
			public void updateStoredCredential(CredentialModel cred) {
				// Non implementato
			}

			@Override
			public CredentialModel createStoredCredential(CredentialModel cred) {
				// Non supportato
				return null;
			}

			@Override
			public boolean removeStoredCredentialById(String id) {
				// Non supportato
				return false;
			}

			@Override
			public CredentialModel getStoredCredentialById(String id) {
				// Non supportato
				return null;
			}

			@Override
			public Stream<CredentialModel> getStoredCredentialsStream() {
				// Non implementato
				return Stream.empty();
			}

			@Override
			public Stream<CredentialModel> getStoredCredentialsByTypeStream(String type) {
				// Non implementato
				return Stream.empty();
			}

			@Override
			public CredentialModel getStoredCredentialByNameAndType(String name, String type) {
				// Non implementato
				return null;
			}

			@Override
			public boolean moveStoredCredentialTo(String id, String newPreviousCredentialId) {
				// Non implementato
				return false;
			}

			@Override
			public void updateCredentialLabel(String credentialId, String credentialLabel) {
				// Non implementato
			}

			@Override
			public void disableCredentialType(String credentialType) {
				// Non implementato
			}

			@Override
			public Stream<String> getDisableableCredentialTypesStream() {
				// Non implementato
				return Stream.empty();
			}

			@Override
			public boolean isConfiguredLocally(String type) {
				// Non implementato
				return false;
			}

			@Override
			public Stream<String> getConfiguredUserStorageCredentialTypesStream() {
				// Non implementato
				return Stream.empty();
			}

			@Override
			public CredentialModel createCredentialThroughProvider(CredentialModel model) {
				// Non implementato
				return null;
			}
		};
	}
}
