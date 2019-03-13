package game1;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class Bullet extends GameObject {
    public long alive;
    public long lifetime;

    private int[] XP = {0, (int) size, 0, (int) -size};
    private int[] YP = {(int) -size, (int) size, 0, (int) size};

    public double size() {
        return this.size;
    }

    public Bullet(Vector2D position, Vector2D velocity, Vector2D direction, int lifetime) {
        super(position, velocity, direction, 70);
        this.lifetime = lifetime;
    }

    public Bullet(Vector2D position, Vector2D velocity, Vector2D direction) {
        this(position, velocity, direction, 3);
    }

    @Override
    public void update(double dt) {
        synchronized (Game.class) {
            super.update(dt);
            alive += dt;
            if (alive >= lifetime * 1_000_000_000) dead = true;
        }
    }

    @Override
    public boolean collidesWith(GameObject other) {
        if (other instanceof Ship) {
            return alive > 250_000_000;
        }

        return true;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);
        g.fillRect((int) (0 - size/2), (int) (0 - size/2), (int) (size * 1.4), (int) (size * 1.4));

        g.setTransform(at);
    }
}
