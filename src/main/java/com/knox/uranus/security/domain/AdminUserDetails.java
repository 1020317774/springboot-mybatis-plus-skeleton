package com.knox.uranus.security.domain;

import com.knox.uranus.modules.ums.model.UmsAdminUser;
import com.knox.uranus.modules.ums.model.UmsResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 *
 * @author knox
 * @date 2020/08/26
 */
public class AdminUserDetails implements UserDetails {
    /**
     * 登录用户对象
     */
    private final UmsAdminUser umsAdminUser;
    /**
     * 可访问资源列表
     */
    private final List<UmsResource> resourceList;

    public AdminUserDetails(UmsAdminUser umsAdminUser, List<UmsResource> resourceList) {
        this.umsAdminUser = umsAdminUser;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getId() + ":" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdminUser.getUsername();
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

    @Override
    public boolean isEnabled() {
        return umsAdminUser.getStatus();
    }
}
