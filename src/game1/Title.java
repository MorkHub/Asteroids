package game1;

import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class Title extends GameObject {
    private static List<Title> queue = new LinkedList<>();

    private Color color;
    private String text;
    private long alive = 0;
    private long duration = 1;
    private long fadeIn = 0;
    private long fadeOut = 0;
    private JComponent comp;

    public Title(Vector2D position, Vector2D velocity, Vector2D direction, double size, Color color, String text, long duration, long fadeIn, long fadeOut, JComponent comp) {
        super(position, velocity, direction, size);
        this.color = color;
        this.text = text;
        this.duration = duration;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.comp = comp;
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        alive += dt;
        dead = alive >= (fadeIn + duration + fadeOut);
    }

    private double opacity() {
        if (alive <= fadeIn) {
            return (double) alive / (double) fadeIn;
        }

        if (alive >= fadeIn + duration) {
            return Math.min(Math.max(0, 1 - ((double) alive - ((double) fadeIn + (double) duration)) / (double) fadeOut), 1);
        }

        return 1f;
    }

    @Override
    public void doDraw(Graphics2D g) {
        Font f = g.getFont();
        g.setFont(Constants.TITLE_FONT);

        int height = g.getFontMetrics().getHeight();
        int width = g.getFontMetrics().stringWidth(text);

        g.setColor(new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, (float) opacity()));
        g.drawString(text, (int) position.x - width / 2, (int) position.y - height / 2);
        g.setFont(f);
    }

    public static Title showTitle(JComponent frame, String text, Color color, float duration, float fadeIn, float fadeOut) {
        int width = frame.getWidth();
        int height = frame.getHeight();
        long ns = 1_000_000_000;

        return new Title(
                new Vector2D(width / 2, height * 0.1),
                new Vector2D(0, 0),
                new Vector2D(0, 0),
                0, color, text,
                (long) (duration * ns),
                (long) (fadeIn * ns),
                (long) (fadeOut * ns), frame);
    }


    public static Title showTitle(JComponent comp, String text, Color color, float duration, float fade) {
        return showTitle(comp, text, color, duration, fade, fade);
    }

    public static Title showTitle(JComponent comp, String text, Color color, float duration) {
        return showTitle(comp, text, color, duration, 1, 1);
    }

    public static Title showTitle(JComponent comp, String text, Color color) {
        return showTitle(comp, text, color, 1, 3, 1);
    }

    public static Title showTitle(JComponent comp, String text) {
        return showTitle(comp, text, Color.white, 1, 3, 1);
    }
}
