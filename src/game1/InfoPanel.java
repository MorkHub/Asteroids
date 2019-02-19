package game1;

import utilities.Vector2D;

import java.awt.*;

public class InfoPanel {

    private BasicGame game;
    private long ns = System.nanoTime();
    private String output = "";

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

        this.output = String.format("FPS: %s\nPOS: %s\nVEL: %s\nDIR: %s\n", fps, ship.getPosition(), ship.getVelocity(), ship.getDirection());
    }

    public void draw(Graphics2D g) {
        int h = g.getFontMetrics().getHeight();
        int x = (int) pos.x;
        int y = (int) pos.y;

        g.setColor(Color.GREEN);
        g.setFont(Constants.UI_FONT);

        String[] lines = output.split("\n");

        int screenMax = game.view.getHeight() / h;
        int first = Math.min(0, 0);

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

