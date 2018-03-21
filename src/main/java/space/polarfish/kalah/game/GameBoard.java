package space.polarfish.kalah.game;

import space.polarfish.kalah.exceptions.GameLogicException;
import space.polarfish.kalah.model.GameBoardModel;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoard {

    private static final int HOUSE_INDEX = 6;

    private final UUID gameId = UUID.randomUUID();
    private Date creationTime = new Date();

    private GameStatus status = GameStatus.NOT_STARTED;

    private final Queue<Player> playersQueue = new LinkedList<>();
    private Player host;
    private Player guest;

    private final Map<Player, List<CupOfStones>> cupsMap = new HashMap<>();

    public UUID getGameId() {
        return gameId;
    }

    public GameBoard(Player host) {
        this.host = host;
    }

    public synchronized GameStatus getStatus() {
        return status;
    }

    public synchronized void joinGame(Player guest) {
        if (status == GameStatus.STARTED) {
            throw new GameLogicException("This game has already started");
        }

        if (status == GameStatus.ABORTED) {
            throw new GameLogicException("The game was aborted");
        }

        if (host.equals(guest)) {
            throw new GameLogicException("You cannot join your own game");
        }

        this.guest = guest;

        startGame();
    }

    private void startGame() {
        playersQueue.add(guest);
        playersQueue.add(host);

        List<CupOfStones> hostCups = new ArrayList<>(7);
        for (int i = 0; i < 6; i++) {
            hostCups.add(new Pit(host, i + 1));
        }
        hostCups.add(new House(host));
        cupsMap.put(host, hostCups);

        List<CupOfStones> guestCups = new ArrayList<>(7);
        for (int i = 0; i < 6; i++) {
            guestCups.add(new Pit(guest, i + 1));
        }
        guestCups.add(new House(guest));
        cupsMap.put(guest, guestCups);

        for (int i = 0; i < hostCups.size() - 1; i++) {
            hostCups.get(i).setNextCup(hostCups.get(i + 1));
        }

        for (int i = 0; i < guestCups.size() - 1; i++) {
            guestCups.get(i).setNextCup(guestCups.get(i + 1));
        }

        hostCups.get(HOUSE_INDEX).setNextCup(guestCups.get(0));
        guestCups.get(HOUSE_INDEX).setNextCup((hostCups.get(0)));

        status = GameStatus.STARTED;
    }

    public synchronized void abort() {
        if (status != GameStatus.FINISHED) {
            status = GameStatus.ABORTED;
        }
    }

    private void nextPlayer() {
        playersQueue.add(playersQueue.remove());
        playersQueue.element();
    }

    private Player currentPlayer() {
        return playersQueue.element();
    }

    private boolean isTurnPossible(Player p) {
        List<CupOfStones> playerCups = cupsMap.get(p);

        if (playerCups == null) {
            throw new IllegalArgumentException("Player " + p.getName() + " does not participate in this game");
        }

        for (int i = 0; i < HOUSE_INDEX; i++) {
            if (!playerCups.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean isGameStarted() {
        return guest != null;
    }

    private boolean isGameFinished() {
        if (!isGameStarted()) {
            throw new GameLogicException("The game has not started yet");
        }

        for (Player p : cupsMap.keySet()) {
            if (isTurnPossible(p)) {
                return false;
            }
        }

        return true;
    }

    private void tryFinishingGame() {
        List<Player> players = new ArrayList<>(cupsMap.keySet());
        boolean[] playerHasTurn = new boolean[]{isTurnPossible(players.get(0)), isTurnPossible(players.get(1))};

        if (playerHasTurn[0] ^ playerHasTurn[1]) {
            if (playerHasTurn[0]) {
                takeAllPlayersStonesToHouse(players.get(0));
            } else {
                takeAllPlayersStonesToHouse(players.get(1));
            }
        }
    }

    private void takeAllPlayersStonesToHouse(Player p) {
        List<CupOfStones> playersCups = cupsMap.get(p);
        CupOfStones playersHouse = playersCups.get(HOUSE_INDEX);
        for (int i = 0; i < HOUSE_INDEX; i++) {
            playersHouse.putStones(playersCups.get(i).takeStones());
        }
    }

    public synchronized void makeTurn(Player p, int cupIndex) {
        if (!cupsMap.keySet().contains(p)) {
            throw new GameLogicException("You do not participate in this game");
        }

        if (status == GameStatus.NOT_STARTED) {
            throw new GameLogicException("The game has not started yet");
        }

        if (status == GameStatus.FINISHED) {
            throw new GameLogicException("The game is already finished");
        }

        if (status == GameStatus.ABORTED) {
            throw new GameLogicException("The game was aborted");
        }

        if (!p.equals(currentPlayer())) {
            throw new GameLogicException("It is turn of " + currentPlayer().getName());
        }

        if (cupIndex < 0 || cupIndex > 5) {
            throw new GameLogicException("Invalid cup index received: " + cupIndex);
        }

        CupOfStones cup = cupsMap.get(p).get(cupIndex);

        if (cup.getType() == CupOfStones.CupType.HOUSE) {
            throw new GameLogicException("It is not allowed to take stones from a house");
        }

        Pit pit = (Pit) cup;

        if (cup.isEmpty()) {
            throw new GameLogicException("Pit number " + pit.getPitNumber() + " is empty");
        }

        int remainingStones = cup.takeStones();

        CupOfStones currentCup = pit;
        while (remainingStones > 0) {
            currentCup = currentCup.getNextCup();

            if (currentCup.getType() == CupOfStones.CupType.HOUSE && !currentCup.getOwner().equals(p)) {
                continue;
            }

            currentCup.putStone();
            remainingStones--;
        }

        if (currentCup.getType() != CupOfStones.CupType.HOUSE) {
            if (currentCup.getOwner().equals(p) && currentCup.getStones() == 1) {
                CupOfStones playersHouse = cupsMap.get(p).get(HOUSE_INDEX);
                playersHouse.putStones(currentCup.takeStones());
                playersHouse.putStones(currentCup.getOppositeCup().takeStones());
            }

            nextPlayer();
        }

        tryFinishingGame();

        if (isGameFinished()) {
            status = GameStatus.FINISHED;
        }
    }

    public synchronized int getScore(Player p) {
        if (!isGameStarted()) {
            throw new GameLogicException("The game has not started yet");
        }
        return cupsMap.get(p).get(HOUSE_INDEX).getStones();
    }

    public synchronized GameBoardModel getModel(Player p) {
        if (!isGameStarted()) {
            throw new GameLogicException("The game has not started yet");
        }

        Player player;
        Player opponent;

        if (p.equals(host)) {
            player = host;
            opponent = guest;
        } else {
            player = guest;
            opponent = host;
        }

        return new GameBoardModel(
                player,
                cupsMap.get(player).stream().map(CupOfStones::getStones).collect(Collectors.toList()).subList(0, HOUSE_INDEX),
                cupsMap.get(player).get(HOUSE_INDEX).getStones(),
                opponent,
                cupsMap.get(opponent).stream().map(CupOfStones::getStones).collect(Collectors.toList()).subList(0, HOUSE_INDEX),
                cupsMap.get(opponent).get(HOUSE_INDEX).getStones(),
                player.equals(currentPlayer()));
    }

    @Override
    public synchronized String toString() {
        if (!isGameStarted()) {
            return "The game has not started yet";
        }

        StringBuilder result = new StringBuilder();

        List<CupOfStones> hostPits = cupsMap.get(host);
        List<CupOfStones> guestPits = cupsMap.get(guest);

        result.append(guest.getName()).append(System.lineSeparator())
                .append(MessageFormat.format("    {5} {4} {3} {2} {1} {0}    ",
                        guestPits.stream().map(CupOfStones::getStones).toArray())).append(System.lineSeparator())
                .append(MessageFormat.format(" {0}               {1} ",
                        guestPits.get(HOUSE_INDEX).getStones(), hostPits.get(HOUSE_INDEX).getStones())).append(System.lineSeparator())
                .append(MessageFormat.format("    {0} {1} {2} {3} {4} {5}    ",
                        hostPits.stream().map(CupOfStones::getStones).toArray())).append(System.lineSeparator())
                .append(host.getName()).append(System.lineSeparator());

        return result.toString();
    }
}
