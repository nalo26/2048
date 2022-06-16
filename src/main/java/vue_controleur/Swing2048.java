package vue_controleur;

import javax.swing.*;
import javax.swing.border.Border;
import modele.Case;
import modele.Game;

import static javax.imageio.ImageIO.read;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.SwingConstants.CENTER;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.awt.BorderLayout.PAGE_START;
import static java.awt.Color.WHITE;
import static java.awt.Color.darkGray;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.util.Arrays.asList;
import static modele.Case.EMPTY_CASE;
import static modele.Direction.*;

public class Swing2048 extends JPanel implements Observer {
    public static final int PIXEL_PER_SQUARE = 150;
    // tableau de cases : i, j -> case graphique
    protected JLabel[][] tabC;
    protected Game game;
    protected final Map<Integer, Color> caseColor;
    protected final List<CaseColors> colorList = asList(CaseColors.values());

    public Swing2048(Game _jeu) {
        game = _jeu;
        addKeyListener();
        Container mainContent = new Container();
        mainContent.setLayout(new BorderLayout());
        Component topComponent = generateTopComponent();
        mainContent.add(topComponent, PAGE_START);

        tabC = new JLabel[game.getSize()][game.getSize()];
        caseColor = IntStream.rangeClosed(1, 10).map(val -> (int) pow(2, val)).boxed()
                .collect(Collectors.toMap(value -> value, value -> colorList.get((int) (log(value) / log(2))).getColor()));
        JPanel contentPanel = getGameContentPanel();
        mainContent.add(contentPanel);
        add(mainContent);
        refresh();

    }

    protected JPanel getGameContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(game.getSize(), game.getSize()));
        contentPanel.setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE));
        Border border = createLineBorder(darkGray, 5);
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tabC[i][j] = new JLabel();
                tabC[i][j].setOpaque(true);
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(CENTER);
                tabC[i][j].setForeground(WHITE);
                tabC[i][j].setFont(new Font(tabC[i][j].getFont().getName(), Font.BOLD, 45));

                contentPanel.add(tabC[i][j]);

            }
        }
        return contentPanel;
    }


    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     * 🤡🤡🤡
     */
    protected void refresh() {

        // demande au processus graphique de réaliser le traitement
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < game.getSize(); i++) {
                for (int j = 0; j < game.getSize(); j++) {
                    Case c = game.getCase(j, i);

                    if (c == EMPTY_CASE) {

                        tabC[i][j].setText("");
                        tabC[i][j].setBackground(WHITE);
                    } else {
                        tabC[i][j].setText(c.getValue() + "");
                        tabC[i][j].setBackground(caseColor.get(c.getValue()));
                    }


                }
            }
            if (game.isGameOver() || game.isGameWon()) {
                EndScreen endScreen = new EndScreen(game, game.isGameOver() ? "YOU LOSE 🤡🤡 !" : "YOU WIN 👑👑!");
                endScreen.setVisible(true);
                removeAll();
                add(endScreen);
                endScreen.requestFocusInWindow();
                System.out.println("ici");
                updateUI();
            }
        });


    }

    private Component generateTopComponent() {
        JPanel topPanel = new JPanel(new BorderLayout(game.getSize(), game.getSize()));
        topPanel.setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, PIXEL_PER_SQUARE / 2));
        topPanel.setBackground(Color.BLACK);
        topPanel.setOpaque(false);
        topPanel.setForeground(Color.BLACK);

        JLabel title = new JLabel("2048 - Score : " + game.getScore(), SwingConstants.LEFT);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 46));
        topPanel.add(title, BorderLayout.CENTER);
        JLabel restartClickableLabel = new JLabel();
        try {
            BufferedImage icon = read(new File("src/main/resources/restartIcon.png"));
            restartClickableLabel.setIcon(new ImageIcon((icon.getScaledInstance(55, 60, Image.SCALE_SMOOTH))));
        } catch (Exception e) {
            restartClickableLabel.setText("restart");
        }
        restartClickableLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.restart();
            }
        });
        topPanel.add(restartClickableLabel, BorderLayout.EAST);
        return topPanel;
    }


    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    public void addKeyListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT:
                        game.move(LEFT);
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

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}