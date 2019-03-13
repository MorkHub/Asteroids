package game1;

import utilities.Vector2D;

import java.awt.*;

public class InfoPanel {

    private Game game;
    private long ns = System.nanoTime();
    private String left = "";
    private String right = "";

    private Vector2D pos;

    public InfoPanel(Game game) {
        this(20, 20, game);
    }

    public InfoPanel(double x, double y, Game game) {
        this.pos = new Vector2D(x, y);
        this.game = game;
    }

    public void update() {
        Ship ship = game.ship;
        long fps = 1_000_000_000L / (-ns + (ns=System.nanoTime()));

        this.left = String.format("FPS: %s\nPOS: %s\nVEL: %s\nDIR: %s", fps, ship.getPosition(), ship.getVelocity(), ship.getDirection());
        this.right = String.format("LEVEL: %,d\nLIVES: %,d\nSCORE: %,d\nTARGETS: %,d", game.getLevel(), game.getLives(), game.getScore(), game.asteroids());
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

        if (!game.ctrl.action().info) return;

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

