package game1;

import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;

public abstract class GameObject {
    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D direction;
    protected Color color = Color.WHITE;
    protected double size = 100;
    protected boolean dead = false;
    private String name;

    public final double size() {
        return this.size;
    }

    public boolean isDead() {
        return dead;
    }

    protected final void setName(String name) {
        this.name = name;
    }

    public final String getName() {
        return name != null ? name : getClass().getSimpleName();
    }

    public final void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (game.ctrl.action().debug) {
            Color c = g.getColor();
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.red);
            g.draw(hitbox());
            g.setColor(c);
        }

        g.setStroke(new BasicStroke(10));
        doDraw(g);
    }

    public abstract void doDraw(Graphics2D g);

    public void update(double dt) {
        if (Double.isNaN(position.x)) position.x = 0;
        if (Double.isNaN(position.y)) position.y = 0;
        position.addScaled(velocity, DT);
        position.wrap(game.view.getWidth(), game.view.getHeight());
    }

    public final Vector2D getPosition() {
        return position;
    }

    public final Vector2D getVelocity() {
        return velocity;
    }

    public GameObject(Vector2D position, Vector2D velocity, Vector2D direction, double size) {
        this.position = position;
        this.velocity = velocity;
        this.direction = direction;
        this.size = size;
    }

    public void hit(GameObject other) {
        this.dead = true;
    }

    public Rectangle hitbox() {
        return new Rectangle((int) (position.x - size() / 4), (int) (position.y - size() / 4), (int) size() / 2,
                (int) size() / 2);
    }

    public boolean overlap(GameObject other) {
        return hitbox().intersects(other.hitbox());
    }

    public boolean collidesWith(GameObject other) {
        return true;
    }

    public void collisionHandling(GameObject other) {
        if (this == other)
            return;

        if (collidesWith(other)) {
            if (this.overlap(other)) {
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
