package vue_controleur;

import javax.swing.*;
import modele.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class MainMenu extends JPanel {

    private final Game game;

    public MainMenu() {
        game = Game.init(4);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));

        JLabel title = new JLabel("2048 GAME");
        title.setFont(new Font("serif", Font.BOLD, 100));
        title.setForeground(Color.RED);
        title.setVerticalAlignment(JLabel.NORTH);
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, NORTH);

        JLabel baseGameClickableLabel = new JLabel("Base Game");
        baseGameClickableLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Swing2048 vue = new Swing2048(game);
                game.addObserver(vue);
                vue.setVisible(true);
                removeAll();
                vue.setFocusable(true);
                setFocusable(false);
                add(vue);
                vue.requestFocusInWindow();
                updateUI();
            }
        });
        baseGameClickableLabel.setHorizontalAlignment(JLabel.CENTER);
        baseGameClickableLabel.setVerticalAlignment(JLabel.CENTER);
        baseGameClickableLabel.setFont(new Font("serif", Font.PLAIN, 32));
        add(baseGameClickableLabel, CENTER);


    }
}
