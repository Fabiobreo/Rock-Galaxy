package rockGalaxy;
/**
 * Class handling midi files
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiSequence extends Sound
{
    private Sequencer sequencer;
    private Sequence song;
    
    /**
     * Default constructor
     */
    public MidiSequence() {
        try
        {
            sequencer = MidiSystem.getSequencer();
        }
        catch (MidiUnavailableException e)
        {
        	
        }
    }

    /**
     * Constructor
     * @param filename: the midi to load
     */
    public MidiSequence(String filename)
    {
        this();
        load(filename);
    }
    
    /**
     * Get the song
     * @return the current playing song
     */
    public Sequence getSong()
    {
    	return song;
    }

    @Override
    public boolean load(String file)
    {
        try
        {
            setFilename(file);
            song = MidiSystem.getSequence(getURL(getFilename()));
            sequencer.setSequence(song);
            sequencer.open();
            return true;

        }
        catch (InvalidMidiDataException e)
        {
            return false;
        }
        catch (MidiUnavailableException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
    }
    
    @Override
    public boolean isLoaded()
    {
        return (boolean)(sequencer.isOpen());
    }


    @Override
    public void play()
    {
        if (!sequencer.isOpen()) return;

        if (getLooping()) {
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
        } else {
            sequencer.setLoopCount(getRepeat());
            sequencer.start();
        }
    }

    @Override
    public void stop()
    {
        sequencer.stop();
    }

}
