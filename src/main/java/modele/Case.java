package modele;

public class Case {
    private final Game game;
    private final int value;

    public Case(Game game, int value) {
        this.game = game;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void move(Direction direction) {
        Case neighbour = game.getNeighbour(direction, this);
        if(neighbour != null) {
            game.moveCase(direction, this);
        }
    }

}
