/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231037 - Al-khiqmah Manzilatul Mukaromah
 * 2 - 5026231095 - Akhtar Zia Faizarrobbi
 * 3 - 5026231227 - Arjuna Veetaraq
 */
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
