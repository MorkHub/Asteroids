package game1;

import java.awt.*;
import java.util.Random;

class Constants {
    public static final boolean FULLSCREEN = false;
    public static final boolean OVERLAY = false;
    public static final double DRAWING_SCALE = 0.2;
    public static final Random r = new Random();
    public static Game game;

    // used to cap the frame rate to primary display refresh rate
    public static final int DISPLAY_REFRESH_RATE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    public static final int DELAY = 1000 / (DISPLAY_REFRESH_RATE != DisplayMode.REFRESH_RATE_UNKNOWN ? DISPLAY_REFRESH_RATE : 30);  // in milliseconds
    public static final double DT = DELAY / 1000.0;  // in seconds

    // get display resolution
    public static final int FRAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int FRAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final Dimension FRAME_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    // UI fonts
    public static final Font UI_FONT = new Font("monospace", Font.PLAIN, 12);
    public static final Font UI_TITLE_FONT = new Font("sans", Font.PLAIN, 32);
}
