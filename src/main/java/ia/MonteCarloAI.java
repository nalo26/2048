package ia;

import modele.Direction;
import modele.Game;

import java.util.concurrent.*;

public class MonteCarloAI implements AI {

    private Game game;

    public MonteCarloAI(Game game) {
        this.game = game;
    }

    @Override
    public Direction play() {
        Direction best = null;
        double best_ratio = 0;
        int games_per_move = 100;
        for (Direction direction : Direction.values()) {
            int won = 0, lost = 0;
            Game copy;
            for (int i = 0; i < games_per_move; i++) {
                try {
                    copy = (Game) game.clone();
                } catch (CloneNotSupportedException e) {
                    continue;
                }
                copy.move(direction);
                // if (copy.isGameWon())
                //     return direction;
                if (randomGame(copy))
                    won++;
                else
                    lost++;
            }
            double ratio = (double) won / (lost + 1);
            if (ratio > best_ratio || best == null) {
                best = direction;
                best_ratio = ratio;
            }
        }
        return best;
    }

    private boolean randomGame(Game game) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                while (!game.isGameWon() && !game.isGameOver()) {
                    game.move(Direction.randomDirection());
                }
                return game.isGameWon();
            }
        };
        Future<Boolean> future = executor.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Cannot get The value");
        } finally {
            executor.shutdown();
        }
    }

}