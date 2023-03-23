package pl.mfurman.memoro.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mfurman.memoro.dto.CardRequest;
import pl.mfurman.memoro.dto.CardResponse;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.entities.CardCollection;
import pl.mfurman.memoro.mappers.CardMapper;
import pl.mfurman.memoro.repositories.CardRepository;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static pl.mfurman.memoro.mappers.CardMapper.toEntity;
import static pl.mfurman.memoro.utils.CommonUtil.getOrThrow;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;

@Service
@RequiredArgsConstructor
public class CardService {

  private final CardRepository repository;
  private final CardCollectionService collectionService;

  public List<CardResponse> getAll(final UUID collectionId) {
    return toStream(repository.findAllByCollectionId(collectionId))
      .map(CardMapper::toResponse)
      .collect(toList());
  }

  public Card getOneById(final UUID id) {
    return getOrThrow(repository.findById(id));
  }

  @Transactional
  public void createCard(final CardRequest request) {
    final CardCollection collection = collectionService.getOneById(request.getCollectionId());
    collectionService.incrementSize(collection);

    repository.save(
      toEntity(new Card(), request, collection)
    );
  }

  @Transactional
  public void updateCard(final UUID id, final CardRequest request) {
    final Card card = getOneById(id);
    final CardCollection collection = collectionService.getOneById(request.getCollectionId());

    if (!card.getCollection().getId().equals(collection.getId())) {
      collectionService.incrementSize(collection);
      collectionService.decrementSize(card.getCollection());
    }

    repository.save(
      toEntity(getOneById(id), request, collection)
    );
  }

  @Transactional
  public void deleteCard(final UUID id) {
    final Card card = getOneById(id);
    collectionService.decrementSize(card.getCollection());
    repository.delete(card);
  }

  @Transactional
  public void markAsFavorite(final UUID id) {
    final Card card = getOneById(id);
    card.setFavorite(!card.isFavorite());
    repository.save(card);
  }
}
