package com.dannypa.demoplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ShowMouseFollowAndResizePanel extends AnAction {
    private void showMeme() {
        try {
            URL memeResource = getClass().getClassLoader().getResource("meme.png");
            if (memeResource == null) {
                throw new IOException("No such file!");
            }
            BufferedImage meme = ImageIO.read(memeResource);

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(meme, 0, 0, getWidth(), getHeight(), this);
                }
            };
            new MouseFollowAndResizeFrame(
                    panel,
                    new Dimension(
                            meme.getWidth(),
                            meme.getHeight()
                    )
            );
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }

    private void showProgressBar() {
        JPanel panel = new PreprocessingPanel(new SwingWorker<>() {
            private final int POWER = 1_000_000_000;
            private final int MOD = 1_000_000_007;

            @Override
            protected Void doInBackground() {
                System.out.println("started background");
                int result = 1;
                setProgress(0);
                for (int progress = 0; progress <= POWER; progress++) {
                    result = (result * 2) % MOD; // Never mind, java overflow is goofy like that
                    int percent = POWER / 100;
                    if ((progress % percent) == 0) {
                        setProgress(progress / percent);
                    }
                }
                System.out.println(result);
                return null;
            }
        });

        new MouseFollowAndResizeFrame(
                panel,
                new Dimension(
                        MouseFollowAndResizeFrame.SCREEN_WIDTH / 2,
                        MouseFollowAndResizeFrame.SCREEN_HEIGHT / 2
                )
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
//        showMeme();
        showProgressBar();
    }

    // Override getActionUpdateThread() when you target 2022.3 or later!

}