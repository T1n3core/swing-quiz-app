import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays a WAV audio file in a loop as background music.
 * Runs in a separate thread and supports stopping playback.
 * 
 * @author GasTheJuice
 */
public class BackgroundMusicPlayer extends Thread {
    /** Path to the audio file to play */
    private final String filePath;
    
    /** Controls whether the music should continue playing */
    private volatile boolean isPlaying = true;

    /**
     * Constructs a new background music player with the specified audio file.
     * 
     * @param filePath path to the WAV file (e.g., "audio/background.wav")
     */
    public BackgroundMusicPlayer(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Starts playback of the audio file in a loop.
     * Reads audio data in chunks and writes to the sound line.
     * Resets the stream to the beginning when EOF is reached.
     * 
     * <p>Handles {@code UnsupportedAudioFileException}, {@code IOException}, 
     * and {@code LineUnavailableException} by printing stack trace.</p>
     */
    @Override
    public void run() {
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath))) {
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                line.open(format);
                line.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while (isPlaying) {
                    audioIn.mark(Integer.MAX_VALUE);
                    while ((bytesRead = audioIn.read(buffer, 0, buffer.length)) != -1 && isPlaying) {
                        line.write(buffer, 0, bytesRead);
                    }
                    audioIn.reset();
                }
                line.drain();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the music playback immediately.
     * Sets the playing flag to false, causing the loop to exit.
     */
    public void stopMusic() {
        isPlaying = false;
    }
}