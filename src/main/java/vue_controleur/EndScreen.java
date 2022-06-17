package vue_controleur;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.BorderLayout.CENTER;

public class EndScreen extends JPanel {

    public EndScreen(Swing2048 view, String text) {
        setLayout(new BorderLayout());
        setPreferredSize(view.getGameContentSize());
        setBackground(new Color(100, 100, 100, 100));
        Font font = new Font("serif", Font.PLAIN, 40);
        JLabel endLabel = createEndMessage(text, font);
        add(endLabel, CENTER);
        JLabel restart = createRestartButton(view, font);
        add(restart, BorderLayout.SOUTH);
    }

    private JLabel createRestartButton(Swing2048 view, Font font) {
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
        return restart;
    }

    private JLabel createEndMessage(String text, Font font) {
        JLabel endLabel = new JLabel(text);
        endLabel.setFont(font);
        endLabel.setHorizontalAlignment(JLabel.CENTER);
        endLabel.setVerticalAlignment(JLabel.CENTER);
        return endLabel;
    }
}
