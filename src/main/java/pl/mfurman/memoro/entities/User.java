package pl.mfurman.memoro.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static java.time.LocalDateTime.now;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity implements UserDetails, Serializable {

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private char[] password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @Column(nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String email;

  @Builder.Default
  private boolean active = false;

  @Builder.Default
  private LocalDateTime activationLinkExpirationDate = now().plusMinutes(20);

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptySet();
  }

  @Override
  public String getPassword() {
    return String.valueOf(password);
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
    return active;
  }
}
