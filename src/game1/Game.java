package game1;

import utilities.Controller;
import utilities.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static game1.Constants.*;

public class Game {
    private static final int N_INITIAL_ASTEROIDS = (int) (Math.pow(5,
            (Math.log((double) Math.min(Toolkit.getDefaultToolkit().getScreenSize().height,
                    Toolkit.getDefaultToolkit().getScreenSize().width) / 200))) / 2);
    private List<GameObject> objectsToAdd = new ArrayList<>();
    private int score;

    boolean reset = false;
    int level = 0;
    String playerName;
    View view;
    Controller ctrl;
    Title title;
    Ship ship;
    InfoPanel info;
    List<GameObject> objects = new ArrayList<>();

    public Game() {
        ctrl = new Keys();
        ship = new Ship(ctrl);
        score = 0;

        Constants.game = this;
        info = new InfoPanel(this);
        view = new View(game);
    }

    public int getLevel() {
        return level;
    }

    public void addObject(GameObject o) {
        if (o instanceof Title)
            title = (Title) o;
        else objectsToAdd.add(o);
    }

    public void addObjects(Collection<GameObject> o) {
        o.forEach(this::addObject);
    }

    private void populate() {
//        for (int i = 0; i < N_INITIAL_ASTEROIDS + Math.pow((level > 0 ? level : 1), 1.4); i++)
        for (int i = 0; i < level; i++)
            addObject(Asteroid.makeRandomAsteroid());

        for (int i = 0; i < Math.floor(level / 4); i++)
            addObject(Enemy.makeRandomEnemy());

        ship.invulnerableFor(5);

        addObject(Title.showTitle(view, "Level " + level));
    }

    public void over() {
        Graphics g = view.getGraphics();
        g.fillRect(0, 0, view.getWidth(), view.getHeight());

        JOptionPane.showMessageDialog(
                null,
                String.format("Game Over.\nFinal score: %,d", game.getScore()),
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "true");
        Game game = new Game();
        GameFrame frame = new GameFrame(game.view, "Basic Game", OVERLAY, FULLSCREEN);
        frame.addKeyListener(game.ctrl);

        game.playerName = JOptionPane.showInputDialog(null, "Enter your name", "Start Game", JOptionPane.INFORMATION_MESSAGE);

        long from = System.nanoTime();
        while (game.view.isVisible() && game.getLives() > 0) {
            if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != null) {
                game.update(-from + (from = System.nanoTime()));
                game.view.repaint();
            }
            Thread.sleep(DELAY);
        }

        game.over();
    }

    public int asteroids() {
        return (int) objects.stream().filter(o -> o instanceof Asteroid).count();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return ship.lives;
    }

    public int modScore(int value) {
        return score += value;
    }

    private void update(long dt) {
        synchronized (Game.class) {
            if (asteroids() == 0 || reset) {
                objects.clear();

                if (ship.isDead()) {
                    over();
                }

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
                if (o.dead) continue;

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

            info.update(dt);
            title.update(dt);
        }
    }
}
