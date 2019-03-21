package game1;

import utilities.Controller;
import utilities.Vector2D;

import static game1.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject {

    private static final double STEER_RATE = 2.5 * Math.PI;  // rotation velocity in radians per second
    private static final double MAG_ACC = 30;  // acceleration when thrust is applied
    private static final double DRAG = 0.01;  // constant speed loss factor

    private static final Color color = Color.cyan;
    private static final Color thrustColor = Color.orange;

    // polygons
    private final int[] XP = {0, (int) size, 0, (int) -size};
    private final int[] YP = {(int) -size, (int) size, 0, (int) size};

    private final int[] XPTHRUST = {0, (int) -size, 0, (int) size};
    private final int[] YPTHRUST = {0, (int) size, (int) size / 2, (int) size};

    private int alphaBulletAmmunitionIndex = 0;
    private long timeInvulnerable = 0;
    private long timeUntilRegen = 0;
    private long timeUntilNextBullet = 0;
    protected Controller ctrl; // controller which provides an Action object in each frame

    int lives;
    int maxHealth;
    float health;
    double fireRate;

    public Ship(double x, double y, double vx, double vy, double dx, double dy, double size) {
        super(new Vector2D(x, y), new Vector2D(vx, vy), new Vector2D(dx, dy), size);
        direction.normalise();
        setName("Ship");

        this.maxHealth = 100;
        this.health = maxHealth;
        this.lives = 5;
        this.fireRate = 4;
    }

    Ship(Controller ctrl) {
        this();
        this.ctrl = ctrl;
    }

    public Ship() {
        this((double) FRAME_WIDTH / 2, (double) FRAME_HEIGHT / 2, 0, 0, 0, -1, 100);
    }

    private Color getColour(Color c) {
        if (this.isInvulnerable()) {
            return new Color((float) c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 0.3f);
        } else return c;
    }

    public void invulnerableFor(double seconds) {
        this.timeInvulnerable = (long) seconds * 1_000_000_000L;
    }

    public boolean isInvulnerable() {
        return this.timeInvulnerable > 0;
    }

    public void regenFor(double seconds) {
        this.timeUntilRegen = (long) seconds * 1_000_000_000L;
    }

    public boolean isRegenerating() {
        return this.health < maxHealth && this.timeUntilRegen <= 0;
    }

    public void doFire() {
        double cone = Math.toRadians(fireRate / 20);

        if (ctrl.action().selectedWeapon == 1) {
            game.modScore(-5);
            game.addObject(new Bullet(
                    new Vector2D(position).add(new Vector2D(direction).normalise().mult((70 + size) * DRAWING_SCALE)).addScaled(velocity, DT),
                    new Vector2D(direction).normalise().mult(600).rotate(2 * r.nextDouble() * cone - cone).add(velocity),
                    new Vector2D(direction)));
        } else {
            String s = game.playerName.toUpperCase();
            if (s.length() < 1)
                s = "#";

            char c = s.charAt(alphaBulletAmmunitionIndex);
            alphaBulletAmmunitionIndex = (++alphaBulletAmmunitionIndex) % s.length();

            game.modScore(-5);
            game.addObject(new AlphaBullet(c,
                    new Vector2D(position).add(new Vector2D(direction).normalise().mult((70 + size) * DRAWING_SCALE)).addScaled(velocity, DT),
                    new Vector2D(direction).normalise().mult(600).rotate(2 * r.nextDouble() * cone - cone).add(velocity),
                    new Vector2D(direction), 2));
        }
    }

    public boolean canFire() {
        return timeUntilNextBullet <= 0;
    }

    public final void fire() {
        doFire();
        this.timeUntilNextBullet = (long) (1_000_000_000L / this.fireRate);
    }

    public void doDraw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);

        float healthPercent = Math.min(1, health / maxHealth);
        g.setColor(Color.getHSBColor(Math.min(healthPercent * .35f, 1f), 1f, 1f));
        g.fillRect((int) (size * 1.15), (int) (size * 1.15), (int) (healthPercent * size * 1.5), (int) (size * 0.3));

        float loadPercent = Math.min(1, 1 - (timeUntilNextBullet / (float) (1_000_000_000L / 4)));
        g.setColor(this.canFire() ? Color.gray : Color.yellow);
        g.fillRect((int) (size * 1.15), (int) (size * 1.5), (int) (loadPercent * size * 1.5), (int) (size * 0.3));

        g.rotate(direction.angle() + Math.PI / 2);
        g.setColor(getColour(this.isRegenerating() ? new Color(0, 1f, .8f) : color));

        g.fillPolygon(XP, YP, XP.length);
        if (ctrl.action().thrust != 0) {
            g.setColor(getColour(thrustColor));
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);
    }

    @Override
    public Rectangle getHitbox() {
        double s = size * DRAWING_SCALE;
        return new Rectangle((int) (position.x - s), (int) (position.y - s), (int) (s * 2), (int) (s * 2));
    }

    public void onHit(GameObject other) {
        if (dead && lives > 0) {
            dead = false;
            this.invulnerableFor(3);
            game.addObject(Title.showTitle(game.view, String.format("Hull breach! %d hits until critical.", game.getLives()), Color.red, 3, 0, 1));
        }
    }

    @Override
    public final void hit(GameObject other) {
        float health = this.health - (float) other.size * 0.7f;
        while (health <= 0) {
            health += maxHealth;
            lives--;
            dead = true;
        }

        this.health = health;
        this.regenFor(3);

        onHit(other);
    }

    @Override
    public void update(double dt) {
        synchronized (Game.class) {
            velocity.mult(1 - DRAG);

            if (ctrl.action().turnLeft ^ ctrl.action().turnRight) {
                if (ctrl.action().turnLeft)
                    direction.rotate(-STEER_RATE * DT);
                if (ctrl.action().turnRight)
                    direction.rotate(STEER_RATE * DT);
            }

            Vector2D thrust = new Vector2D(direction).mult(MAG_ACC).mult(Math.abs(ctrl.action().thrust));
            velocity.add(thrust);

            if (this.timeInvulnerable > 0)
                this.timeInvulnerable -= dt;
            if (this.timeUntilRegen > 0)
                this.timeUntilRegen -= dt;
            if (this.timeUntilNextBullet > 0)
                this.timeUntilNextBullet -= dt;

            if (this.isRegenerating())
                this.health += dt * 0.000_000_001;

            this.health = Math.max(0, Math.min(health, maxHealth));
            super.update(dt);
        }
    }

    @Override
    public boolean collidesWith(GameObject other) {
        return this.timeInvulnerable <= 0 && (other instanceof Asteroid || other instanceof Bullet) && other.collidesWith(this);
    }


    @Override
    public String toString() {
        return String.format("%s{health=%.0f/%d, %s, %s}", getName(), health, maxHealth, position, velocity);
    }
}
