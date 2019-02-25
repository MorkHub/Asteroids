package game1;

import utilities.BasicController;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static game1.Constants.*;

public class BasicGame {
    public static final int N_INITIAL_ASTEROIDS = (int) (Math.pow(5,
            (Math.log((double) Math.min(Toolkit.getDefaultToolkit().getScreenSize().height,
                    Toolkit.getDefaultToolkit().getScreenSize().width) / 75)))
            / 2);
    public List<GameObject> objects = new ArrayList<>();
    public BasicShip ship;
    public BasicController ctrl;
    public InfoPanel info;
    public BasicView view;
    public int score;
    public int lives;

    public BasicGame() {
        ctrl = new BasicKeys();
        ship = new BasicShip(ctrl);
        score = 0;
        lives = 5;

        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++)
            objects.add(BasicAsteroid.makeRandomAsteroid());

        Constants.game = this;
        info = new InfoPanel(this);
        view = new BasicView(game);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "true");
        BasicGame game = new BasicGame();
        new JEasyFrame(game.view, "Basic Game", OVERLAY, FULLSCREEN).addKeyListener(game.ctrl);
        while (game.view.isVisible()) {
            game.update();
            game.view.repaint();
            Thread.sleep(DELAY);
        }
    }

    long ns = System.nanoTime();

    public void update() {
        ship.update();
        if (ctrl.action().shoot)
            if (ship.canFire())
                objects.add(ship.fire());

        long now = System.nanoTime();
        boolean addObject = now >= ns + 1_000_000_000 * 2;
        if (addObject) {
            objects.add(BasicAsteroid.makeRandomAsteroid());
            ns = now;
        }

        for (int i = 0; i < objects.size(); i++) {
            GameObject o = objects.get(i);
            o.update();
            for (int j = i; j < objects.size(); j++) {
                o.collisionHandling(objects.get(j));
            }
        }

        GameObject o;
        ListIterator<GameObject> it;
        for (it = objects.listIterator(); it.hasNext();) {
            o = it.next();
            if (o.dead)
                it.remove();
        }

        info.update();
    }
}
