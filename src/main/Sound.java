package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * 
 * @author Jason Wong
 * Plays music and sound effects!
 *
 */
public class Sound {

	private Clip clip;
	private URL soundURL[] = new URL[30];
	/**
	 * Builds the sound object and loads each file
	 */
	public Sound() {

		soundURL[0] = getClass().getResource("Bad Piggies.wav");
		soundURL[1] = getClass().getResource("");
		soundURL[2] = getClass().getResource("");
		soundURL[3] = getClass().getResource("");
	}

	/**
	 * Loads and opens the audio file to be played by the play() method<br>
	 * pre: i should have a corresponding file in the soundURL array<br>
	 * @param i the audio file stored in array
	 */
	public void setFile(int i) {

		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);

		} catch (Exception e) {

		}
	}

	/**
	 * Plays the clip <br>pre: a clip must be previously opened <br>post: the clip is played
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

}
