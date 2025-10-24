package site.tushar.data_generator.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import site.tushar.data_generator.entities.User;
import site.tushar.data_generator.repository.UserRepository;
import site.tushar.data_generator.services.UserService;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void generateRandomUser_shouldReturnSavedUser() {
        User user = new User(null, "Tushar", "tushar123@gmail.com", 25);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.generateRandomUser();

        assertThat(savedUser.getName()).isEqualTo("Tushar");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user1 = new User(1L, "A", "a@gmail.com", 20);
        User user2 = new User(2L, "B", "b@gmail.com", 22);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getAllUsers();
        assertThat(users).hasSize(2);
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        User existing = new User(1L, "Old", "old@gmail.com", 20);
        User updated = new User(null, "New", "new@gmail.com", 25);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        User result = userService.updateUser(1L, updated);
        assertThat(result.getName()).isEqualTo("New");
    }

    @Test
    void deleteUser_shouldCallRepository() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

}
