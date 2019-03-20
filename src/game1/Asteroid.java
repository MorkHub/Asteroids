package game1;

import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;

public class Asteroid extends GameObject {
    public static final double MAX_SPEED = 160;

    public Asteroid(double x, double y, double vx, double vy, double dx, double dy) {
        this(x, y, vx, vy, dx, dy, 150);
    }

    public Asteroid(double x, double y, double vx, double vy, double dx, double dy, double size) {
        super(new Vector2D(x, y), new Vector2D(vx, vy), new Vector2D(dx, dy), size);
        if (velocity.mag() < MAX_SPEED / 4) velocity.mult(4);
        direction.normalise();
        setName("Asteroid");
    }

    public Collection<GameObject> debris(Vector2D direction) {
        Vector2D dir1 = new Vector2D(direction).normalise().rotate(Math.PI / 2);
        Vector2D dir2 = new Vector2D(direction).normalise().rotate(Math.PI / -2);

        Vector2D vel1 = new Vector2D(dir1).mult(direction.mag() * 0.8).add(velocity);
        Vector2D vel2 = new Vector2D(dir2).mult(direction.mag() * 0.8).add(velocity);

        double newSize = size/2;

        return Arrays.asList(
                new Asteroid(position.x + dir1.x*newSize, position.y + dir1.y*newSize, vel1.x, vel1.y, dir1.x, dir1.y, newSize),
                new Asteroid(position.x + dir2.x*newSize, position.y + dir2.y*newSize, vel2.x, vel2.y, dir2.x, dir2.y, newSize)
        );
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return other instanceof Asteroid || other instanceof Bullet && !(other instanceof EnemyBullet) || other instanceof Ship && !(other instanceof Enemy);
    }

    public static Asteroid makeRandomAsteroid() {
        Random r = new Random();
        int width, height;

        try {
            width = game.view.getWidth();
            height = game.view.getHeight();

            return new Asteroid(
                    r.nextInt(width),
                    r.nextInt(height),
                    2 * MAX_SPEED * r.nextDouble() - MAX_SPEED,
                    2 * MAX_SPEED * r.nextDouble() - MAX_SPEED,
                    r.nextDouble() * Math.PI,
                    r.nextDouble() * Math.PI);
        } catch (Exception ignored) {
            return new Asteroid(0,0,r.nextInt(5), r.nextInt(5), 0,0);
        }
    }

    private int[] XP = {(int) size, (int) size, (int) -size, (int) -size};
    private int[] YP = {(int) size, (int) -size, (int) -size, (int) size};

    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public void hit(GameObject other) {
        if (other instanceof Asteroid) {
            Vector2D o = this.velocity;

            if (
                    Math.abs(this.velocity.diff(other.velocity).mag()) < 20 &&
                    Math.abs(this.position.diff(other.position).mag()) < 20
            ) {
                this.position.add(size, size);
                other.position.add(-size, -size);
            }

            this.velocity = other.velocity;
            other.velocity = o;

            update(DT);
            other.update(DT);

            return;
        }

        super.hit(other);
        game.modScore((int) size);

        if (size > 75) {
            game.addObjects(this.debris(other.velocity));
        }
    }

    @Override
    public void doDraw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);
        g.drawPolygon(XP, YP, XP.length);

        g.setStroke(new BasicStroke(1));
        g.setTransform(at);
    }
}
