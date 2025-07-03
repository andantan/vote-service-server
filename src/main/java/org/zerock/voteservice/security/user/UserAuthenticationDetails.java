package org.zerock.voteservice.security.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.tool.hash.Sha256;

import java.util.ArrayList;
import java.util.Collection;

public class UserAuthenticationDetails implements UserDetails {
    private final UserEntity userEntity;
    @Getter
    private final String userHash;

    public UserAuthenticationDetails(
            UserEntity userEntity, String userHash
    ) {
        this.userEntity = userEntity;
        this.userHash = userEntity.isHashable() ? Sha256.sum(userEntity) : userHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> this.userEntity.getRole().getValue());

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return this.userEntity.getPassword();
    }

    public Integer getUid() {
        return this.userEntity.getUid();
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
        return true;
    }

}
