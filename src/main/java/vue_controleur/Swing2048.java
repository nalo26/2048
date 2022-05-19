package vue_controleur;

import javax.swing.*;
import javax.swing.border.Border;
import modele.Case;
import modele.Game;

import static javax.imageio.ImageIO.read;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;
import java.awt.*;
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

public class Swing2048 extends JPanel implements Observer {
    public static final int PIXEL_PER_SQUARE = 150;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Game game;
    private final Map<Integer, Color> caseColor;
    private final List<CaseColors> colorList = asList(CaseColors.values());

    public Swing2048(Game _jeu) {
        game = _jeu;
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize((game.getSize() * PIXEL_PER_SQUARE), (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE));


        Container mainContent = new Container();
        mainContent.setLayout(new BorderLayout());
        Component topComponent = generateTopComponent();
        mainContent.add(topComponent, PAGE_START);

        tabC = new JLabel[game.getSize()][game.getSize()];
        caseColor = IntStream.rangeClosed(1, 10).map(val -> (int) pow(2, val)).boxed()
                .collect(Collectors.toMap(value -> value, value -> colorList.get((int) (log(value) / log(2))).getColor()));
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
        mainContent.add(contentPanel);
        add(mainContent);
        refresh();

    }


    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     * 🤡🤡🤡
     */
    private void refresh() {

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
            if (game.isGameOver()) {
                generateTopComponent();
            }
        });


    }

    private Component generateTopComponent() {
        JPanel endScreen = new JPanel(new BorderLayout(game.getSize(), game.getSize()));
        endScreen.setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, PIXEL_PER_SQUARE / 2));
        endScreen.setBackground(Color.BLACK);
        endScreen.setOpaque(false);
        endScreen.setForeground(Color.BLACK);

        JLabel endLabel = new JLabel("2048", LEFT);
        endLabel.setFont(new Font(endLabel.getFont().getName(), Font.BOLD, 46));
        endScreen.add(endLabel, BorderLayout.CENTER);
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
        endScreen.add(restartClickableLabel, BorderLayout.EAST);
        return endScreen;
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}