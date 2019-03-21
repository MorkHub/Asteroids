package game1;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class Bullet extends GameObject {
    private int[] XP = {0, (int) size/5, 0, (int) -size/5};
    private int[] YP = {(int) -size, (int) size, 0, (int) size};

    protected Color color = Color.red;

    public long alive;
    public long lifetime;

    public Bullet(Vector2D position, Vector2D velocity, Vector2D direction, int lifetime) {
        super(position, velocity, direction, 70);
        this.lifetime = lifetime;
        setName("Bullet");
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
    public void doDraw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);
        g.fillPolygon(XP, YP, XP.length);

        g.setTransform(at);
    }

    @Override
    public boolean collidesWith(GameObject other) {
        if (other instanceof Ship) {
            return alive > 250_000_000;
        }

        return !(other instanceof Bullet);
    }
}
