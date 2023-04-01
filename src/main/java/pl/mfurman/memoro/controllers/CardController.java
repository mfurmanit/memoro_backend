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
import pl.mfurman.memoro.dto.CardRequest;
import pl.mfurman.memoro.dto.CardResponse;
import pl.mfurman.memoro.enums.CardSide;
import pl.mfurman.memoro.services.CardService;

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
  public Page<CardResponse> getAll(
    @PathVariable final UUID id,
    @PageableDefault final Pageable pageable,
    @RequestParam(value = "side", required = false) final CardSide side,
    @RequestParam(value = "value", required = false) final String value
  ) {
    return service.getAll(id, pageable, side, value);
  }

  @PostMapping(API_CARDS)
  public void createCard(@Valid @RequestBody final CardRequest request) {
    service.createCard(request);
  }

  @PatchMapping(API_CARD)
  public void updateCard(@PathVariable final UUID id, @Valid @RequestBody final CardRequest request) {
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
