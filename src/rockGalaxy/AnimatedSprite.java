package rockGalaxy;
/**
 * An animation for a sprite
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite
{
    private ImageEntity animationImage;
    private BufferedImage tempImage;
    private Graphics2D tempSurface;
    
    private int currentFrame;
    private int totFrames;
    private int animationDirection;
    private int frameCount;
    private int frameDelay;
    private int frameWidth;
    private int frameHeight;
    private int numberOfAnimations;

    /**
     * Constructor
     * @param applet
     * @param g2d
     */
    public AnimatedSprite(Applet applet, Graphics2D g2d)
    {
        super(applet, g2d);
        animationImage = new ImageEntity(applet);
        currentFrame = 0;
        totFrames = 0;
        animationDirection = 1;
        frameCount = 0;
        frameDelay = 0;
        frameWidth = 0;
        frameHeight = 0;
        numberOfAnimations = 0;
    }

    /**
     * Get the current frame of the animation
     * @return the current frame
     */
    public int getCurrentFrame()
    {
    	return currentFrame;
    }
    
    /**
     * Set the current frame of the animation
     * @param frame
     */
    public void setCurrentFrame(int frame)
    {
    	currentFrame = frame;
    }

    /**
     * Get the frame width
     * @return the frame width
     */
    public int getFrameWidth()
    {
    	return frameWidth;
    }
    
    public void setFrameWidth(int width)
    {
    	frameWidth = width;
    }

    public int getFrameHeight()
    {
    	return frameHeight;
    }
    
    public void setFrameHeight(int height)
    {
    	frameHeight = height;
    }

    public int totalFrames()
    {
    	return totFrames;
    }
    
    public void setTotalFrames(int total)
    {
    	totFrames = total;
    }

    public int animationDirection()
    {
    	return animationDirection;
    }
    
    public void setAnimationDirection(int direction)
    {
    	animationDirection = direction;
    }

    public int frameDelay()
    {
    	return frameDelay;
    }
    
    public void setFrameDelay(int delay)
    {
    	frameDelay = delay;
    }

    /**
     * Get the number of images the sprite is composed of
     * @return the column number
     */
    public int getNumberOfAnimations()
    {
    	return numberOfAnimations;
    }
    
    /**
     * Set the number of images the sprite is composed of
     * (seeing the sprite as a matrix of images)
     * @param num
     */
    public void setNumberOfAnimations(int num)
    {
    	numberOfAnimations = num;
    }

    /**
     * Get the image associated with the animation
     * @return the image associated with the animation
     */
    public Image getAnimImage()
    {
    	return animationImage.getImage();
    }
    
    /**
     * Set the image for the animation
     * @param image: the image to set
     */
    public void setAnimImage(Image image)
    {
    	animationImage.setImage(image);
    }

    /**
     * Update the animation
     */
    public void updateAnimation()
    {
        frameCount += 1;
        if (frameCount > frameDelay)
        {
            frameCount = 0;
            currentFrame += animationDirection;
            if (currentFrame > totFrames - 1) {
                currentFrame = 0;
            }
            else if (currentFrame < 0) {
                currentFrame = totFrames - 1;
            }
        }
    }

    /**
     * Update the animation frame
     */
    public void updateFrame()
    {
        if (totFrames > 0)
        {
            int frameX = (getCurrentFrame() % getNumberOfAnimations()) * getFrameWidth();
            int frameY = (getCurrentFrame() / getNumberOfAnimations()) * getFrameHeight();

            if (tempImage == null) {
                tempImage = new BufferedImage(getFrameWidth(), getFrameHeight(),
                                              BufferedImage.TYPE_INT_ARGB);
                tempSurface = tempImage.createGraphics();
            }

            if (animationImage.getImage() != null) {
                tempSurface.drawImage(animationImage.getImage(), 0, 0, getFrameWidth() - 1,
                		getFrameHeight() - 1, frameX, frameY,
                frameX + getFrameWidth(),
                frameY + getFrameHeight(), getApplet());
            }
            super.setImage(tempImage);
        }
    }

}

