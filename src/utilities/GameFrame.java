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
}
