package Exceptions;

public class InvalidEmailException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
        super("Invalid email address");
    }
}
