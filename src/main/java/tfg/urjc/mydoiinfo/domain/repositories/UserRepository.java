package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findFirstByUsername(String username);
}
