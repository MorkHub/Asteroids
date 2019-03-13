package game1;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class BasicBullet extends GameObject {
    public long alive;

    private int[] XP = {0, (int) size, 0, (int) -size};
    private int[] YP = {(int) -size, (int) size, 0, (int) size};

    public double size() {
        return this.size;
    }

    public BasicBullet(Vector2D position, Vector2D velocity, Vector2D direction) {
        super(position, velocity, direction, 70);
    }

    @Override
    public void update(double dt) {
        synchronized (BasicGame.class) {
            super.update(dt);
            alive += dt;
            if (alive >= 2_000_000_000) dead = true;
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
