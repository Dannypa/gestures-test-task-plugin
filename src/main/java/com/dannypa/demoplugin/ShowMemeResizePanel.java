package com.dannypa.demoplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ShowMemeResizePanel extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            URL memeResource = getClass().getClassLoader().getResource("meme.png");
            if (memeResource == null) {
                throw new IOException("No such file!");
            }
            BufferedImage meme = ImageIO.read(memeResource);

            new MemeResizeFrame(meme);
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }

    // Override getActionUpdateThread() when you target 2022.3 or later!

}