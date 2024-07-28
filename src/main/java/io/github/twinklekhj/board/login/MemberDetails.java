package io.github.twinklekhj.board.login;

import io.github.twinklekhj.board.dao.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class MemberDetails implements UserDetails {
    private Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(Role.values())
                .filter(r -> r.ordinal() <= member.getRole().ordinal())
                .forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));

        return authorities;
    }

    public List<Role> getRoles() {
        return Arrays.stream(Role.values()).filter(r -> r.ordinal() <= member.getRole().ordinal()).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getFailureCnt() < 5;
    }

    @Override
    public boolean isEnabled() {
        return member.isEnabled();
    }
}
