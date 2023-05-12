package pl.mfurman.memoro.mappers;

import pl.mfurman.memoro.dto.UserRequest;
import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.entities.User;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import static java.text.MessageFormat.format;
import static pl.mfurman.memoro.utils.StringConstants.CANNOT_BE_NULL;

public interface UserMapper {

  static UserResponse toResponse(final User entity) {
    if (entity == null) return null;

    return UserResponse.builder()
      .id(entity.getId())
      .username(entity.getUsername())
      .name(entity.getName())
      .surname(entity.getSurname())
      .fullName(entity.getFullName())
      .email(entity.getEmail())
      .build();
  }

  static User toEntity(final UserRequest request, final char[] password) {
    if (request == null) throw new ApiException(CANNOT_BE_NULL);

    return User.builder()
      .name(request.getName())
      .surname(request.getSurname())
      .email(request.getEmail())
      .username(request.getUsername())
      .password(password)
      .fullName(format("{0} {1}", request.getName(), request.getSurname()))
      .build();
  }
}
