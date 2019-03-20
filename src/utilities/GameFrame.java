package utilities;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(Component comp, String title, boolean OVERLAY, boolean FULLSCREEN) {
        super(title);
        getContentPane().add(BorderLayout.CENTER, comp);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (FULLSCREEN || OVERLAY) {
            setResizable(false);
            setUndecorated(true);
            if (OVERLAY) {
                setBackground(new Color(0, 0, 0, 0));
                setAlwaysOnTop(true);
            }
        }

        pack();
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        setVisible(true);
    }

    @Override
    public void paintComponents(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponents(g);
    }
}
