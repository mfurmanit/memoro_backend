package pl.mfurman.memoro.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cards")
@SuperBuilder(toBuilder = true)
@Audited(withModifiedFlag = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Card extends BaseEntity {

  @Column(nullable = false, columnDefinition = "text")
  private String front;

  @Column(nullable = false, columnDefinition = "text")
  private String back;

  @Builder.Default
  private boolean isFavorite = false;

  @Builder.Default
  private double eFactor = 2.5;

  @Builder.Default
  private int interval = 0;

  @Builder.Default
  private int repetition = 0;

  @Builder.Default
  private LocalDateTime nextReviewDate = now();

  @Builder.Default
  private boolean reviewed = false;

  @ManyToOne
  @JoinColumn(name = "id_card_collection", referencedColumnName = "id")
  private CardCollection collection;
}
