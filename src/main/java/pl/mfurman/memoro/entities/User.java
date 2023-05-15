package pl.mfurman.memoro.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.time.LocalDateTime.now;

// 1. shared - propertka w CardCollection
// 2. wyświetlanie (predicate) na podstawie propertki
// 3. kopiowanie kolekcji 1:1 i przypisasnie do innego użytkownika + parentId
// 4. lista udostepnionych przez innych i oznaczenie moich udostepnionych (inny kolor)
// 5. mogę zrobić dwa query - lista udostepnionych innych userów + moje + moje pobrane i na tej podstawie
// matchować odpowiednie propertki (mapowanie do parentId)

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

  @NotNull
  @NotAudited
  @ToString.Exclude
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  private Set<CardCollection> collections = new HashSet<>();

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
