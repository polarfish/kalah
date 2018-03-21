package space.polarfish.kalah.exceptions;

public abstract class GameException extends RuntimeException {

    public GameException(String message) {
        super(message);
    }
}
