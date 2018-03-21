package space.polarfish.kalah.game;

abstract class CupOfStones {

    enum CupType {
        PIT, HOUSE
    }

    private Player owner;
    private int stones = 0;
    private CupOfStones nextCup;

    public CupOfStones(Player owner, int stones) {
        this.owner = owner;
        this.stones = stones;
    }

    public Player getOwner() {
        return owner;
    }

    abstract CupType getType();

    boolean isEmpty() {
        return stones == 0;
    }

    int getStones() {
        return stones;
    }

    int takeStones() {
        final int result = stones;
        stones = 0;
        return result;
    }

    int putStone() {
        return putStones(1);
    }

    int putStones(int newStones) {
        stones += newStones;
        return stones;
    }

    CupOfStones getNextCup() {
        return nextCup;
    }

    void setNextCup(CupOfStones nextCup) {
        this.nextCup = nextCup;
    }

    abstract CupOfStones getOppositeCup();
}
