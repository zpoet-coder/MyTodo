package cn.zpoet.mytodobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
public class User implements UserDetails { // 实现 Spring Security 的 UserDetails 接口
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    private Long uuid;

    @Column(unique = true, nullable = false) // 用户名必须唯一
    private String username;

    private String password;

    @Enumerated(EnumType.STRING) // 以字符串存储枚举值
    private Role role; // 角色（user / admin）

    private boolean enabled = true; // 账户是否启用

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + role.name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
