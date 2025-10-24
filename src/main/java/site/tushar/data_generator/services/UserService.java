package site.tushar.data_generator.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.tushar.data_generator.entities.User;
import site.tushar.data_generator.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Random random = new Random();
    private final List<String> names = List.of("Tushar", "Sagar", "Aditi", "Ravi", "Neha", "Priya");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User generateRandomUser() {
        User user;
        do {
            String name = names.get(random.nextInt(names.size()));
            String email = name.toLowerCase() + random.nextInt(1000) + "@gmail.com";
            int age = 18 + random.nextInt(30);
            user = new User(null, name, email, age);
        } while (userRepository.findByEmail(user.getEmail()).isPresent());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUser) {
        return userRepository.findById(id).map(u -> {
            u.setName(newUser.getName());
            u.setEmail(newUser.getEmail());
            u.setAge(newUser.getAge());
            return userRepository.save(u);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> getUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}
