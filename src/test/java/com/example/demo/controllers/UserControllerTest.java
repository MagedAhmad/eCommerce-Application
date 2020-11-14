package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user() throws Exception{
        when(encoder.encode("password")).thenReturn("encryptedPass");
        CreateUserRequest u = new CreateUserRequest();
        u.setUsername("test");
        u.setPassword("password");
        u.setConfirmPassword("password");
        final ResponseEntity<User> response = userController.createUser(u);
        assertNotNull(response);
        assertEquals(0, response.getBody().getId());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody().getUsername(), u.getUsername());
    }

    @Test
    public void find_user_by_id() {
        when(encoder.encode("password")).thenReturn("encryptedPass");
        CreateUserRequest u = new CreateUserRequest();
        u.setUsername("test");
        u.setPassword("password");
        u.setConfirmPassword("password");
        ResponseEntity<User> user1 = userController.createUser(u);
        ResponseEntity<User> user = userController.findById(Long.valueOf(0));
        assertEquals(u.getUsername(), user1.getBody().getUsername());
    }

    @Test
    public void find_user_by_username() {
        when(encoder.encode("password")).thenReturn("encryptedPass");
        CreateUserRequest u = new CreateUserRequest();
        u.setUsername("test");
        u.setPassword("password");
        u.setConfirmPassword("password");
        ResponseEntity<User> user1 = userController.createUser(u);
        ResponseEntity<User> user = userController.findByUserName("test");
        assertEquals(u.getUsername(), user1.getBody().getUsername());
    }

}
