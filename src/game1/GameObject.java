package game1;

import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;

public abstract class GameObject {
    protected static final double MAX_SPEED = 160;

    private String name;

    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D direction;
    protected Color color = Color.WHITE;
    protected double size = 100;
    protected boolean dead = false;

    public GameObject(Vector2D position, Vector2D velocity, Vector2D direction, double size) {
        this.position = position;
        this.velocity = velocity;
        this.direction = direction;
        this.size = size;
    }

    protected final void setName(String name) {
        this.name = name;
    }

    public final String getName() {
        return name != null ? name : getClass().getSimpleName();
    }

    public final double getSize() {
        return this.size;
    }

    public boolean isDead() {
        return dead;
    }

    public final Vector2D getPosition() {
        return new Vector2D(position);
    }

    public final Vector2D getVelocity() {
        return new Vector2D(velocity);
    }

    public final Vector2D getDirection() {
        return new Vector2D(direction);
    }

    public final void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (game.ctrl.action().debug) {
            Color c = g.getColor();
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.red);
            g.draw(getHitbox());
            g.setColor(c);
        }

        g.setStroke(new BasicStroke(10));
        doDraw(g);
    }

    public Rectangle getHitbox() {
        return new Rectangle((int) (position.x - getSize() / 4), (int) (position.y - getSize() / 4), (int) getSize() / 2,
                (int) getSize() / 2);
    }

    public void update(double dt) {
        if (Double.isNaN(position.x)) position.x = 0;
        if (Double.isNaN(position.y)) position.y = 0;
        position.addScaled(velocity, DT);
        position.wrap(game.view.getWidth(), game.view.getHeight());
    }

    public void hit(GameObject other) {
        this.dead = true;
    }

    public abstract void doDraw(Graphics2D g);

    public boolean overlaps(GameObject other) {
        return getHitbox().intersects(other.getHitbox());
    }

    public boolean collidesWith(GameObject other) {
        return true;
    }

    public void collisionHandling(GameObject other) {
        if (this == other)
            return;

        if (collidesWith(other)) {
            if (this.overlaps(other)) {
                this.hit(other);
                if (getClass() != other.getClass()) {
                    other.hit(this);
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s{%s, %s}", getName(), position, velocity);
    }
}
