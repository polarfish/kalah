package space.polarfish.kalah.game;

public class Pit extends CupOfStones {

    private int pitNumber;

    Pit(Player owner, int pitNumber) {
        super(owner, 6);
        this.pitNumber = pitNumber;
    }

    int getPitNumber() {
        return pitNumber;
    }

    @Override
    CupType getType() {
        return CupType.PIT;
    }

    @Override
    CupOfStones getOppositeCup() {
        CupOfStones p = this;
        for (int i = 0, limit = (7 - pitNumber) * 2; i < limit; i++) {
            p = p.getNextCup();
        }
        return p;
    }
}
