package game1;

import utilities.Action;
import utilities.Controller;
import utilities.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Random;

import static game1.Asteroid.MAX_SPEED;
import static game1.Constants.*;

public class Enemy extends Ship {

    public Enemy(double x, double y, double vx, double vy, double dx, double dy, double size) {
        super(x, y, vx, vy, dx, dy, size);
        this.ctrl = new Controller() {
            public Action action() { return new Action(); }
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
        };
        setName("Enemy");
    }

    public static Enemy makeRandomEnemy() {
        Random r = new Random();
        int width, height;

        if (game != null && game.view != null) {
            width = game.view.getWidth();
            height = game.view.getHeight();
        } else {
            width = FRAME_WIDTH;
            height = FRAME_HEIGHT;
        }

        return new Enemy(
                r.nextDouble() * width,
                r.nextDouble() * height,
                2 * MAX_SPEED * r.nextDouble() - MAX_SPEED,
                2 * MAX_SPEED * r.nextDouble() - MAX_SPEED,
                r.nextDouble() * Math.PI,
                r.nextDouble() * Math.PI, 120);
    }

    @Override
    public void update(double dt) {
        synchronized (Game.class) {
            if (this.invuln > 0)
                this.invuln -= dt;

            if (this.velocity.mag() < 100)
                this.velocity.add(new Vector2D(direction).normalise().mult(100));

            if (Math.random() * 1000 <= 5)
                direction.rotate(Math.random() * 0.3 * Math.PI);

            if (canFire()) fire();
            super.update(dt);
        }
    }

    private Color color = Color.orange;

    private int[] XP = {(int) size, 0, (int) size, 0};
    private int[] YP = {(int) size, (int) size, 0, 0};

    @Override
    public void doDraw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);

        g.fillPolygon(XP, YP, XP.length);
        g.setTransform(at);
    }

    public boolean canFire() {
        long now = System.nanoTime();
        boolean fire = now >= lastBullet + 4_000_000_000L;
        if (fire)
            lastBullet = now;
        return fire;
    }

    @Override
    public void hit(GameObject other) {
        this.dead = true;
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return other instanceof Bullet && !(other instanceof EnemyBullet);
    }

    @Override
    public void fire() {
        Vector2D a = new Vector2D(direction).rotate(Math.PI * 0.5).normalise();
        Vector2D b = new Vector2D(a).rotate(Math.PI);
        game.addObject(new EnemyBullet(new Vector2D(position), a.mult(100), a));
        game.addObject(new EnemyBullet(new Vector2D(position), b.mult(100), b));
    }
}
