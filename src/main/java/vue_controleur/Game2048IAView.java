package vue_controleur;

import ia.MonteCarloAI;
import modele.Direction;
import modele.Game;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Game2048IAView extends Swing2048 {

    private final MonteCarloAI ai;

    public Game2048IAView(Game _jeu) {
        super(_jeu);
        ai = new MonteCarloAI(_jeu);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                Executor exec = Executors.newSingleThreadExecutor();
                exec.execute(() -> play(_jeu));
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
    }

    public void play(Game _jeu) {
        while (!_jeu.isGameOver() && !_jeu.isGameWon()) {
            Direction play = ai.play();
            System.out.println("dir : " + play);
            _jeu.move(play);
            refresh();

        }
    }

    @Override
    public void addKeyListener() {
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }

}
