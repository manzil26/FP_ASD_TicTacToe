package ticTacToe;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.io.IOException;

public class SoundManager {

    public static void playSound(String soundFile) {
        try {
            // Try loading the resource from the current package
            InputStream sound = SoundManager.class.getResourceAsStream("/" + soundFile);
            if (sound == null) {
                System.out.println("Sound file not found: " + soundFile);
                return;
            }

            // Create an AudioInputStream from the InputStream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();  // Get a clip to play the sound
            clip.open(audioIn);  // Open the audio input stream
            clip.start();  // Start playing the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
