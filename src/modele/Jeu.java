package modele;

import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private final Case[][] tabCases;
    private static final Random rnd = new Random(4);

    public Jeu(int size) {
        tabCases = new Case[size][size];
        rnd();
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }


    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;

                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0 -> tabCases[i][j] = null;
                            case 1 -> tabCases[i][j] = new Case(2);
                            case 2 -> tabCases[i][j] = new Case(4);
                        }
                    }
                }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

}
