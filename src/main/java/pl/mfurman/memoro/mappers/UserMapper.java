package pl.mfurman.memoro.mappers;

import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.entities.User;

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
}
