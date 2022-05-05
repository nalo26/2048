package vue_controleur;

import modele.Case;
import modele.Game;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Console2048 extends Thread implements Observer {

    private Game game;


    public Console2048(Game _jeu) {
        game = _jeu;

    }


    @Override
    public void run() {
        while (true) {
            draw();

            synchronized (this) {
                listenKeyEvent();
                try {
                    wait(); // lorsque le processus s'endort, le verrou sur this est relâché, ce qui permet au processus de ecouteEvennementClavier()
                    // d'entrer dans la partie synchronisée, ce verrou évite que le réveil du processus de la console (update(..)) ne soit exécuté avant
                    // que le processus de la console ne soit endormi

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void listenKeyEvent() {

        final Object _this = this;

        new Thread() {
            public void run() {

                synchronized (_this) {
                    boolean end = false;

                    while (!end) {
                        String s = null;
                        try {
                            s = Character.toString((char) System.in.read());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (s.equals("4") || s.equals("8") || s.equals("6") || s.equals("2")) {
                            end = true;
                            game.fillGrid();
                        }
                    }


                }

            }
        }.start();


    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void draw() {


        System.out.printf("\033[H\033[J"); // permet d'effacer la console (ne fonctionne pas toujours depuis la console de l'IDE)

        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Case c = game.getCase(j, i);
                if (c != null) {
                    System.out.format("%5.5s", c.getValue());
                } else {
                    System.out.format("%5.5s", "");
                }

            }
            System.out.println();
        }

    }

    private void refresh() {
        synchronized (this) {
            try {
                notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}
