package ticTacToe;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {
    private Clip clip;

    public SoundManager(String soundFileName) {
        try {
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(soundFileName);
            if (audioSrc == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFileName);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            audioInputStream.close();
        } catch (Exception e) {
            System.err.println("Error loading sound file: " + soundFileName);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public static void playSound(String soundFileName) {
        SoundManager soundManager = new SoundManager(soundFileName);
        soundManager.play();
    }
}
