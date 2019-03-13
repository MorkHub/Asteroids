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

    public double size() {
        return this.size;
    }

    public boolean isDead() {
        return dead;
    }

    public abstract void draw(Graphics2D g);

    public void update(double dt) {
        if (Double.isNaN(position.x)) position.x = 0;
        if (Double.isNaN(position.y)) position.y = 0;
        position.addScaled(velocity, DT);
        position.wrap(game.view.getWidth(), game.view.getHeight());
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public GameObject(Vector2D position, Vector2D velocity, Vector2D direction, double size) {
        this.position = position;
        this.velocity = velocity;
        this.direction = direction;
        this.size = size;
    }

    public void hit() {
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
        if (collidesWith(other)) {
            if (this.getClass() != other.getClass() && this.overlap(other)) {
                this.hit();
                other.hit();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s{%s, %s}", getClass().getSimpleName(), position, velocity);
    }
}
