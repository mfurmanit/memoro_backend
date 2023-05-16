package pl.mfurman.memoro.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.CardCollectionRequest;
import pl.mfurman.memoro.dto.CardCollectionResponse;
import pl.mfurman.memoro.services.CardCollectionService;

import java.util.List;
import java.util.UUID;

import static pl.mfurman.memoro.utils.StringConstants.API;

@RestController
@AllArgsConstructor
public class CardCollectionController {

  public final static String API_CARD_COLLECTIONS = API + "/card-collections";
  public final static String API_CARD_COLLECTION = API_CARD_COLLECTIONS + "/{id}";
  public final static String API_CARD_COLLECTIONS_SHARED = API_CARD_COLLECTIONS + "/shared";
  public final static String API_CARD_COLLECTION_SHARE = API_CARD_COLLECTION + "/share";
  public final static String API_CARD_COLLECTION_STOP_SHARING = API_CARD_COLLECTION + "/stop-sharing";
  public final static String API_CARD_COLLECTION_SAVE = API_CARD_COLLECTION + "/save";

  private final CardCollectionService service;

  @GetMapping(API_CARD_COLLECTIONS)
  public Page<CardCollectionResponse> getAll(
    @PageableDefault final Pageable pageable,
    @RequestParam(value = "value", required = false) final String value,
    @RequestParam(value = "omitShared", required = false) final Boolean omitShared
  ) {
    return service.getAll(pageable, value, omitShared);
  }

  @GetMapping(API_CARD_COLLECTIONS_SHARED)
  public List<CardCollectionResponse> getAllShared() {
    return service.getAllShared();
  }

  @PostMapping(API_CARD_COLLECTIONS)
  public void createCollection(@Valid @RequestBody final CardCollectionRequest request) {
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

  @PostMapping(API_CARD_COLLECTION_SHARE)
  public void shareCollection(@PathVariable final UUID id) {
    service.shareCollection(id);
  }

  @PostMapping(API_CARD_COLLECTION_STOP_SHARING)
  public void stopSharingCollection(@PathVariable final UUID id) {
    service.stopSharingCollection(id);
  }

  @PostMapping(API_CARD_COLLECTION_SAVE)
  public void saveCollection(@PathVariable final UUID id) {
    service.saveCollection(id);
  }
}
