package pengliu.me.backend.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    //管理员角色有所有permission
    ADMIN(Sets.newHashSet(ApplicationUserPermission.NAV_READ,
            ApplicationUserPermission.NAV_WRITE,
            ApplicationUserPermission.WIKI_READ,
            ApplicationUserPermission.WIKI_WRITE,
            ApplicationUserPermission.WIKI_CATEGORY_READ,
            ApplicationUserPermission.WIKI_CATEGORY_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return this.permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        // 这里把role对应的所有permission都包装成为SimpleGrantedAuthority对象
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        // 这里把上面permission所属的role也包装成SimpleGrantedAuthority对象，并返回到结果中
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

