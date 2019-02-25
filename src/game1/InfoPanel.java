package game1;

import utilities.Vector2D;

import java.awt.*;

public class InfoPanel {

    private BasicGame game;
    private long ns = System.nanoTime();
    private String left = "";
    private String right = "";

    private Vector2D pos;

    public InfoPanel(BasicGame game) {
        this(20, 20, game);
    }

    public InfoPanel(double x, double y, BasicGame game) {
        this.pos = new Vector2D(x, y);
        this.game = game;
    }

    public void update() {
        BasicShip ship = game.ship;
        long fps = 1_000_000_000L / (-ns + (ns=System.nanoTime()));

        this.left = String.format("FPS: %s\nPOS: %s\nVEL: %s\nDIR: %s", fps, ship.getPosition(), ship.getVelocity(), ship.getDirection(), game.score);
        this.right = String.format("LIVES: %d\nSCORE: %,d", game.lives, game.score);
    }

    public void draw(Graphics2D g) {
        int h = g.getFontMetrics().getHeight();
        int x = (int) pos.x;
        int y = (int) pos.y;

        g.setColor(Color.GREEN);
        g.setFont(Constants.UI_FONT);

        String[] lines = right.split("\n");
        for (String s : lines) {
            g.drawString(s, game.view.getWidth() - x - g.getFontMetrics().stringWidth(s), y);
            y += h;
        }

        y = (int) pos.y;
        lines = left.split("\n");
        for (String s : lines) {
            g.drawString(s, x, y);
            y += h;
        }

        for (GameObject o : game.objects) {
            g.drawString(o.toString(), x, y);
            y += h;
        }
    }
}

