package TikTakTu;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundEffect {
    private Clip clip;

    public SoundEffect(String soundFileName) {
        try {
            // Use getResourceAsStream to load files inside the src folder
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.stop();               // Stop any previous sound
            clip.setFramePosition(0);   // Rewind to the beginning
            clip.start();               // Start playing
        }
    }
}