package modele;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private final Location location;

    Direction(int x, int y) {
        this.location = new Location(x, y);
    }

    public Location getLocation() {
        return this.location;
    }
}
