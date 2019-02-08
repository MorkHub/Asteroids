package game1;

import utilities.BasicController;
import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;

public class BasicShip {
    private static final int RADIUS = 8;

    // rotation velocity in radians per second
    private static final double STEER_RATE = 2* Math.PI;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 50;

    // constant speed loss factor
    private static final double DRAG = 0.01;

    private static final Color COLOR = Color.cyan;

    private Vector2D position; // on frame
    private Vector2D velocity; // per second
    // direction in which the nose of the ship is pointing
    // this will be the direction in which thrust is applied
    // it is a unit vector representing the angle by which the ship has rotated
    private Vector2D direction;

    // controller which provides an Action object in each frame
    private BasicController ctrl;

    private Stroke body = new BasicStroke(2);
    private Stroke pointer = new BasicStroke(3);

    BasicShip(BasicController ctrl) {
        this.ctrl = ctrl;
        position = new Vector2D((double) FRAME_WIDTH / 2, (double) FRAME_HEIGHT / 2);
        velocity = new Vector2D(0, 0);
        direction = new Vector2D(0, 1);
    }

    void update() {
        velocity.mult(1 - DRAG);

        direction.rotate(STEER_RATE * DT * ctrl.action().turn);
        Vector2D thrust = new Vector2D(direction).mult(MAG_ACC * DT).mult(Math.abs(ctrl.action().thrust));
        velocity.add(thrust);

        if (ctrl.action().thrust != 0)
            System.out.println(thrust);

        position.add(velocity);
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    @Override
    public String toString() {
        return String.format("BasicShip{position=%s, velocity=%s, direction=%s}", position, velocity, direction);
    }

    void draw(Graphics2D g) {
        Stroke old = g.getStroke();
        g.setColor(COLOR);
        g.drawOval((int) position.x, (int) position.y, RADIUS, RADIUS);
        g.drawLine((int) position.x + RADIUS/2, (int) position.y + RADIUS/2, (int) (position.x + direction.x * 15), (int) (position.y + direction.y * 15));
        g.setStroke(old);
    }
}

