package game1;

import utilities.BasicController;
import utilities.JEasyFrame;

import java.util.ArrayList;
import java.util.List;

import static game1.Constants.*;

public class BasicGame {
    public static final int N_INITIAL_ASTEROIDS = Math.min(FRAME_HEIGHT, FRAME_WIDTH) / 75;
    public List<BasicAsteroid> asteroids;
    public BasicShip ship;
    public BasicController ctrl;

    public BasicGame() {
        ctrl = new BasicKeys();
        ship = new BasicShip(ctrl);
        asteroids = new ArrayList<>();
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            asteroids.add(BasicAsteroid.makeRandomAsteroid());
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "true");
        BasicGame game = new BasicGame();
        BasicView view = new BasicView(game);
        new JEasyFrame(view, "Basic Game").addKeyListener(game.ctrl);
        while (view.isVisible()) {
            game.update();
            view.repaint();
            Thread.sleep(DELAY);
        }
    }

    public void update() {
        ship.update();
        for (BasicAsteroid a : asteroids) {
            a.update();
        }
    }
}
