package pl.mfurman.memoro.mappers;

import pl.mfurman.memoro.dto.CardCollectionRequest;
import pl.mfurman.memoro.dto.CardCollectionResponse;
import pl.mfurman.memoro.entities.CardCollection;
import pl.mfurman.memoro.entities.User;

import static pl.mfurman.memoro.utils.StringConstants.CANNOT_BE_NULL;

public interface CardCollectionMapper {

  static CardCollectionResponse toResponse(final CardCollection collection) {
    if (collection == null) return null;

    return CardCollectionResponse.builder()
      .id(collection.getId())
      .name(collection.getName())
      .icon(collection.getIcon())
      .size(collection.getSize())
      .build();
  }

  static CardCollection toEntity(final CardCollection collection, final CardCollectionRequest request,
                                 final long size) {
    if (request == null) throw new RuntimeException(CANNOT_BE_NULL);

    return collection.toBuilder()
      .name(request.getName())
      .icon(request.getIcon())
      .size(size)
      .build();
  }

  static CardCollection toEntity(final CardCollection collection, final CardCollectionRequest request,
                                 final User user, final long size) {
    if (request == null) throw new RuntimeException(CANNOT_BE_NULL);

    return toEntity(collection, request, size).toBuilder()
      .user(user)
      .build();
  }
}
