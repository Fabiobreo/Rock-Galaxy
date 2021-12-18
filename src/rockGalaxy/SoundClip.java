package rockGalaxy;
/**
 * Class for handling clip files
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip extends Sound
{
    private AudioInputStream sample;
    private Clip clip;

    /**
     * Constructor
     */
    public SoundClip() {
        try
        {
            clip = AudioSystem.getClip();

        }
        catch (LineUnavailableException e)
        {
        	
        }
    }

    /**
     * Constructor
     * @param filename: the clip to load
     */
    public SoundClip(String filename)
    {
        this();
        load(filename);
    }
    
    /**
     * Get the clip
     * @return the current playing clip
     */
    public Clip getClip()
    {
    	return clip;
    }

    @Override
    public boolean load(String audiofile)
    {
        try
        {
            setFilename(audiofile);
            sample = AudioSystem.getAudioInputStream(getURL(getFilename()));
            clip.open(sample);
            return true;

        }
        catch (IOException e)
        {
            return false;
        }
        catch (UnsupportedAudioFileException e)
        {
            return false;
        }
        catch (LineUnavailableException e)
        {
            return false;
        }
    }
    
    @Override
    public boolean isLoaded()
    {
        return (boolean)(sample != null);
    }

    @Override
    public void play()
    {
        if (!isLoaded())
        {
        	return;
        }
        clip.setFramePosition(0);
        if (getLooping())
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else
        {
            clip.loop(getRepeat());
        }
    }

    @Override
    public void stop()
    {
        clip.stop();
    }

}
