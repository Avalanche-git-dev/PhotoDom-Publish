package com.app.userservice.test.junit;


//
//@SpringBootTest
//public class UserServiceTest {
//	
//	
//	
//	
//	
//	
//	
//	
//	@Autowired
//    private UserService userService;
//
//
//    //@Autowired
//	//private PasswordEncoder passwordEncoder;
//
//    
//    @Test
//    public void populateDatabaseWithUsers() {
//        IntStream.rangeClosed(1, 30).forEach(i -> {
//            User user = new User();
//            user.setUsername("momo" + i);
//            user.setPassword("momonee" + i);
//            user.setEmail("momo" + i + "@example.com");
//            user.setFirstName("FirstName" + i);
//            user.setLastName("LastName" + i);
//            user.setBirthday(LocalDate.of(1990, (i % 12) + 1, (i % 28) + 1)); // Varie date di nascita
//            user.setNickname("Avalanche" + i);
//            user.setTelephone(String.format("333%07d", i)); // Telefono univoco
//            user.setRole(i % 2 == 0 ? Role.USER : Role.ADMIN); // Alterna tra USER e ADMIN
//            user.setStatus(i % 3 == 0 ? UserStatus.BANNED : UserStatus.ACTIVE); // Alcuni utenti bannati
//
//            userService.createUser(user); // Salva l'utente nel database
//        });
//    }
//
//
//    //@Test
//public void testGetAllUsers() {
//    List<UserDto> users = userService.getAllUsers();
//    assertNotNull(users, "The user list should not be null");
//    assertTrue(users.size() > 0, "The user list should contain at least one user");
//    
//    // Log or print the users for debugging
//    users.forEach(user -> System.out.println(user.getUsername()));
//}
//    
//    //@Test
//    public void testGetAllBannedUsers() {
//        List<UserDto> bannedUsers = userService.getAllBannedUsers();
//        assertNotNull(bannedUsers, "La lista degli utenti bannati non dovrebbe essere null");
//        assertTrue(bannedUsers.size() > 0, "Ci dovrebbero essere utenti bannati");
//    }
//
//
////@Test
//public void testGetUsersWithPagination() {
//    int page = 0; // First page
//    int size = 10; // Number of users per page
//    List<UserDto> paginatedUsers = userService.getUsers(null, page, size);
//    
//    assertNotNull(paginatedUsers, "The paginated user list should not be null");
//    assertEquals(size, paginatedUsers.size(), "The paginated user list should contain the expected number of users");
//    
//    // Optional: Assert the content
//    paginatedUsers.forEach(user -> assertNotNull(user.getUsername(), "Each user should have a username"));
//}
//
//
////@Test
//public void testUpdateCredentials() {
//    Long userId = 1L; // Assumi che un utente con ID 1 esista
//    Credentials credentials = new Credentials("newPassword123","momonee1");
//
//    boolean result = userService.updateCredentials(userId, credentials);
//    assertTrue(result, "Le credenziali dovrebbero essere aggiornate con successo");
//
//    // Recupera l'utente aggiornato
//    User updatedUser = userService.getUserById(userId);
//
//    // Logga la password crittografata per il debug
//    String encodedPassword = updatedUser.getPassword();
//    System.out.println("Encoded password in DB: " + encodedPassword);
//
//    // Verifica che la password sia stata aggiornata
////    assertTrue(passwordEncoder.matches("newPassword123", updatedUser.getPassword()),
////            "La nuova password dovrebbe corrispondere a quella aggiornata");
//}
//
//
////@Test
//public void testUpdateUser() {
//    Long userId = 2L; // Assumi che un utente con ID 2 esista
//    UserDto userDetails = new UserDto();
//    userDetails.setUsername("updatedUsername");
//    userDetails.setEmail("updatedemail@example.com");
//    userDetails.setRole(Role.ADMIN);
//
//    User updatedUser = userService.updateUser(userId, userDetails);
//
//    assertNotNull(updatedUser, "L'utente aggiornato non dovrebbe essere null");
//    assertEquals("updatedUsername", updatedUser.getUsername(), "Lo username dovrebbe essere aggiornato");
//    assertEquals("updatedemail@example.com", updatedUser.getEmail(), "L'email dovrebbe essere aggiornata");
//    assertEquals(Role.ADMIN, updatedUser.getRole(), "Il ruolo dovrebbe essere aggiornato ad ADMIN");
//}
//
//
//
////@Test
//public void testAuthenticate() {
//    LoginRequest loginRequest = new LoginRequest();
//    loginRequest.setUsername("momo1");
//    loginRequest.setPassword("momonee1");
//
//    UserDto authenticatedUser = userService.authenticate(loginRequest);
//
//    assertNotNull(authenticatedUser, "L'utente autenticato non dovrebbe essere null");
//    assertEquals("momo1", authenticatedUser.getUsername(), "Lo username dovrebbe corrispondere a quello fornito");
//}
//
//
//
//
//}
