package pl.mfurman.memoro.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.CardCollectionRequest;
import pl.mfurman.memoro.dto.CardCollectionResponse;
import pl.mfurman.memoro.services.CardCollectionService;

import java.util.UUID;

import static pl.mfurman.memoro.utils.StringConstants.API;

@RestController
@AllArgsConstructor
public class CardCollectionController {

  public final static String API_CARD_COLLECTIONS = API + "/card-collections";
  public final static String API_CARD_COLLECTION = API_CARD_COLLECTIONS + "/{id}";

  private final CardCollectionService service;

  @GetMapping(API_CARD_COLLECTIONS)
  public Page<CardCollectionResponse> getAll(final Pageable pageable) {
    return service.getAll(pageable);
  }

  @PostMapping(API_CARD_COLLECTIONS)
  public void createCollection(@Valid final @RequestBody CardCollectionRequest request) {
    service.createCollection(request);
  }

  @PatchMapping(API_CARD_COLLECTION)
  public void updateCollection(@PathVariable final UUID id, @Valid @RequestBody final CardCollectionRequest request) {
    service.updateCollection(id, request);
  }

  @DeleteMapping(API_CARD_COLLECTION)
  public void deleteCollection(@PathVariable final UUID id) {
    service.deleteCollection(id);
  }
}
