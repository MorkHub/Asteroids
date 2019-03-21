package game1;

import utilities.Action;
import utilities.Controller;
import utilities.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class Enemy extends Ship {
    private final int[] XP = {0, (int)(size*.85), (int)(size*.85), 0, -(int)(size*.85), -(int)(size*.85), 0};
    private final int[] YP = {-(int) size, -(int)(size * .5), (int)(size * .5), (int) size, (int)(size * .5), -(int)(size * .5), -(int) size};
    private Color color = Color.pink;

    private long isTargeting = 0;

    public Enemy(double x, double y, double vx, double vy, double dx, double dy, double size) {
        super(x, y, vx, vy, dx, dy, size);

        this.fireRate = 2;
        this.lives = 0;

        setName("Enemy");

        this.ctrl = new Controller() {
            public Action action() { return new Action(); }
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
        };
    }

    public static Enemy makeRandomEnemy() {
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
                r.nextDouble() * Math.PI,
                120);
    }

    public void targetFor(double seconds) {
        this.isTargeting = (long) seconds * 1_000_000_000L;
    }

    @Override
    public void doFire() {
        if (r.nextInt(1_000) <= 1) {
            Vector2D dir = new Vector2D(0, 1).rotate(r.nextFloat() * Math.PI);
            Vector2D vel = new Vector2D(dir).add(velocity);

            game.addObject(new Asteroid(position.x, position.y, vel.x, vel.y, dir.x, dir.y));
            return;
        }

        Vector2D a = new Vector2D(direction).rotate(Math.PI * 0.5).normalise();
        Vector2D b = new Vector2D(a).rotate(Math.PI);

        game.addObject(new EnemyBullet(new Vector2D(position), a.mult(100), a));
        game.addObject(new EnemyBullet(new Vector2D(position), b.mult(100), b));
    }

    @Override
    public void update(double dt) {
        synchronized (Game.class) {
            if (this.velocity.mag() < 100)
                this.velocity.add(new Vector2D(direction).normalise().mult(100));

            if (isTargeting > 0) {
                this.isTargeting -= dt;
                direction = position.diff(game.ship.position).normalise();
            } else if (r.nextInt(30) <= 1) {
                direction.rotate(r.nextFloat() * 0.3 * Math.PI);
            } else if (r.nextInt(1000) <= 1)
                targetFor(3);

            if (canFire()) fire();
            super.update(dt);
        }
    }

    @Override
    public void doDraw(Graphics2D g) {
        AffineTransform at = g.getTransform();


        g.translate(position.x, position.y);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);
        g.rotate(direction.angle() + Math.PI / 2);

        if (isTargeting > 0)
            g.setColor(Color.red);
        else if (health < maxHealth)
            g.setColor(Color.green);
        else
            g.setColor(color);

        g.fillPolygon(XP, YP, XP.length);

        g.setColor(new Color(
                1 - color.getRed()/255,
                1 - color.getGreen()/255,
                1 - color.getBlue()/255));
        g.drawString(Float.toString(health), 0, 0);

        g.setTransform(at);
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return other instanceof Bullet && !(other instanceof EnemyBullet);
    }
}
