package pl.mfurman.memoro.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mfurman.memoro.dto.CardCollectionRequest;
import pl.mfurman.memoro.dto.CardCollectionResponse;
import pl.mfurman.memoro.entities.CardCollection;
import pl.mfurman.memoro.mappers.CardCollectionMapper;
import pl.mfurman.memoro.repositories.CardCollectionRepository;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.util.UUID;

import static pl.mfurman.memoro.criteria.CardCollectionCriteria.userPredicate;
import static pl.mfurman.memoro.mappers.CardCollectionMapper.toEntity;
import static pl.mfurman.memoro.utils.CommonUtil.getOrThrow;
import static pl.mfurman.memoro.utils.StringConstants.WRONG_CONTEXT;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;
import static pl.mfurman.memoro.utils.UserUtil.getPrincipal;

@Service
@RequiredArgsConstructor
public class CardCollectionService {

  private final CardCollectionRepository repository;

  public Page<CardCollectionResponse> getAll(final Pageable pageable) {
    return repository.findAll(userPredicate(), pageable).map(CardCollectionMapper::toResponse);
  }

  public CardCollection getOneById(final UUID id) {
    return getOrThrow(repository.findById(id));
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

    if (!getLoggedUserId().equals(collection.getUser().getId()))
      throw new ApiException(WRONG_CONTEXT);

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
}
