package game1;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class BasicBullet extends GameObject {
    public long die;

    private int[] XP = {0, (int) size, 0, (int) -size};
    private int[] YP = {(int) -size, (int) size, 0, (int) size};

    public double size() {
        return this.size;
    }

    public BasicBullet(Vector2D position, Vector2D velocity, Vector2D direction) {
        super(position, velocity, direction, 70);
        die = System.nanoTime() + 1_000_000_000 * 2;
    }

    @Override
    public void update() {
        synchronized (BasicGame.class) {
            super.update();
            if (System.nanoTime() > die) dead = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(color);
        g.fillPolygon(XP, YP, XP.length);

        g.setTransform(at);
    }
}
