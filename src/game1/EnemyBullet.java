package game1;

import utilities.Vector2D;

import java.awt.*;

public class EnemyBullet extends Bullet {
    public EnemyBullet(Vector2D position, Vector2D velocity, Vector2D direction) {
        super(position, velocity, direction, 8);
        this.size *= 0.8;
        this.color = Color.pink;
    }

    @Override
    public void doDraw(Graphics2D g) {
        super.doDraw(g);
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return other instanceof Ship && !(other instanceof Enemy);
    }
}
