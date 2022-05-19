package vue_controleur;

import javax.swing.*;
import modele.Direction;
import modele.Game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;
import static modele.Direction.*;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class MainMenu extends JFrame {

    private final Game game;

    public MainMenu() {
        game = Game.init(4);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));

        setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));
        JPanel panel = new JPanel(new BorderLayout());
        JLabel baseGameClickableLabel = new JLabel("Base Game");
        baseGameClickableLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Swing2048 vue = new Swing2048(game);
                game.addObserver(vue);
                vue.setVisible(true);
                pack();
                setContentPane(vue);
            }
        });
        panel.add(baseGameClickableLabel, CENTER);
        setContentPane(panel);
        addKeyListener();
        pack();
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void addKeyListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT:
                        game.move(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.move(RIGHT);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.move(DOWN);
                        break;
                    case KeyEvent.VK_UP:
                        game.move(UP);
                        break;
                }
            }
        });
    }
}
