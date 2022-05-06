package modele;

import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

import static modele.Location.locationAddition;

public class Game extends Observable {

    private final Case[][] tabCases;
    public static final Random RANDOM = new Random(10);

    public Game(int size) {
        tabCases = new Case[size][size];
        fillGrid();
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(Location loc) throws ArrayIndexOutOfBoundsException {
        return tabCases[loc.getRow()][loc.getCol()];
    }

    public Case getCase(int col, int row) throws ArrayIndexOutOfBoundsException {
        return tabCases[row][col];
    }

    public void setCase(Case givenCase, Location loc) throws ArrayIndexOutOfBoundsException {
        tabCases[loc.getRow()][loc.getCol()] = givenCase;
    }

    public void move(Direction direction) {
        if (Arrays.asList(Direction.LEFT, Direction.UP).contains(direction)) {
            for (int y = 0; y < getSize(); y++) {
                for (int x = 0; x < getSize(); x++) {
                    Case currentCase = getCase(x, y);
                    if (currentCase == null)
                        continue;
                    currentCase.move(direction);
                }
            }
        } else {
            for (int y = getSize() - 1; y >= 0; y--) {
                for (int x = getSize() - 1; x >= 0; x--) {
                    Case currentCase = getCase(x, y);
                    if (currentCase == null)
                        continue;
                    currentCase.move(direction);
                }
            }
        }
        if (!isGameOver())
            generateRandomCase();

        setChanged();
        notifyObservers();
    }

    public void deleteCase(Case caseToDelete) {
        Location location = getCaseLocation(caseToDelete);
        setCase(null, location);
    }

    public void moveCase(Direction direction, Case currentCase) throws ArrayIndexOutOfBoundsException {
        Location locationToAdd = direction.getLocation();
        Location oldLocation = getCaseLocation(currentCase);
        Location newLocation = locationAddition(locationToAdd, oldLocation);
        setCase(currentCase, newLocation);
        setCase(null, oldLocation);
    }

    public Case getNeighbour(Direction direction, Case givenCase) throws ArrayIndexOutOfBoundsException {
        Location locationToAdd = direction.getLocation();
        Location currentLocation = getCaseLocation(givenCase);
        return getCase(locationAddition(locationToAdd, currentLocation));
    }

    public Location getCaseLocation(Case givenCase) {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (givenCase == getCase(x, y)) {
                    return new Location(x, y);
                }
            }
        }
        return null;
    }

    public void generateRandomCase() {
        Location location;
        do {
            location = Location.generateRandomLocation(getSize());
        } while (getCase(location) != null);
        Case caseToAdd = new Case(this, (RANDOM.nextInt(2) + 1) * 2);
        tabCases[location.getRow()][location.getCol()] = caseToAdd;
    }

    public boolean isGameOver() {
        Case currentCase, neighbour;
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                currentCase = getCase(x, y);
                if (currentCase == null)
                    return false;
                for (Direction direction : Direction.values()) {
                    try {
                        neighbour = getNeighbour(direction, currentCase);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    if (neighbour != null && neighbour.getValue() == currentCase.getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void fillGrid() {
        new Thread(() -> { // permet de libérer le processus graphique ou de la console

            // fill the grid of nothing
            for (int y = 0; y < getSize(); y++) {
                for (int x = 0; x < getSize(); x++) {
                    tabCases[y][x] = null;
                }
            }

            generateRandomCase();
            generateRandomCase();

        }).start();

        setChanged();
        notifyObservers();

    }

}
