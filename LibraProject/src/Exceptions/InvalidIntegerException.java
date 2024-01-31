package Exceptions;

public class InvalidIntegerException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidIntegerException(String input) {
        super("Invalid "+input+" doit etre de type entier");
    }
}
