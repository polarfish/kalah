package space.polarfish.kalah.manager;

import org.springframework.stereotype.Component;
import space.polarfish.kalah.exceptions.GameNotFoundException;
import space.polarfish.kalah.game.GameBoard;
import space.polarfish.kalah.game.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager {
    private final Map<UUID, GameBoard> allGames = new ConcurrentHashMap<>();

    public GameBoard getGameById(UUID gameId) {
        GameBoard game = allGames.get(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game with uid " + gameId.toString() + " not found", gameId);
        }
        return game;
    }

    public UUID createGame(Player host) {
        GameBoard game = new GameBoard(host);
        allGames.put(game.getGameId(), game);
        return game.getGameId();
    }

    public void joinGame(UUID gameId, Player guest) {
        GameBoard game = allGames.get(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game with uid " + gameId.toString() + " not found", gameId);
        }
        game.joinGame(guest);
    }
}
