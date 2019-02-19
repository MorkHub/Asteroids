package game1;

import utilities.BasicController;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static game1.Constants.*;

public class BasicGame {
    public static final int N_INITIAL_ASTEROIDS = (int) (Math.pow(5, (Math.log((double) Math.min(Toolkit.getDefaultToolkit().getScreenSize().height, Toolkit.getDefaultToolkit().getScreenSize().width) / 75))) / 2);
    public List<GameObject> objects = new ArrayList<>();
    public BasicShip ship;
    public BasicController ctrl;
    public InfoPanel info;
    public BasicView view;

    public BasicGame() {
        ctrl = new BasicKeys();
        ship = new BasicShip(ctrl);

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

    public void update() {
        ship.update();
        if (ctrl.action().shoot)
            if (ship.canFire())
                objects.add(ship.fire());

        synchronized (BasicGame.class) {
            GameObject o;
            ListIterator<GameObject> it;
            for (it = objects.listIterator(); it.hasNext(); ) {
                o = it.next();
                o.update();
                if (o.dead) it.remove();
            }
        }

        info.update();
    }
}
