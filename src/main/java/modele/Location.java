package modele;

import java.util.Random;

import static modele.Jeu.RANDOM;

public class Location {

    private final int row;
    private final int col;

    public Location(int col, int row) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public static Location generateRandomLocation(int bound) {
        Random rdm = RANDOM;
        int x = rdm.nextInt(bound);
        int y = rdm.nextInt(bound);

        return new Location(x, y);
    }

    public static Location locationAddition(Location left, Location right) {
        return new Location(left.getCol() + right.getCol(), left.getRow() + right.getRow());
    }

    @Override

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (col != other.col)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

}
