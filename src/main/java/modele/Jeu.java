package modele;

import java.util.Observable;
import java.util.Random;

import static modele.Location.locationAddition;

public class Jeu extends Observable {

    private final Case[][] tabCases;
    public static final Random RANDOM = new Random(4);

    public Jeu(int size) {
        tabCases = new Case[size][size];
        fillGrid();
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

    public Case getNeighbour(Direction direction, Case givenCase) {
        Location locationToAdd = direction.getLocation();
        Location currentLocation = getCaseLocation(givenCase);
        return getCase(locationAddition(locationToAdd, currentLocation));
    }

    public Location getCaseLocation(Case givenCase) {
        for (int y = 0; y < tabCases.length; y++) {
            for (int x = 0; x < tabCases[y].length; x++) {
                if (givenCase == getCase(x, y)) {
                    return new Location(x, y);
                }
            }
        }
        return null;
    }

    public void fillGrid() {
        new Thread(() -> { // permet de libérer le processus graphique ou de la console

            // fill the grid of nothing
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    tabCases[i][j] = null;
                }
            }

            // generate 2 positions for starting cells
            Location randomLocation1 = Location.generateRandomLocation(tabCases.length);
            Location randomLocation2;
            do {
                randomLocation2 = Location.generateRandomLocation(tabCases.length);
            } while (randomLocation2.equals(randomLocation1));

            // generate value for the 2 starting cells and putting them on grid
            tabCases[randomLocation1.getRow()][randomLocation1.getCol()] = new Case((RANDOM.nextInt(2) + 1) * 2);
            tabCases[randomLocation2.getRow()][randomLocation2.getCol()] = new Case((RANDOM.nextInt(2) + 1) * 2);

        }).start();

        setChanged();
        notifyObservers();

    }

}
