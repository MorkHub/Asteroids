package game1;

import utilities.Vector2D;

import java.awt.*;

public class InfoPanel extends GameObject {

    private Game game;
    private long ns = System.nanoTime();
    private String left = "";
    private String right = "";

    public InfoPanel(double x, double y, Game game) {
        super(new Vector2D(x, y), new Vector2D(0, 0), new Vector2D(0, 0), 0);
        this.game = game;
    }

    public InfoPanel(Game game) {
        this(20, 20, game);
    }

    @Override
    public void update(double dt) {
        Ship ship = game.ship;
        long fps = 1_000_000_000L / (-ns + (ns=System.nanoTime()));

        this.left = String.format("FPS: %s\nPOS: %s\nVEL: %s\nDIR: %s", fps, ship.getPosition(), ship.getVelocity(), ship.getDirection());
        this.right = String.format("LEVEL: %,d\nHEALTH: %s\nLIVES: %,d\nSCORE: %,d\nTARGETS: %,d", game.getLevel(), game.ship.health, game.getLives(), game.getScore(), game.asteroids());
    }

    @Override
    public void doDraw(Graphics2D g) {
        int h = g.getFontMetrics().getHeight();
        int x = (int) position.x;
        int y = (int) position.y;

        g.setColor(Color.GREEN);
        g.setFont(Constants.UI_FONT);

        String[] lines = right.split("\n");
        for (String s : lines) {
            g.drawString(s, game.view.getWidth() - x - g.getFontMetrics().stringWidth(s), y);
            y += h;
        }

        if (!game.ctrl.action().info) return;

        y = (int) position.y;
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

