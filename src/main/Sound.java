package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Plays music and sound effects!
 */
public class Sound {

    private Clip clip;
    private URL soundURL[] = new URL[30];
    private float volume = 1; // Default volume is set to maximum (1.0)

    /**
     * Builds the sound object and loads each file
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/audio/Bad Piggies.wav");
        soundURL[1] = getClass().getResource("/audio/Elevator Music.wav");
        // soundURL[2] = getClass().getResource("");
        // soundURL[3] = getClass().getResource("");
    }

    /**
     * Loads and opens the audio file to be played by the play() method
     * @param i the audio file stored in array
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the clip
     */
    public void play() {
        clip.start();
    }

    /**
     * Pauses the clip
     */
    public void pause() {
        clip.stop();
    }

    /**
     * Loops the clip forever
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Sets the volume
     * @param volume the desired volume level (0.0 to 1.0)
     */
    public void setVolume(float volume) {
        // Checking if volume is in range
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));

        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * this.volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    /**
     * Returns the current volume level
     * @return the current volume level (0.0 to 1.0)
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Closes the clip
     */
	public void close() {
		clip.close();
	}
}
