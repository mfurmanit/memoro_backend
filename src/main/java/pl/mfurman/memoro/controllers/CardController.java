package pl.mfurman.memoro.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.CardRequest;
import pl.mfurman.memoro.dto.CardResponse;
import pl.mfurman.memoro.services.CardService;

import java.util.List;
import java.util.UUID;

import static pl.mfurman.memoro.controllers.CardCollectionController.API_CARD_COLLECTION;
import static pl.mfurman.memoro.utils.StringConstants.API;

@RestController
@AllArgsConstructor
public class CardController {

  public final static String API_CARDS = API + "/cards";
  public final static String API_CARD = API_CARDS + "/{id}";
  public final static String API_CARD_FAVORITE = API_CARD + "/favorite";
  public final static String API_COLLECTION_CARDS = API_CARD_COLLECTION + "/cards";

  private final CardService service;

  @GetMapping(API_COLLECTION_CARDS)
  public List<CardResponse> getAll(@PathVariable final UUID id) {
    return service.getAll(id);
  }

  @PostMapping(API_CARDS)
  public void createCard(final CardRequest request) {
    service.createCard(request);
  }

  @PostMapping(API_CARD)
  public void updateCard(@PathVariable final UUID id, final CardRequest request) {
    service.updateCard(id, request);
  }

  @PostMapping(API_CARD_FAVORITE)
  public void markAsFavorite(@PathVariable final UUID id) {
    service.markAsFavorite(id);
  }

  @DeleteMapping(API_CARD)
  public void deleteCard(@PathVariable final UUID id) {
    service.deleteCard(id);
  }
}
