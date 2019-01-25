package game1;

import javax.swing.*;
import java.awt.*;

public class BasicView extends JComponent {
    // background colour
    public static final Color BG_COLOR = Color.black;
    float hue = 0;

    private BasicGame game;

    public BasicView(BasicGame game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        // paint the background
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

//        BasicAsteroid.hue = (BasicAsteroid.hue + 0.01f) % 1;

        for (BasicAsteroid a : game.asteroids) {
            a.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}
