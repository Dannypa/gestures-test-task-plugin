package com.dannypa.demoplugin;

import javax.swing.*;
import java.awt.*;

/**
 * Frame that handles the MouseResize panel.
 */
public class MouseFollowAndResizeFrame extends JFrame {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;

    MouseFollowAndResizeFrame(Component targetComponent, Dimension targetComponentSize) {
        MouseFollowAndResizePanel panel = new MouseFollowAndResizePanel(
                targetComponent, targetComponentSize
        );
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}
