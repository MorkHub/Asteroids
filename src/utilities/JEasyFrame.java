package utilities;

import javax.swing.*;
import java.awt.*;

public class JEasyFrame extends JFrame {
    public Component comp;

    public JEasyFrame(Component comp, String title, boolean OVERLAY, boolean FULLSCREEN) {
        super(title);
        this.comp = comp;
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

    public JEasyFrame(Component comp, String title) {
        super(title);
        this.comp = comp;
        getContentPane().add(BorderLayout.CENTER, comp);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
