package game1;

import java.awt.*;
import java.util.Random;

class Constants {
    public static final boolean FULLSCREEN = false;
    public static final boolean OVERLAY = false;
    public static final int FRAME_HEIGHT = !FULLSCREEN ? 480 : Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int FRAME_WIDTH = !FULLSCREEN ? 640 : Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final Dimension FRAME_SIZE = !FULLSCREEN ? new Dimension(FRAME_WIDTH, FRAME_HEIGHT) : Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DISPLAY_REFRESH_RATE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    public static final int DELAY = 1000 / (DISPLAY_REFRESH_RATE != DisplayMode.REFRESH_RATE_UNKNOWN ? DISPLAY_REFRESH_RATE : 30);  // in milliseconds
    public static final double DT = DELAY / 1000.0;  // in seconds
    public static final Font UI_FONT = new Font("monospace", Font.PLAIN, 12);
    public static final double DRAWING_SCALE = 0.2;
    public static BasicGame game;
    public static final Random r = new Random();
}
