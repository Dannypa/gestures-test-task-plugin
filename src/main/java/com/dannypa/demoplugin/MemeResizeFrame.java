package com.dannypa.demoplugin;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Frame that handles the MouseResize panel.
 */
public class MemeResizeFrame extends JFrame {
    final int SCREEN_WIDTH = 1000;
    final int SCREEN_HEIGHT = 1000;

    MemeResizeFrame(BufferedImage meme) {
        MemeResizePanel panel = new MemeResizePanel(meme);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}
