package com.zpsm.rpgsessionassisstant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Player", schema = "zpsm_projekt")
public class Player implements UserDetails {
    @Serial
    private static final long serialVersionUID = 2203748061513131558L;
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "player_seq")
    @SequenceGenerator(
        name = "player_seq",
        schema = "zpsm_projekt",
        initialValue = 10)
    @Column(name = "player_id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "login", nullable = false, length = 30, unique = true)
    private String login;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "player")
    private Set<Gamemaster> gamemasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Character> characters = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_PLAYER"));
    }

    @Override
    public String getUsername() {
        return login;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Player player = (Player) o;
        return getId() != null && Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
