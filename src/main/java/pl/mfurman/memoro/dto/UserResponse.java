package pl.mfurman.memoro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class UserResponse implements Serializable {

  private UUID id;
  private String username;
  private String name;
  private String surname;
  private String fullName;
  private String email;
}
