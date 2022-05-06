package modele;

import static modele.Game.EMPTY_CASE;
import static modele.Game.getInstance;

public class Case {
    private final Game game = getInstance();
    private int value;

    public Case(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void merge(Case otherCase) {
        this.value += otherCase.getValue();
    }

    public void move(Direction direction) {

        Case neighbour = game.getNeighbour(direction, this);
        while (neighbour == EMPTY_CASE) {
            if (!game.moveCase(direction, this)) {
                return;
            }
            neighbour = game.getNeighbour(direction, this);
        }
        if (neighbour != null && neighbour.getValue() == this.value) {
            neighbour.merge(this);
            game.deleteCase(this);
        }
    }

}
