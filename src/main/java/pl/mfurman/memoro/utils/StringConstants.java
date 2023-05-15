package pl.mfurman.memoro.utils;

public interface StringConstants {

  String API = "/api";
  String LOGIN = "/login";
  String REGISTER = "/register";
  String SESSION_COOKIE = "JSESSIONID";
  String LOGIN_REDIRECT = "/login?redirectUrl=%s";
  String UNAUTHORIZED_ERROR = "Nieautoryzowane żądanie.";
  String BAD_CREDENTIALS = "Wprowadziłeś niepoprawną nazwę użytkownika lub hasło.";
  String CANNOT_BE_NULL = "Wskazany obiekt nie może być pusty.";
  String ENTITY_NOT_FOUND = "Obiekt o podanym identyfikatorze nie istnieje.";
  String WRONG_CONTEXT = "Wskazana kolekcja nie należy do tego użytkownika.";
  String NOT_SHARED = "Wskazana kolekcja nie została udostępniona.";
  String CANNOT_SAVE = "Nie można zapisać kolekcji udostępnionej przez siebie.";
  String GLOBAL_ERROR = "Wystąpił nieoczekiwany problem.";
  String CANNOT_ANSWER = "Nie można dokonać odpowiedzi, ponieważ proces nauki nie został jeszcze rozpoczęty.";
  String BAD_COLLECTION = "Wskazana karta nie należy do kolekcji, która jest w trakcie nauki.";
  String CANNOT_ACTIVATE = "Link aktywacyjny wygasł.";
  String USER_ACTIVATED = "Link aktywacyjny został już wykorzystany.";
  String CANNOT_SEND = "Podczas wysyłki wiadomości e-mail wystąpił nieoczekiwany problem.";
  String ACTIVATION_SUBJECT = "Aktywacja konta w aplikacji Memoro";
  String EXPIRATION_SUBJECT = "Link aktywacyjny wygasł";
  String CANNOT_LOAD_TEMPLATE = "Nie udało się wczytać wskazanego szablonu.";

  String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$!%&()*+,\\-./:;<=>?@\\[\\]^_`{|}~])" +
    "[a-zA-Z\\d#$!%&()*+,\\-./:;<=>?@\\[\\]^_`{|}~]{8,40}$";

  String WEAK_PASSWORD = "Hasło musi zawierać co najmniej 8 znaków, w tym co najmniej jedną " +
    "małą i dużą literę, cyfrę oraz znak specjalny z zakresu (#$!%&()*+,\\-./:;<=>?@[\\]^_`{|}~).";
}
