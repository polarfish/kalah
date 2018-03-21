package space.polarfish.kalah.game;

import space.polarfish.kalah.exceptions.GameLogicException;

class House extends CupOfStones {

    House(Player owner) {
        super(owner, 0);
    }

    @Override
    CupType getType() {
        return CupType.HOUSE;
    }

    @Override
    int takeStones() {
        throw new GameLogicException("It is not allowed to take stones from a house");
    }

    @Override
    CupOfStones getOppositeCup() {
        throw new GameLogicException("Cannot get opposite cup of a house");
    }
}
