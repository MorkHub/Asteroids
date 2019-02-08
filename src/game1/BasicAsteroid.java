package game1;

import utilities.Vector2D;

import static game1.Constants.DT;
import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class BasicAsteroid {
    public static final int RADIUS = 10;
    public static final double MAX_SPEED = 100;

    private Vector2D pos;
    private Vector2D vel;

    public BasicAsteroid(double x, double y, double vx, double vy) {
        this.pos = new Vector2D(x, y);
        this.vel = new Vector2D(vx, vy);
    }

	public static BasicAsteroid makeRandomAsteroid() {
        Random r = new Random();
        return new BasicAsteroid(r.nextInt(FRAME_WIDTH), r.nextInt(FRAME_HEIGHT), 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED, 2 * MAX_SPEED * r.nextDouble() - MAX_SPEED);
    }

    public void update() {
        this.pos.x += this.vel.x * DT;
        this.pos.y += this.vel.y * DT;
        this.pos.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.red);
        g.fillOval((int) this.pos.x - RADIUS, (int) this.pos.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
