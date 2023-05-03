package pl.mfurman.memoro.utils;

public interface StringConstants {

  String API = "/api";
  String LOGIN = "/login";
  String SESSION_COOKIE = "JSESSIONID";
  String LOGIN_REDIRECT = "/login?redirectUrl=%s";
  String UNAUTHORIZED_ERROR = "Nieautoryzowane żądanie.";
  String BAD_CREDENTIALS = "Wprowadziłeś niepoprawną nazwę użytkownika lub hasło.";
  String CANNOT_BE_NULL = "Wskazany obiekt nie może być pusty.";
  String ENTITY_NOT_FOUND = "Obiekt o podanym identyfikatorze nie istnieje.";
  String WRONG_CONTEXT = "Wskazana kolekcja nie należy do tego użytkownika.";
  String GLOBAL_ERROR = "Wystąpił nieoczekiwany problem.";
  String CANNOT_ANSWER = "Nie można dokonać odpowiedzi, ponieważ proces nauki nie został jeszcze rozpoczęty.";
  String BAD_COLLECTION = "Wskazana karta nie należy do kolekcji, która jest w trakcie nauki.";
}
