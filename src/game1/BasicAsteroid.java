package game1;

import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

public class BasicAsteroid extends GameObject {
    public static final double MAX_SPEED = 200;

    List<Class<? extends GameObject>> collidables = Arrays.asList(BasicShip.class, BasicBullet.class);

    public BasicAsteroid(double x, double y, double vx, double vy, double dx, double dy) {
        super(new Vector2D(x, y), new Vector2D(vx, vy), new Vector2D(dx, dy), 150);
        if (velocity.mag() < 100) velocity.mult(4);
        direction.normalise();
    }

	public static BasicAsteroid makeRandomAsteroid() {
        Random r = new Random();
        return new BasicAsteroid(r.nextInt(FRAME_WIDTH), r.nextInt(FRAME_HEIGHT), 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED, 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI);
    }

    private int[] XP = {(int) size, (int)  size, (int) -size, (int) -size};
    private int[] YP = {(int) size, (int) -size, (int) -size, (int)  size};

    @Override
    public void hit() {
        super.hit();
        game.score += 75;
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
