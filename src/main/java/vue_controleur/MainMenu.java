package vue_controleur;

import javax.swing.*;
import modele.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class MainMenu extends JPanel {

    private final Game game;

    public MainMenu() {
        game = Game.init(4);
        //setSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));
        JLabel baseGameClickableLabel = new JLabel("Base Game");
        baseGameClickableLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Swing2048 vue = new Swing2048(game);
                game.addObserver(vue);
                vue.setVisible(true);
                remove(baseGameClickableLabel);
                vue.setFocusable(true);
                setFocusable(false);
                //vue.addKeyListener(vue.addKeyListener());
                add(vue);
                vue.requestFocusInWindow();
                updateUI();
            }
        });
        add(baseGameClickableLabel, CENTER);
    }
}
