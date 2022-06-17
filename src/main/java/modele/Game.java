package modele;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static modele.Case.EMPTY_CASE;
import static modele.Location.locationAddition;

public class Game extends Observable implements Cloneable {

    private Case[][] tabCases;
    private Map<Location, Case> posCases = new HashMap<Location, Case>();
    private int score;
    public static final Random RANDOM = new Random();

    private static final List<Location> BORDERS = new ArrayList<>();

    public Game(int size) {
        tabCases = new Case[size][size];
        score = 0;
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
        return posCases.get(loc);
    }

    public Case getCase(int col, int row) {
        return tabCases[row][col];
    }

    public void setCase(Case givenCase, Location loc) {
        tabCases[loc.getRow()][loc.getCol()] = givenCase;
        posCases.put(loc, givenCase);
    }

    public void move(Direction direction) {
        AtomicInteger move_count = new AtomicInteger(0);
        List<Integer> cases = posCases.values().stream().filter(currentCase -> currentCase != Case.EMPTY_CASE)
                .map(currentCase -> move_count.addAndGet(currentCase.move(direction) ? 1 : 0))
                .collect(Collectors.toList());

        if (!isGameOver() && move_count.get() != 0)
            generateRandomCase();

        resetCellsMergeState();
        setChanged();
        notifyObservers();
    }

    private void resetCellsMergeState() {
        posCases.values().forEach(c -> c.setMerged(false));
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
        final Optional<Location> currentLocation = posCases.entrySet().stream()
                .filter(enty -> givenCase.equals(enty.getValue())).map(Entry::getKey).findFirst();
        return currentLocation.orElseThrow(() -> new RuntimeException("Cannot find location"));
    }

    public void generateRandomCase() {
        Location location;
        do {
            location = Location.generateRandomLocation(getSize());
        } while (getCase(location) != EMPTY_CASE);
        Case caseToAdd = new Case((RANDOM.nextInt(2) + 1) * 2, this);
        setCase(caseToAdd, location);
    }

    public boolean isGameOver() {
        Case currentCase, neighbour;
        List<Case> notEmptyCases = new ArrayList<>();
        for (Entry<Location, Case> entry : posCases.entrySet()) {
            currentCase = entry.getValue();
            if (currentCase == EMPTY_CASE)
                return false;
            notEmptyCases.add(currentCase);
        }
        for (Direction direction : Direction.values()) {
            for (Case currentNotEmptyCase : notEmptyCases) {
                neighbour = getNeighbour(direction, currentNotEmptyCase);
                if (neighbour != null && neighbour.getValue() == currentNotEmptyCase.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameWon() {
        for (Entry<Location, Case> entry : posCases.entrySet()) {
            if (entry.getValue().getValue() >= 2048)
                return true;
        }
        return false;
    }

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void fillGrid() {
        // fill the grid of nothing
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                setCase(EMPTY_CASE, new Location(x, y));
            }
        }

        generateRandomCase();
        generateRandomCase();

        setChanged();
        notifyObservers();
    }

    public void restart() {
        tabCases = new Case[getSize()][getSize()];
        score = 0;
        fillGrid();
        generateBorders();
        setChanged();
        notifyObservers();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Game clone = new Game(this.getSize());
        for (Entry<Location, Case> entry : posCases.entrySet()) {
            clone.setCase(new Case(entry.getValue().getValue(), clone), entry.getKey());
        }
        return clone;
    }

}
