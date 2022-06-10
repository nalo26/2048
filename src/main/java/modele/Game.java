package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import static java.util.Arrays.asList;
import static modele.Case.EMPTY_CASE;
import static modele.Location.locationAddition;

public class Game extends Observable {

    private Case[][] tabCases;
    public static final Random RANDOM = new Random(10);

    private static final List<Location> BORDERS = new ArrayList<>();

    public Game(int size) {
        tabCases = new Case[size][size];
        fillGrid();
        generateBorders();
    }

    private void generateBorders() {
        int gridSize = getSize();
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (x == 0 || y == 0 || x == gridSize - 1 || y == gridSize - 1) {
                    BORDERS.add(new Location(x, y));
                }
            }
        }
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(Location loc) {
        return tabCases[loc.getRow()][loc.getCol()];
    }

    public Case getCase(int col, int row) {
        return tabCases[row][col];
    }

    public void setCase(Case givenCase, Location loc) {
        tabCases[loc.getRow()][loc.getCol()] = givenCase;
    }

    public void move(Direction direction) {
        new Thread(() -> {
            int move_count = 0;
            if (asList(Direction.LEFT, Direction.UP).contains(direction)) {
                for (int y = 0; y < getSize(); y++) {
                    for (int x = 0; x < getSize(); x++) {
                        Case currentCase = getCase(x, y);
                        if (currentCase == EMPTY_CASE) continue;
                        move_count += currentCase.move(direction) ? 1 : 0;
                    }
                }
            } else {
                for (int y = getSize() - 1; y >= 0; y--) {
                    for (int x = getSize() - 1; x >= 0; x--) {
                        Case currentCase = getCase(x, y);
                        if (currentCase == EMPTY_CASE) continue;
                        move_count += currentCase.move(direction) ? 1 : 0;
                    }
                }
            }
            if (!isGameOver() && move_count != 0)
                generateRandomCase();

            resetCellsMergeState();
            setChanged();
            notifyObservers();
        }).start();
    }

    private void resetCellsMergeState() {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                tabCases[y][x].setMerged(false);
            }
        }
    }

    public boolean isPossibleLocation(Direction direction, Location caseLocation) {

        if (BORDERS.contains(caseLocation)) {
            switch (direction) {
                case UP:
                    return caseLocation.getRow() != 0;
                case LEFT:
                    return caseLocation.getCol() != 0;
                case DOWN:
                    return caseLocation.getRow() != getSize() - 1;
                case RIGHT:
                    return caseLocation.getCol() != getSize() - 1;

            }
        }
        return true;
    }

    public void deleteCase(Case caseToDelete) {
        Location location = getCaseLocation(caseToDelete);
        setCase(EMPTY_CASE, location);
    }

    public boolean moveCase(Direction direction, Case currentCase) {
        if (isPossibleLocation(direction, getCaseLocation(currentCase))) {
            Location locationToAdd = direction.getLocation();
            Location oldLocation = getCaseLocation(currentCase);
            Location newLocation = locationAddition(locationToAdd, oldLocation);
            setCase(currentCase, newLocation);
            setCase(EMPTY_CASE, oldLocation);
            return true;
        }
        return false;
    }

    public Case getNeighbour(Direction direction, Case givenCase) {
        Location caseLocation = getCaseLocation(givenCase);
        if (isPossibleLocation(direction, caseLocation)) {
            Location locationToAdd = direction.getLocation();
            return getCase(locationAddition(locationToAdd, caseLocation));
        }
        return null;
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
        } while (getCase(location) != EMPTY_CASE);
        Case caseToAdd = new Case((RANDOM.nextInt(2) + 1) * 2, this);
        tabCases[location.getRow()][location.getCol()] = caseToAdd;
    }

    public boolean isGameOver() {
        Case currentCase, neighbour;
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                currentCase = getCase(x, y);
                if (currentCase == EMPTY_CASE)
                    return false;
                for (Direction direction : Direction.values()) {
                    neighbour = getNeighbour(direction, currentCase);
                    if (neighbour != null && neighbour.getValue() == currentCase.getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isGameWon() {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (getCase(x, y).getValue() >= 2048)
                    return true;
            }
        }
        return false;
    }

    public void fillGrid() {
        new Thread(() -> { // permet de libérer le processus graphique ou de la console

            // fill the grid of nothing
            for (int y = 0; y < getSize(); y++) {
                for (int x = 0; x < getSize(); x++) {
                    tabCases[y][x] = EMPTY_CASE;
                }
            }

            generateRandomCase();
            generateRandomCase();

        }).start();

        setChanged();
        notifyObservers();

    }


    public void restart() {
        tabCases = new Case[getSize()][getSize()];
        fillGrid();
        generateBorders();
        setChanged();
        notifyObservers();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
