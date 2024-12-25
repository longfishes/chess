package com.longfish;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import static com.longfish.CommonConstant.RESOURCE_PATH;

public class Utility {

    public static void playSound(String soundFileName) {
        try {
            File soundFile = new File(RESOURCE_PATH + "audio\\" + soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("音频加载失败" + e.getMessage());
            e.printStackTrace();
        }
    }
}
