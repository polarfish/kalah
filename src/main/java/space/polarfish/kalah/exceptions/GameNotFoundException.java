package space.polarfish.kalah.exceptions;

import java.util.UUID;

public class GameNotFoundException extends GameManagerException {
    private final UUID gameId;

    public GameNotFoundException(String message, UUID gameId) {
        super(message);
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
