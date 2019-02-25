package game1;

import utilities.BasicController;
import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class BasicShip extends GameObject {
    // rotation velocity in radians per second
    private static final double STEER_RATE = 2.5 * Math.PI;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 30;

    // constant speed loss factor
    private static final double DRAG = 0.01;

    private static final Color color = Color.cyan;
    private static final Color thrustColor = Color.orange;

    private long lastBullet = 0;

    // controller which provides an Action object in each frame
    private BasicController ctrl;

    private int[] XP = { 0, (int) size, 0, (int) -size };
    private int[] YP = { (int) -size, (int) size, 0, (int) size };
    private int[] XPTHRUST = { 0, (int) -size, 0, (int) size };
    private int[] YPTHRUST = { 0, (int) size, (int) size / 2, (int) size };

    public Vector2D getDirection() {
        return direction;
    }

    BasicShip() {
        super(new Vector2D((double) FRAME_WIDTH / 2, (double) FRAME_HEIGHT / 2), new Vector2D(),
                new Vector2D(0, -1).normalise(), 100);
    }

    BasicShip(BasicController ctrl) {
        this();
        this.ctrl = ctrl;
    }

    @Override
    public void update() {
        synchronized (BasicGame.class) {
            velocity.mult(1 - DRAG);

            direction.rotate(STEER_RATE * DT * ctrl.action().turn);
            Vector2D thrust = new Vector2D(direction).mult(MAG_ACC).mult(Math.abs(ctrl.action().thrust));
            velocity.add(thrust);

            super.update();
        }
    }

    @Override
    public String toString() {
        return String.format("BasicShip{position=%s, velocity=%s, direction=%s}", position, velocity, direction);
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);
        g.setColor(color);
        g.fillPolygon(XP, YP, XP.length);
        if (ctrl.action().thrust != 0) {
            g.setColor(thrustColor);
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);
    }

    private double fireRate = 3;

    public boolean canFire() {
        long now = System.nanoTime();
        boolean fire = now >= lastBullet + 1_000_000_000 / fireRate;
        if (fire)
            lastBullet = now;
        return fire;
    }

    public void hit() {
        game.lives--;
    }

    public BasicBullet fire() {
        Random r = new Random();
        double cone = Math.toRadians(fireRate / 20);

        game.score -= 5;

        return new BasicBullet(new Vector2D(position),
                new Vector2D(direction).normalise().mult(1000).rotate(2 * r.nextDouble() * cone - cone).add(velocity),
                new Vector2D(direction));
    }
}
