package MJ.sellingservice.exception;

public class NoCorrectPasswordException extends RuntimeException {

  public NoCorrectPasswordException(String message) {
    super(message);
  }
}
