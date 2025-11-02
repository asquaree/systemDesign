package com.example.monzobank.repository;

import com.example.monzobank.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void testFindByEmail_ExistingUser() {
        User user = userRepository.findByEmail("aakash@gmail.com");
        
        assertNotNull(user);
        assertEquals("aakash@gmail.com", user.getEmail());
        assertNotNull(user.getUrlLastAccessTime());
    }

    @Test
    void testFindByEmail_NonExistingUser() {
        User user = userRepository.findByEmail("nonexistent@example.com");
        
        assertNull(user);
    }

    @Test
    void testFindByEmail_NullEmail() {
        User user = userRepository.findByEmail(null);
        
        assertNull(user);
    }

    @Test
    void testMarkUrlsAsVisited_ValidUser() {
        String userEmail = "aakash@gmail.com";
        String parentUrl = "https://test.com/";
        String childUrl = "https://test.com/page";
        
        Boolean result = userRepository.markUrlsAsVisited(parentUrl, childUrl, userEmail);
        
        assertTrue(result);
        
        // Verify the URL was marked as visited
        User user = userRepository.findByEmail(userEmail);
        assertNotNull(user.getUrlLastAccessTime().get(parentUrl));
        assertNotNull(user.getUrlLastAccessTime().get(parentUrl).get(childUrl));
    }

    @Test
    void testMarkUrlsAsVisited_InvalidUser() {
        String userEmail = "nonexistent@example.com";
        String parentUrl = "https://test.com/";
        String childUrl = "https://test.com/page";
        
        Boolean result = userRepository.markUrlsAsVisited(parentUrl, childUrl, userEmail);
        
        assertFalse(result);
    }

    @Test
    void testMarkUrlsAsVisited_MultipleUrls() {
        String userEmail = "karan@example.com";
        String parentUrl = "https://example.com/";
        
        userRepository.markUrlsAsVisited(parentUrl, "https://example.com/page1", userEmail);
        userRepository.markUrlsAsVisited(parentUrl, "https://example.com/page2", userEmail);
        
        User user = userRepository.findByEmail(userEmail);
        assertEquals(2, user.getUrlLastAccessTime().get(parentUrl).size());
    }

    @Test
    void testTestUsersArePreloaded() {
        // Test user 1
        assertNotNull(userRepository.findByEmail("aakash@gmail.com"));
        
        // Test user 2
        assertNotNull(userRepository.findByEmail("karan@example.com"));
        
        // Test user 3
        assertNotNull(userRepository.findByEmail("aayush@test.com"));
    }
}
