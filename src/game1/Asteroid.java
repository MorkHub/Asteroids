package game1;

import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Asteroid extends GameObject {
    public static final double MAX_SPEED = 160;

    public Asteroid(double x, double y, double vx, double vy, double dx, double dy) {
        this(x, y, vx, vy, dx, dy, 150);
    }

    public Asteroid(double x, double y, double vx, double vy, double dx, double dy, double size) {
        super(new Vector2D(x, y), new Vector2D(vx, vy), new Vector2D(dx, dy), size);
        if (velocity.mag() < MAX_SPEED / 4) velocity.mult(4);
        direction.normalise();
    }

    public Asteroid debris() {
        Vector2D vel = new Vector2D(velocity).mult(1 + (Math.random() * 0.2)).rotate(Math.random() * Math.PI);
        Vector2D dir = new Vector2D(0, 1).rotate(Math.random() * Math.PI);

        return new Asteroid(
                position.x, position.y,
                vel.x, vel.y,
                dir.x, dir.y,
                size / 3);
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return other instanceof Bullet && !(other instanceof EnemyBullet) || other instanceof Ship && !(other instanceof Enemy);
    }

    public static Asteroid makeRandomAsteroid() {
        Random r = new Random();
        int width, height;

        if (game != null && game.view != null) {
            width = game.view.getWidth();
            height = game.view.getHeight();
        } else {
            width = FRAME_WIDTH;
            height = FRAME_HEIGHT;
        }

        return new Asteroid(r.nextInt(width), r.nextInt(height), 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED, 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI);
    }

    private int[] XP = {(int) size, (int) size, (int) -size, (int) -size};
    private int[] YP = {(int) size, (int) -size, (int) -size, (int) size};

    @Override
    public void hit() {
        super.hit();
        game.modScore((int) size);

        if (size > 75) {
            game.addObject(this.debris());
            game.addObject(this.debris());
        }
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);
        g.setStroke(new BasicStroke((int) ((double) size / 20)));
        g.drawPolygon(XP, YP, XP.length);

        g.setStroke(new BasicStroke(1));
        g.setTransform(at);
    }
}
