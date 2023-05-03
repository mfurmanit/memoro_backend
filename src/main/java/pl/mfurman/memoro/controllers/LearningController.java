package pl.mfurman.memoro.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.AnswerRequest;
import pl.mfurman.memoro.dto.CardResponse;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.main.LearningService;

import java.util.UUID;

import static pl.mfurman.memoro.mappers.CardMapper.toResponse;
import static pl.mfurman.memoro.utils.StringConstants.API;

@RestController
@AllArgsConstructor
public class LearningController {

  public final static String API_LEARNING = API + "/learning";
  public final static String API_LEARNING_START = API_LEARNING + "/start/{collectionId}";
  public final static String API_LEARNING_STOP = API_LEARNING + "/stop";
  public final static String API_LEARNING_ANSWER = API_LEARNING + "/answer";

  private final LearningService service;

  @PostMapping(API_LEARNING_START)
  public CardResponse start(@PathVariable final UUID collectionId) {
    return toResponse(service.start(collectionId));
  }

  @PostMapping(API_LEARNING_STOP)
  public void stop() {
    service.stop();
  }

  @PostMapping(API_LEARNING_ANSWER)
  public CardResponse answer(@RequestBody final AnswerRequest request) {
    return toResponse(service.answer(request.getCardId(), request.getAnswer()));
  }
}
