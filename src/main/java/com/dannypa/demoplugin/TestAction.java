package com.dannypa.demoplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Using the event, evaluate the context,
        // and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, implement an action.
        // For example, create and show a dialog.
        try {
            BufferedImage meme = ImageIO.read(new File("/home/dannypa/winter-arc/jb-internships/demo-plugin/src/main/resources/meme.png"));

            MemeResizePanel panel = new MemeResizePanel(meme);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 1000);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(panel);
            frame.setVisible(true);
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }

    // Override getActionUpdateThread() when you target 2022.3 or later!

}