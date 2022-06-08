package modele;

public class Case {
    public static final Case EMPTY_CASE = new Case(-1);

    private int value;
    private Game game;

    public Case(int value) {
        this.value = value;
    }

    public Case(int value, Game game) {
        this(value);
        this.game = game;
    }

    public int getValue() {
        return value;
    }

    public void merge(Case otherCase) {
        this.value += otherCase.getValue();
    }

    public Boolean move(Direction direction) {
        Boolean as_moved = false;

        Case neighbour = game.getNeighbour(direction, this);
        while (neighbour == EMPTY_CASE) {
            as_moved = true;
            if (!game.moveCase(direction, this)) {
                return as_moved;
            }
            neighbour = game.getNeighbour(direction, this);
        }
        if (neighbour == null) return as_moved;
        if (neighbour.getValue() == this.value){
            as_moved = true;
            neighbour.merge(this);
            game.deleteCase(this);
        } 
        return as_moved;
    }

}
