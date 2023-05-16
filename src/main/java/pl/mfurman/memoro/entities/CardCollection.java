package pl.mfurman.memoro.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Audited
@NoArgsConstructor
@Table(name = "card_collections")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@ToString(onlyExplicitlyIncluded = true)
public class CardCollection extends BaseEntity {

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String icon;

  @Column(nullable = false)
  @Builder.Default
  private boolean shared = false;

  @Column(nullable = false)
  @Builder.Default
  private long size = 0;

  @NotNull
  @ManyToOne
  @NotAudited
  @JoinColumn(name = "id_user", referencedColumnName = "id")
  @EqualsAndHashCode.Exclude
  private User user;

  @Nullable
  @ManyToOne
  @NotAudited
  @JoinColumn(name = "id_parent", referencedColumnName = "id")
  @EqualsAndHashCode.Exclude
  private CardCollection parent;

  @NotAudited
  @Builder.Default
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Card> cards = new HashSet<>();
}
