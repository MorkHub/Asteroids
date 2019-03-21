package game1;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.DRAWING_SCALE;
import static game1.Constants.game;

public class AlphaBullet extends Bullet {
    private static Font BULLET_FONT = new Font("monospace", Font.PLAIN, 128);

    private char c;

    public AlphaBullet(char c, Vector2D position, Vector2D velocity, Vector2D direction, int lifetime) {
        super(position, velocity, direction, lifetime);
        this.c = c;
    }

    @Override
    public Rectangle getHitbox() {
        FontMetrics f = game.view.getGraphics().getFontMetrics(BULLET_FONT);
        int h = (int) (f.getHeight() * DRAWING_SCALE);
        int w = (int) (f.stringWidth(String.valueOf(c)) * DRAWING_SCALE);

        return new Rectangle((int) position.x - (w/2), (int) position.y - (h/2), w, h );
    }

    @Override
    public void doDraw(Graphics2D g) {
        String c = String.valueOf(this.c).trim();
        if (c.isEmpty())
            super.doDraw(g);

        AffineTransform at = g.getTransform();
        Font o = g.getFont();

        g.setColor(color);
        g.setFont(BULLET_FONT);

        int h = g.getFontMetrics().getHeight();
        int w = g.getFontMetrics().stringWidth(c);

        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(DRAWING_SCALE , DRAWING_SCALE);

        g.drawString(c, -(int) ((double) w/2d), (int)((double) h/4d));
        g.setTransform(at);
        g.setFont(o);
    }
}
