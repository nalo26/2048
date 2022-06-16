package vue_controleur;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class EndScreen extends JPanel {

    public EndScreen(Swing2048 view, String text) {
        setLayout(new BorderLayout());
        setPreferredSize(view.getGameContentSize());
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
                view.restartGame();
                Container container = view.getMainContent();
                container.remove(EndScreen.this);
                container.add(view.getContentPanel());
                updateUI();
            }
        });
        add(restart, BorderLayout.SOUTH);
    }
}
