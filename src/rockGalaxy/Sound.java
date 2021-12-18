package rockGalaxy;
/**
 * Basic representation of a sound
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.net.URL;
public abstract class Sound
{
    private String filename = "";
    private boolean looping = false;
    private int repeat = 0;
    
    /**
     * Default constructor
     */
    public Sound()
    {
    	
    }
    
    /**
     * Set the filename of the song
     * @param file: the filename of the song
     */
    public void setFilename(String file)
    {
    	filename = file;
    }
    
    /**
     * Get the filename of the song
     * @return the filename of the song
     */
    public String getFilename()
    {
    	return filename;
    }
    
    /**
     * Get if the song is in loop
     * @return if the song is played in loop
     */
    public boolean getLooping()
    {
    	return looping;
    }
    
    /**
     * Set if the song must be played continuously
     * @param loop
     */
    public void setLooping(boolean loop)
    {
    	looping = loop;
    }
    
    /**
     * Get the number of repetition of the song
     * @return the number of repetition of the song
     */
    public int getRepeat()
    {
    	return repeat;
    }
    
    /**
     * Set the number of times the song must be repeated
     * @param rep
     */
    public void setRepeat(int rep)
    {
    	repeat = rep;
    }

    /**
     * Get the absolute path to the filename
     * @param filename
     * @return the absolute path
     */
    public URL getURL(String filename)
    {
        URL url = null;
        try
        {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e)
        {
        	
        }
        return url;
   }

    /**
     * Load the file
     * @param file
     * @return true if the load was successful
     */
    public abstract boolean load(String file);
    
    /**
     * If the file is loaded
     * @return true if it's loaded
     */
    public abstract boolean isLoaded();

    /**
     * Start playing the song
     */
    public abstract void play();

    /**
     * Stop reproducing the song
     */
    public abstract void stop();

}
