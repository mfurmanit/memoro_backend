package pl.mfurman.memoro.mappers;

import pl.mfurman.memoro.dto.CardRequest;
import pl.mfurman.memoro.dto.CardResponse;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.entities.CardCollection;

import static pl.mfurman.memoro.utils.StringConstants.CANNOT_BE_NULL;

public interface CardMapper {

  static CardResponse toResponse(final Card card) {
    if (card == null) return null;

    return CardResponse.builder()
      .id(card.getId())
      .front(card.getFront())
      .back(card.getBack())
      .isFavorite(card.isFavorite())
      .collectionId(card.getCollection().getId())
      .build();
  }

  static Card toEntity(final Card card, final CardRequest request) {
    if (request == null) throw new RuntimeException(CANNOT_BE_NULL);

    return card.toBuilder()
      .front(request.getFront())
      .back(request.getBack())
      .build();
  }

  static Card toEntity(final Card card, final CardRequest request, final CardCollection collection) {
    if (request == null) throw new RuntimeException(CANNOT_BE_NULL);

    return toEntity(card, request).toBuilder()
      .collection(collection)
      .build();
  }
}
