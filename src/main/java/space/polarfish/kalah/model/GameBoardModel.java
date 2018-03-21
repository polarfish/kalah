package space.polarfish.kalah.model;

import space.polarfish.kalah.game.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoardModel {

    private final Player player;
    private final List<Integer> playersPits;
    private final Integer playersHouse;

    private final Player opponent;
    private final List<Integer> opponentsPits;
    private final Integer opponentsHouse;

    private final boolean playersTurn;

    public GameBoardModel(Player player, List<Integer> playersPits, Integer playersHouse,
                          Player opponent, List<Integer> opponentsPits, Integer opponentsHouse,
                          boolean playersTurn) {
        this.player = player;
        this.playersPits = playersPits;
        this.playersHouse = playersHouse;
        this.opponent = opponent;
        this.opponentsPits = opponentsPits;
        this.opponentsHouse = opponentsHouse;
        this.playersTurn = playersTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Integer> getPlayersPits() {
        return playersPits;
    }

    public Integer getPlayersHouse() {
        return playersHouse;
    }

    public Player getOpponent() {
        return opponent;
    }

    public List<Integer> getOpponentsPits() {
        return opponentsPits;
    }

    public List<Integer> getOpponentsPitsReversed() {
        List<Integer> result = new ArrayList<>(this.opponentsPits);
        Collections.reverse(result);
        return result;
    }

    public Integer getOpponentsHouse() {
        return opponentsHouse;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }
}
