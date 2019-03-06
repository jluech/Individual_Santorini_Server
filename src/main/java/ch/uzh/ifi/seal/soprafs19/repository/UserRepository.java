package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	User findByFirstname(String firstname);
	User findByLastname(String lastname);
	User findByUsername(String username);
	User findByToken(String token);
	User findById(long id);
}
