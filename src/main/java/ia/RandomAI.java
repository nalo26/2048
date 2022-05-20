package ia;

import modele.Direction;
import modele.Game;

public class RandomAI implements AI {
    
    private Game game;

    public RandomAI(Game game) {
        this.game = game;
    }

    public void play() {
        int random = Game.RANDOM.nextInt(4);
        this.game.move(Direction.values()[random]);
    }
}
