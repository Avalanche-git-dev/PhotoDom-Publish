package com.app.userservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.userservice.entity.Admin;
import com.app.userservice.entity.Qualification;
import com.app.userservice.entity.Role;
import com.app.userservice.entity.User;
import com.app.userservice.entity.UserStatus;
import com.app.userservice.exception.AdminAlreadyExistsException;
import com.app.userservice.exception.DuplicateEmailException;
import com.app.userservice.exception.DuplicateFieldException;
import com.app.userservice.exception.DuplicateUsernameException;
import com.app.userservice.exception.InvalidFieldException;
import com.app.userservice.exception.NotAuthorizedException;
import com.app.userservice.exception.UserException;
import com.app.userservice.exception.UserNotFoundException;
import com.app.userservice.model.Credentials;
import com.app.userservice.model.LoginRequest;
import com.app.userservice.model.ProfileView;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserFilter;
import com.app.userservice.model.UserMapper;
import com.app.userservice.repository.UserRepository;
import com.app.userservice.socket.UserWebSocketHandler;
import com.app.userservice.utility.UserContext;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // Per codificare e verificare le password

	@Autowired
	private UserContext currentUser;

	@Autowired
	private UserWebSocketHandler userWebSocketHandler;

	public String getNicknameById(Long id) {
		return userRepository.findById(id).map(User::getNickname)
				.orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
	}

	public List<String> getNicknamesByIds(List<Long> ids) {
		List<String> nicknames = userRepository.findAllById(ids).stream().map(User::getNickname)
				.collect(Collectors.toList());

		if (nicknames.isEmpty()) {
			throw new UserNotFoundException("No users found for the provided IDs");
		}

		return nicknames;
	}

	public List<UserDto> getAllUsers() {
		List<UserDto> users = userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
		return users;
	}

	public User getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		return user;
	}

	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}

	public User authenticate(LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
				() -> new UserNotFoundException("User not found with username: " + loginRequest.getUsername()));

		if (user.getStatus() == UserStatus.BANNED) {
			throw new InvalidFieldException("User is banned and cannot authenticate.");
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new InvalidFieldException("Invalid username or password.");
		}

		return user;
	}

	public void banUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if(user.getStatus().equals(UserStatus.BANNED))
		{
			throw new UserException("User is already banned");
		}
		user.setStatus(UserStatus.BANNED);
		userRepository.save(user);
		String message = "User banned: " + id;
		userWebSocketHandler.notifyUserChange(message);
	}

	public void unbanUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if(!user.getStatus().equals(UserStatus.BANNED))
		{
			throw new UserException("User is already active and not banned");
		}
		user.setStatus(UserStatus.ACTIVE);
		userRepository.save(user);
		String message = "User unbanned: " + id;
		userWebSocketHandler.notifyUserChange(message);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	}

	public List<UserDto> getAllBannedUsers() {
		return userRepository.findByStatus(UserStatus.BANNED).stream().map(UserMapper::toUserDto)
				.collect(Collectors.toList());
	}

	public List<UserDto> getAllInactiveUsers() {
		return userRepository.findByStatus(UserStatus.INACTIVE).stream().map(UserMapper::toUserDto)
				.collect(Collectors.toList());
	}

	public int getTotalUserCount() {
		return (int) userRepository.count(); // Conteggio totale degli utenti
	}

	public User createUser(User user) {

		if (user == null) {
			throw new UserException("You have to provide all of the information needed for the registration");
		}

		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new DuplicateUsernameException("Username already exists: " + user.getUsername());
		}

		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new DuplicateEmailException("Email already exists: " + user.getEmail());
		}

		if (userRepository.findByNickname(user.getNickname()).isPresent()) {
			throw new DuplicateUsernameException("Nickname already exists: " + user.getNickname());
		}

		if (user.getAge() < 15) {
			throw new InvalidFieldException("User must be at least 15 years old.");
		}
		if (userRepository.findByTelephone(user.getTelephone()).isPresent()) {
			throw new DuplicateFieldException(
					"Your telephone number seems to be already registered, check it out you have made a mistake : "
							+ user.getTelephone());
		}

		if (!user.getTelephone().matches("\\d{10}")) {
			throw new InvalidFieldException(
					"Telephone number cannot be left blank, please insert it correctly formatted by 10 digit. ");
		}

		if (!isValidPassword(user.getPassword())) {
			throw new InvalidFieldException(
					"Invalid password. It must have at least 6 characters and include at least one number.");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStatus(UserStatus.ACTIVE);
		user.setRole(Role.USER);
		if (user.getPhotoProfileId() != null) {
			user.setPhotoProfileId(user.getPhotoProfileId());
		}

//		// Salva nella cache
//		String cacheKey = "user:" + user.getId();
//		cachy.opsForValue().set(cacheKey, UserMapper.toUserDto(user), Duration.ofMinutes(5));
		return userRepository.save(user);
	}

	private boolean isValidPassword(String password) {
		if (password == null) {
			throw new InvalidFieldException("Password cannot be null");
		}
		return password.length() >= 6 && password.matches(".*\\d.*");
	}

	public boolean updateCredentials(/* Long id, */ Credentials credentials) {
		User user = userRepository.findById(currentUser.getCurrentUserId()).orElseThrow(
				() -> new UserNotFoundException("User not found with id: " + currentUser.getCurrentUserId()));

		if (currentUser.getCurrentUserId() != user.getId()) {
			throw new NotAuthorizedException("You cannot modify other's credential. ");
		}

		// Verifica della password attuale
		if (!passwordEncoder.matches(credentials.getOldPassword(), user.getPassword())) {
			throw new InvalidFieldException("You have to provide your correct current password.");
		}

		// Verifica che la nuova password sia diversa da quella attuale
		if (passwordEncoder.matches(credentials.getNewPassword(), user.getPassword())) {
			throw new InvalidFieldException("You have to provide a new password, please switch the currently in use.");
		}

		// Verifica che la nuova password rispetti i criteri di validità
		if (!isValidPassword(credentials.getNewPassword())) {
			throw new InvalidFieldException(
					"Invalid password. It must have at least 6 characters and include at least one number.");
		}

		// Codifica e aggiorna la password

		user.setPassword(passwordEncoder.encode(credentials.getNewPassword()));
		userRepository.save(user);

		userWebSocketHandler.notifyUserUpdate("User credentials updated: " + user.getId());
		return true;
	}

	// Metodo per promuovere un utente a ADMIN
	private Admin promoteToAdmin(User user) {
		Admin admin = new Admin();

		// Copia i campi comuni da User ad Admin
		admin.setId(user.getId()); // Mantieni lo stesso ID
		admin.setUsername(user.getUsername());
		admin.setPassword(user.getPassword());
		admin.setEmail(user.getEmail());
		admin.setFirstName(user.getFirstName());
		admin.setLastName(user.getLastName());
		admin.setBirthday(user.getBirthday());
		admin.setNickname(user.getNickname());
		admin.setTelephone(user.getTelephone());
		admin.setRole(Role.ADMIN);
		admin.setStatus(UserStatus.ACTIVE);

		// Aggiungi attributi specifici di Admin
		admin.setQualification(Qualification.ADMIN);

		return admin;
	}

	public void nominateAdmin(Long id) {
		// Controlla se l'utente esiste
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		// Controlla se è già un ADMIN
		if (user.getRole() == Role.ADMIN) {
			throw new AdminAlreadyExistsException("User is already an ADMIN");
		}

		// Promuovi l'utente
		user.setRole(Role.ADMIN);
		user.setStatus(UserStatus.ACTIVE);

		if (!(user instanceof Admin)) {

			userRepository.delete(user);// Rimuovi il vecchio record
			Admin admin = promoteToAdmin(user);
			// Promozione a Admin (creazione nuova istanza) in questo ordine dovrebbe andare
			userRepository.save(admin); // Salva l'istanza di Admin
		} else {
			// Se è già Admin, aggiorna i dati
			Admin admin = (Admin) user;
			admin.setQualification(Qualification.ADMIN);
			userRepository.save(admin);
			String message = "User promoted to Admin: " + id;
			userWebSocketHandler.notifyUserChange(message);
		}
	}

	public User updateUser(/* Long id, */ UserDto userDetails) {
		User user = userRepository.findById(currentUser.getCurrentUserId()).orElseThrow(
				() -> new UserNotFoundException("User not found with id: " + currentUser.getCurrentUserId()));

		if (currentUser.getCurrentUserId() != user.getId()) {
			throw new NotAuthorizedException(
					"You cannot modify other's profile . You will be contacted by ADMIN for your violation. ");
		}

		if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
			if (user.getUsername().equals(userDetails.getUsername())
					&& userRepository.findByUsername(userDetails.getUsername()).isPresent()) {
				throw new DuplicateUsernameException("Username already exists: " + userDetails.getUsername());
			}
			user.setUsername(userDetails.getUsername());
		}

		if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
			if (user.getEmail().equals(userDetails.getEmail())
					&& userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
				throw new DuplicateEmailException("Email already exists: " + userDetails.getEmail());
			}
			user.setEmail(userDetails.getEmail());
		}

		if (userDetails.getRole() != null) {
			user.setRole(userDetails.getRole());
		}

		// Aggiorna firstName
		if (userDetails.getFirstName() != null && !userDetails.getFirstName().isEmpty()) {
			user.setFirstName(userDetails.getFirstName());
		}

		// Aggiorna lastName
		if (userDetails.getLastName() != null && !userDetails.getLastName().isEmpty()) {
			user.setLastName(userDetails.getLastName());
		}

		// Aggiorna nickname
		if (userDetails.getNickname() != null && !userDetails.getNickname().isEmpty()) {
			if (!user.getNickname().equals(userDetails.getNickname())
					&& userRepository.findByNickname(userDetails.getNickname()).isPresent()) {
				throw new DuplicateFieldException("Nickname already exists: " + userDetails.getNickname());
			}
			user.setNickname(userDetails.getNickname());
		}

		// Aggiorna telephone
		if (userDetails.getTelephone() != null && !userDetails.getTelephone().isEmpty()) {
			if (!userDetails.getTelephone().matches("\\d{10}")) {
				throw new InvalidFieldException("Telephone number must be 10 digits.");
			}
			if (!user.getTelephone().equals(userDetails.getTelephone())
					&& userRepository.findByTelephone(userDetails.getTelephone()).isPresent()) {
				throw new DuplicateFieldException("Telephone number already exists: " + userDetails.getTelephone());
			}
			user.setTelephone(userDetails.getTelephone());
		}

		if (userDetails.getPhotoProfileId() != null) {
			user.setPhotoProfileId(userDetails.getPhotoProfileId());
		}

		userWebSocketHandler.notifyUserUpdate("User profile updated: " + user.getId());

		return userRepository.save(user);
	}

	public Page<UserDto> getUsers(String search, int first, int max) {
		Pageable pageable = PageRequest.of(first / max, max);

		if (search == null || search.trim().isEmpty()) {
			// Se non ci sono criteri di ricerca, restituisci tutti gli utenti (paginati)
			return userRepository.findAll(pageable).map(UserMapper::toUserDto);
		}

		// Cerca per username o email con criteri di ricerca
		return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable)
				.map(UserMapper::toUserDto);
	}

	public ProfileView getProfile(Long id) {
		User user = userRepository.findById(id).get();
		if (user == null) {
			throw new UserNotFoundException("User Not Found by Id : " + id);
		}
		ProfileView profile = new ProfileView();
		profile.setId(user.getId());
		profile.setAge(Long.valueOf(user.getAge()));
		profile.setBirthday(user.getBirthday());
		profile.setFirstName(user.getFirstName());
		profile.setLastName(user.getLastName());
		profile.setNickname(user.getNickname());
		profile.setPhotoProfileId(user.getPhotoProfileId());
		profile.setTelephone(user.getTelephone());

		return profile;
	}
	//Filtro dinamico 
	 public List<ProfileView> findProfileViewsByFilter(UserFilter filter) {
	        List<User> users = userRepository.findUsersByFilter(
	            filter.getNickname(),
	            filter.getFirstName(),
	            filter.getLastName()
	        );

	        
	        return users.stream()
	                    .map(UserMapper::toProfileView)
	                    .collect(Collectors.toList());
	    }

}
