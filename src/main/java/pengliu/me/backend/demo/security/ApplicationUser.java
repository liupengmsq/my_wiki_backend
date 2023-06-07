package pengliu.me.backend.demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "application_user")
@Component
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, length = 45)
    private String userName;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "account_expired", nullable = false)
    private Boolean isAccountExpired;

    @Column(name = "locked", nullable = false)
    private Boolean isAccountLocked;

    @Column(name = "credentials_expired", nullable = false)
    private Boolean isCredentialsExpired;

    @Column(name = "enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "role", nullable = true, length = 45)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.isEmpty(this.role)) {
            return null;
        }
        return ApplicationUserRole.valueOf(this.role).getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
