package site.tushar.data_generator.controller;

import site.tushar.data_generator.entities.User;
import site.tushar.data_generator.utils.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Helper method to extract User from ApiResponse
    private User extractUser(ResponseEntity<ApiResponse<User>> response) {
        return response.getBody().getData();
    }

    @Test
    void createRandomUser_shouldReturnUser() {
        ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                "/api/users/random",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(extractUser(response)).isNotNull();
    }

    @Test
    void createAndGetUser_shouldReturnSameUser() {
        // Create user
        User newUser = new User(null, "Ravi", "ravi123@gmail.com", 28);
        ResponseEntity<ApiResponse<User>> createResp = restTemplate.exchange(
                "/api/users",
                HttpMethod.POST,
                new HttpEntity<>(newUser),
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        Long id = extractUser(createResp).getId();

        // Get user by ID
        ResponseEntity<ApiResponse<User>> getResp = restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        assertThat(extractUser(getResp).getName()).isEqualTo("Ravi");
    }

    @Test
    void updateUser_shouldChangeUserData() {
        // Create user
        User newUser = new User(null, "Aditi", "aditi@gmail.com", 22);
        ResponseEntity<ApiResponse<User>> createResp = restTemplate.exchange(
                "/api/users",
                HttpMethod.POST,
                new HttpEntity<>(newUser),
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        User created = extractUser(createResp);
        created.setName("Aditi Updated");

        // Update user
        ResponseEntity<ApiResponse<User>> updateResp = restTemplate.exchange(
                "/api/users/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(created),
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        assertThat(extractUser(updateResp).getName()).isEqualTo("Aditi Updated");
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        // Create user
        User newUser = new User(null, "Neha", "neha@gmail.com", 23);
        ResponseEntity<ApiResponse<User>> createResp = restTemplate.exchange(
                "/api/users",
                HttpMethod.POST,
                new HttpEntity<>(newUser),
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        Long id = extractUser(createResp).getId();

        // Delete user
        restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<Void>>() {
                });

        // Verify deletion
        ResponseEntity<ApiResponse<User>> getResp = restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<User>>() {
                });

        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
