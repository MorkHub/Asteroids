package game1;

import utilities.Controller;
import utilities.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static game1.Constants.*;

public class Game {
    private static final int N_INITIAL_ASTEROIDS = (int) (Math.pow(5,
            (Math.log((double) Math.min(Toolkit.getDefaultToolkit().getScreenSize().height,
                    Toolkit.getDefaultToolkit().getScreenSize().width) / 200))) / 2);
    List<GameObject> objects = new ArrayList<>();
    private List<GameObject> objectsToAdd = new ArrayList<>();

    Ship ship;
    Controller ctrl;
    InfoPanel info;
    View view;
    private int score;
    private int lives;
    boolean reset = false;
    int level = 1;

    int getLevel() {
        return level;
    }

    void addObject(GameObject o) {
        objectsToAdd.add(o);
    }

    private Game() {
        ctrl = new Keys();
        ship = new Ship(ctrl);
        score = 0;
        lives = 5;

        populate();

        Constants.game = this;
        info = new InfoPanel(this);
        view = new View(game);
    }

    private void populate() {
        for (int i = 0; i < N_INITIAL_ASTEROIDS + Math.pow((level > 0 ? level : 1), 1.4); i++)
            objects.add(Asteroid.makeRandomAsteroid());

        if ((level) % 4 == 0) {
            Enemy e = Enemy.makeRandomEnemy();
            objects.add(e);
        }

        ship.invuln(5);
    }

    private void over() {
        Graphics g = view.getGraphics();
        g.fillRect(0, 0, view.getWidth(), view.getHeight());

        JOptionPane.showMessageDialog(
                null,
                String.format("You Died.\nFinal score: %,d", game.getScore()),
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "true");
        Game game = new Game();
        GameFrame frame = new GameFrame(game.view, "Basic Game", OVERLAY, FULLSCREEN);
        frame.addKeyListener(game.ctrl);

        long from = System.nanoTime();
        while (game.view.isVisible() && game.getLives() > 0) {
            game.update(-from + (from = System.nanoTime()));
            game.view.repaint();
            Thread.sleep(DELAY);
        }

        game.over();
    }

    int modLives(int diff) {
        return lives += diff;
    }

    int modScore(int value) {
        return score += value;
    }

    int asteroids() {
        return (int) objects.stream().filter(o -> o instanceof Asteroid).count();
    }

    int getScore() {
        return score;
    }

    private void update(long dt) {
        if (asteroids() == 0 || reset) {
            level++;
            populate();
            reset = false;
        }

        ship.update(dt);
        if (ctrl.action().shoot)
            if (ship.canFire())
                ship.fire();

        for (int i = 0; i < objects.size(); i++) {
            GameObject o = objects.get(i);
            if (o == null) continue;

            o.update((double) dt);
            ship.collisionHandling(o);
            for (int j = i; j < objects.size(); j++)
                o.collisionHandling(objects.get(j));
        }

        GameObject o;
        ListIterator<GameObject> it;
        for (it = objects.listIterator(); it.hasNext(); ) {
            o = it.next();
            if (o.dead)
                it.remove();
        }

        if (objectsToAdd.size() > 0) {
            objects.addAll(objectsToAdd);
            objectsToAdd.clear();
        }

        info.update();
    }

    int getLives() {
        return lives;
    }
}
