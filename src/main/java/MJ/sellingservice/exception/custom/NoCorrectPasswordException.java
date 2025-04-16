package MJ.sellingservice.exception.custom;

public class NoCorrectPasswordException extends RuntimeException {

  public NoCorrectPasswordException(String message) {
    super(message);
  }
}
