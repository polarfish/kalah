package space.polarfish.kalah.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameBoardTest {

    @Test
    public void testGame() throws Exception {
        Player redBot = new Player("Red Bot");
        Player blueBot = new Player("Blue Bot");

        class Turn {
            private final Player player;
            private final int index;

            private Turn(Player player, int index) {
                this.player = player;
                this.index = index;
            }
        }

        List<Turn> turns = new ArrayList<>();
        turns.add(new Turn(blueBot, 0));
        turns.add(new Turn(blueBot, 1));
        turns.add(new Turn(redBot, 0));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(blueBot, 4));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 0));
        turns.add(new Turn(blueBot, 0));
        turns.add(new Turn(redBot, 2));
        turns.add(new Turn(blueBot, 3));
        turns.add(new Turn(redBot, 4));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(redBot, 1));
        turns.add(new Turn(blueBot, 2));
        turns.add(new Turn(redBot, 3));
        turns.add(new Turn(blueBot, 3));
        turns.add(new Turn(blueBot, 2));
        turns.add(new Turn(redBot, 4));
        turns.add(new Turn(redBot, 3));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(redBot, 0));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(blueBot, 0));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(blueBot, 4));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 4));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 1));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 3));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 4));
        turns.add(new Turn(redBot, 5));
        turns.add(new Turn(redBot, 0));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(blueBot, 1));
        turns.add(new Turn(redBot, 2));
        turns.add(new Turn(blueBot, 3));
        turns.add(new Turn(blueBot, 5));
        turns.add(new Turn(blueBot, 2));
        turns.add(new Turn(redBot, 4));
        turns.add(new Turn(blueBot, 3));
        turns.add(new Turn(redBot, 3));

        GameBoard board = new GameBoard(redBot);

        assertEquals("GameBoard status is NOT_STARTED", GameStatus.NOT_STARTED.toString(), board.getStatus().toString());

        board.joinGame(blueBot);

        assertEquals("GameBoard status is STARTED", GameStatus.STARTED.toString(), board.getStatus().toString());

        for (Turn turn : turns) {
            board.makeTurn(turn.player, turn.index);
        }

        assertEquals("GameBoard status is FINISHED", GameStatus.FINISHED.toString(), board.getStatus().toString());
        assertEquals("Red Bot has 30 stones in his house", board.getScore(redBot), 30);
        assertEquals("Blue Bot has 42 stones in his house", board.getScore(blueBot), 42);
    }


}