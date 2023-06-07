package pengliu.me.backend.demo.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    List<ApplicationUser> findByUserName(String userName);
}
