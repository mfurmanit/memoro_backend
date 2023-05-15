package pl.mfurman.memoro.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mfurman.memoro.dto.CardCollectionRequest;
import pl.mfurman.memoro.dto.CardCollectionResponse;
import pl.mfurman.memoro.dto.CardCollectionSharedResponse;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.entities.CardCollection;
import pl.mfurman.memoro.mappers.CardCollectionMapper;
import pl.mfurman.memoro.mappers.CardMapper;
import pl.mfurman.memoro.repositories.CardCollectionRepository;
import pl.mfurman.memoro.repositories.CardRepository;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static pl.mfurman.memoro.criteria.CardCollectionCriteria.collectionPredicate;
import static pl.mfurman.memoro.criteria.CardCollectionCriteria.sharedPredicate;
import static pl.mfurman.memoro.mappers.CardCollectionMapper.copyEntity;
import static pl.mfurman.memoro.mappers.CardCollectionMapper.toEntity;
import static pl.mfurman.memoro.utils.CommonUtil.getOrThrow;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;
import static pl.mfurman.memoro.utils.StringConstants.CANNOT_SAVE;
import static pl.mfurman.memoro.utils.StringConstants.NOT_SHARED;
import static pl.mfurman.memoro.utils.StringConstants.WRONG_CONTEXT;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;
import static pl.mfurman.memoro.utils.UserUtil.getPrincipal;

@Service
@RequiredArgsConstructor
public class CardCollectionService {

  private final UserService userService;
  private final CardRepository cardRepository;
  private final CardCollectionRepository repository;

  public Page<CardCollectionResponse> getAll(final Pageable pageable, @Nullable final String value) {
    return repository.findAll(collectionPredicate(value), pageable).map(CardCollectionMapper::toResponse);
  }

  public CardCollection getOneById(final UUID id) {
    final CardCollection collection = getOrThrow(repository.findById(id));

    if (!getLoggedUserId().equals(collection.getUser().getId()) && !collection.isShared())
      throw new ApiException(WRONG_CONTEXT);

    return collection;
  }

  public CardCollection getOneSharedById(final UUID id) {
    final CardCollection collection = getOrThrow(repository.findByIdAndSharedIsTrue(id));

    if (!collection.isShared()) throw new ApiException(NOT_SHARED);
    if (getLoggedUserId().equals(collection.getUser().getId())) throw new ApiException(CANNOT_SAVE);

    return collection;
  }

  public CardCollection getMySharedById(final UUID id) {
    final CardCollection collection = getOrThrow(repository.findByIdAndSharedIsTrue(id));

    if (!collection.isShared()) throw new ApiException(NOT_SHARED);
    if (!getLoggedUserId().equals(collection.getUser().getId())) throw new ApiException(WRONG_CONTEXT);

    return collection;
  }

  public List<CardCollectionSharedResponse> getAllShared() {
    final List<CardCollection> all = repository.findAll(sharedPredicate(false));
    final List<CardCollection> my = repository.findAll(sharedPredicate(true));

    final Set<UUID> myIdentifiers = toStream(my).map(CardCollection::getId).collect(toSet());

    return toStream(all)
      .map(collection -> CardCollectionMapper.toSharedResponse(
        collection,
        myIdentifiers.contains(collection.getId())
      )).collect(Collectors.toList());
  }

  @Transactional
  public void createCollection(final CardCollectionRequest request) {
    repository.save(
      toEntity(new CardCollection(), request, getPrincipal(), 0)
    );
  }

  @Transactional
  public void updateCollection(final UUID id, final CardCollectionRequest request) {
    final CardCollection collection = getOneById(id);

    repository.save(
      toEntity(collection, request, collection.getSize())
    );
  }

  @Transactional
  public void deleteCollection(final UUID id) {
    repository.deleteById(id);
  }

  @Transactional
  public void incrementSize(final CardCollection collection) {
    collection.setSize(collection.getSize() + 1);
    repository.save(collection);
  }

  @Transactional
  public void decrementSize(final CardCollection collection) {
    collection.setSize(collection.getSize() - 1);
    repository.save(collection);
  }

  @Transactional
  public void stopSharingCollection(final UUID id) {
    final CardCollection collection = getMySharedById(id);
    collection.setShared(false);
    repository.save(collection);
  }

  @Transactional
  public void shareCollection(final UUID id) {
    final CardCollection collection = getOneById(id);
    collection.setShared(true);
    repository.save(collection);
  }

  @Transactional
  public void saveCollection(final UUID id) {
    final CardCollection collection = getOneSharedById(id);

    final CardCollection copedCollection = copyEntity(collection, userService.getLogged());
    final Set<Card> copiedCards = toStream(collection.getCards())
      .map(card -> CardMapper.copyEntity(card, copedCollection))
      .collect(toSet());

    repository.save(copedCollection);
    cardRepository.saveAll(copiedCards);
  }
}
