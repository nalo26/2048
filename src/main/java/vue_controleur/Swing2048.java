package vue_controleur;

import javax.swing.*;
import javax.swing.border.Border;
import modele.Case;
import modele.Game;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.util.Arrays.asList;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.SwingConstants.CENTER;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.awt.Color.*;
import static modele.Direction.*;
import static modele.Game.EMPTY_CASE;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Game game;
    private final Map<Integer, Color> caseColor;
    private final List<CaseColors> colorList = asList(CaseColors.values());

    public Swing2048(Game _jeu) {
        game = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[game.getSize()][game.getSize()];
        caseColor = IntStream.rangeClosed(1, 10).map(val -> (int) pow(2, val)).boxed()
                .collect(Collectors.toMap(value -> value, value -> colorList.get((int) (log(value) / log(2))).getColor()));
        JPanel contentPane = new JPanel(new GridLayout(game.getSize(), game.getSize()));

        Border border = createLineBorder(darkGray, 5);
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tabC[i][j] = new JLabel();
                tabC[i][j].setOpaque(true);
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(CENTER);
                tabC[i][j].setForeground(WHITE);


                contentPane.add(tabC[i][j]);

            }
        }
        setContentPane(contentPane);
        addKeyListener();
        refresh();

    }


    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void refresh() {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
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
            }
        });


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