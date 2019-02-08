package game1;

import java.awt.*;

class Constants {
    public static final int FRAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int FRAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final Dimension FRAME_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DISPLAY_REFRESH_RATE =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    public static final int DELAY = 1000 / DISPLAY_REFRESH_RATE;  // in milliseconds
    public static final double DT = DELAY / 1000.0;  // in seconds
}
