package site.tushar.data_generator.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.tushar.data_generator.entities.User;
import site.tushar.data_generator.services.UserService;
import site.tushar.data_generator.utils.ApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create random user
    @PostMapping("/random")
    public ResponseEntity<ApiResponse<User>> createRandomUser() {
        User user = userService.generateRandomUser();
        return ResponseEntity.ok(ApiResponse.success("Random user created successfully", user));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("No users found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Fetched users successfully", users));
    }

    // Get user by ID
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User fetched successfully", user)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(ApiResponse.error("User not found with id " + id)));
    }

    // Create user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error("Email already exists: " + user.getEmail()));
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("User created successfully", savedUser));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("User not found with id " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("User not found with id " + id));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<User>>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<User> usersPage = userService.getUsersPaginated(pageable);

        if (usersPage.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("No users found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Fetched paginated users successfully", usersPage));
    }
}
