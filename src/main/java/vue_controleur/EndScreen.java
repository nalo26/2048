package vue_controleur;

import javax.swing.*;
import modele.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class EndScreen extends JPanel {

    public EndScreen(Game game, String text) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE + PIXEL_PER_SQUARE / 2));
        setBackground(new Color(100, 100, 100, 100));
        JLabel endLabel = new JLabel(text);
        Font font = new Font("serif", Font.PLAIN, 40);
        endLabel.setFont(font);
        endLabel.setHorizontalAlignment(JLabel.CENTER);
        endLabel.setVerticalAlignment(JLabel.CENTER);
        add(endLabel, CENTER);
        JLabel restart = new JLabel("Restart");
        restart.setFont(font);
        restart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.restart();
                removeAll();
                Swing2048 swing2048 = new Swing2048(game);
                add(swing2048);
                swing2048.requestFocusInWindow();
                updateUI();
            }
        });
        add(restart, BorderLayout.SOUTH);
    }
}
