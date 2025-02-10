//package com.app.userservice.test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import com.app.userservice.model.UserDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
////
////@SpringBootTest
////@SpringJUnitConfig
////public class RedisSerializationTest {
////
////    @Autowired
////    private RedisTemplate<String, Object> redisTemplate;
////
////   
////
////    @Test
////    public void testUserDtoSerialization() {
////        // Crea un esempio di UserDto
////        UserDto user = new UserDto();
////        user.setId(1L);
////        user.setUsername("testuser");
////        user.setEmail("testuser@example.com");
////        user.setBirthday(LocalDate.of(1990, 1, 1));
////
////        // Salva l'oggetto in Redis
////        redisTemplate.opsForValue().set("user:1", user);
////
////        // Recupera l'oggetto
////        UserDto retrievedUser = (UserDto) redisTemplate.opsForValue().get("user:1");
////
////        // Verifica i dati
////        assertNotNull(retrievedUser);
////        assertEquals(user.getUsername(), retrievedUser.getUsername());
////        assertEquals("1990-01-01", retrievedUser.getBirthday().toString());
////    }
////
////    @Test
////    public void testListOfUserDtoSerialization() {
////        // Crea una lista di UserDto
////        UserDto user1 = new UserDto();
////        user1.setId(1L);
////        user1.setUsername("user1");
////
////        UserDto user2 = new UserDto();
////        user2.setId(2L);
////        user2.setUsername("user2");
////
////        List<UserDto> users = Arrays.asList(user1, user2);
////
////        // Salva la lista in Redis
////        redisTemplate.opsForValue().set("users", users);
////
////        // Recupera la lista
////        List<UserDto> retrievedUsers = (List<UserDto>) redisTemplate.opsForValue().get("users");
////
////        // Verifica i dati
////        assertNotNull(retrievedUsers);
////        assertEquals(2, retrievedUsers.size());
////        assertEquals("user1", retrievedUsers.get(0).getUsername());
////    }
////}
