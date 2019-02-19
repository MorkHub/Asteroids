package game1;

import javax.swing.*;
import java.awt.*;

public class BasicView extends JComponent {
    public static final Color BG_COLOR = Color.black;
    float hue = 0;

    private BasicGame game;

    public BasicView(BasicGame game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics g0) {
        synchronized (BasicGame.class) {
            Graphics2D g = (Graphics2D) g0;

            if (!Constants.OVERLAY) {
                g.setColor(BG_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            game.objects.forEach(o -> o.draw(g));
            game.ship.draw(g);
            game.info.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}
