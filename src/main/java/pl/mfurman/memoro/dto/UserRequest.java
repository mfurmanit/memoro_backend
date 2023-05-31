package pl.mfurman.memoro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static pl.mfurman.memoro.utils.StringConstants.PASSWORD_PATTERN;
import static pl.mfurman.memoro.utils.StringConstants.WEAK_PASSWORD;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class UserRequest implements Serializable {

  @NotBlank
  private String name;

  @NotBlank
  private String surname;

  @NotBlank
  private String username;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Pattern(regexp = PASSWORD_PATTERN, message = WEAK_PASSWORD)
  private String password;
}
