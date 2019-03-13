package game1;

import utilities.Controller;
import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Ship extends GameObject {
    // rotation velocity in radians per second
    private static final double STEER_RATE = 2.5 * Math.PI;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 30;

    // constant speed loss factor
    private static final double DRAG = 0.01;

    private static final Color color = Color.cyan;
    private static final Color thrustColor = Color.orange;

    private long lastBullet = 0;

    private long invuln = 0;

    // controller which provides an Action object in each frame

    private Controller ctrl;
    private int[] XP = {0, (int) size, 0, (int) -size};

    private int[] YP = {(int) -size, (int) size, 0, (int) size};
    private int[] XPTHRUST = {0, (int) -size, 0, (int) size};
    private int[] YPTHRUST = {0, (int) size, (int) size / 2, (int) size};

    public Vector2D getDirection() {
        return direction;
    }

    Ship() {
        super(new Vector2D((double) FRAME_WIDTH / 2, (double) FRAME_HEIGHT / 2), new Vector2D(),
                new Vector2D(0, -1).normalise(), 100);
        invuln(3);
    }

    Ship(Controller ctrl) {
        this();
        this.ctrl = ctrl;
    }

    @Override
    public void update(double dt) {
        synchronized (Game.class) {
            velocity.mult(1 - DRAG);

            direction.rotate(STEER_RATE * DT * ctrl.action().turn);
            Vector2D thrust = new Vector2D(direction).mult(MAG_ACC).mult(Math.abs(ctrl.action().thrust));
            velocity.add(thrust);

            if (this.invuln > 0)
                this.invuln -= dt;

            super.update(dt);
        }
    }

    @Override
    public String toString() {
        return String.format("Ship{position=%s, velocity=%s, direction=%s}", position, velocity, direction);
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        g.setColor(colour(color));

        g.fillPolygon(XP, YP, XP.length);
        if (ctrl.action().thrust != 0) {
            g.setColor(colour(thrustColor));
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);
    }

    public Color colour(Color c) {
        if (invuln > 0) {
            return new Color((float) c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 0.3f);
        } else return c;
    }

    public void invuln(double seconds) {
        this.invuln = (long) seconds * 1_000_000_000L;
    }

    private double fireRate = 4;

    public boolean canFire() {
        long now = System.nanoTime();
        boolean fire = now >= lastBullet + 1_000_000_000 / fireRate;
        if (fire)
            lastBullet = now;
        return fire;
    }

    @Override
    public Rectangle hitbox() {
        double s = size * DRAWING_SCALE;
        return new Rectangle((int) (position.x - s), (int) (position.y - s), (int) (s * 2), (int) (s * 2));
    }

    public void hit() {
        this.dead = (game.modLives(-1)) <= 0;
        if (!dead)
            this.invuln(3);
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return this.invuln <= 0 && (other instanceof Asteroid || other instanceof Bullet) && other.collidesWith(this);
    }

    public void fire() {
        Random r = new Random();
        double cone = Math.toRadians(fireRate / 20);

        game.modScore(-5);
        game.addObject(new Bullet(
                new Vector2D(position).add(new Vector2D(direction).normalise().mult((70 + size) * DRAWING_SCALE)).addScaled(velocity, DT),
                new Vector2D(direction).normalise().mult(600).rotate(2 * r.nextDouble() * cone - cone).add(velocity),
                new Vector2D(direction)));
    }
}
