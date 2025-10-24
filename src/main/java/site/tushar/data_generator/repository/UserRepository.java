package site.tushar.data_generator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.tushar.data_generator.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Page<User> findAll(Pageable pageable);

	// JPQL query
	@Query("SELECT u FROM User u WHERE u.age > :age")
	List<User> findUsersOlderThan(@Param("age") int age);

	// Native SQL query
	@Query(value = "SELECT * FROM users u WHERE u.name LIKE %:name%", nativeQuery = true)
	List<User> findUsersByNameLike(@Param("name") String name);

}
