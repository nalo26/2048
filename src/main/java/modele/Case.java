package modele;

public class Case {
    private final Game game;
    private int value;

    public Case(Game game, int value) {
        this.game = game;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void merge(Case otherCase) {
        this.value += otherCase.getValue();
    }

    public void move(Direction direction) {
        try {
            Case neighbour = game.getNeighbour(direction, this);
            while (neighbour == null) {
                game.moveCase(direction, this);
                neighbour = game.getNeighbour(direction, this);
            }
            if (neighbour.getValue() == this.value) {
                neighbour.merge(this);
                game.deleteCase(this);
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

}
