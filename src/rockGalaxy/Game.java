package rockGalaxy;
/**
 * An abstract representation of the game applet
 * @author Ing. Fabio Brea
 * @version 1.1
 */

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

abstract class Game extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener
{

	private static final long serialVersionUID = -2065238822360203370L;
	
    abstract void gameStartup();
    abstract void gameTimedUpdate();
    abstract void gameRefreshScreen();
    abstract void gameShutdown();
    abstract void gameKeyDown(int keyCode);
    abstract void gameKeyUp(int keyCode);
    abstract void gameMouseDown();
    abstract void gameMouseUp();
    abstract void gameMouseMove();
    abstract void spriteUpdate(AnimatedSprite sprite);
    abstract void spriteDraw(AnimatedSprite sprite);
    abstract void spriteDying(AnimatedSprite sprite);
    abstract void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2);

	private Thread gameloop;
    private LinkedList<AnimatedSprite> spritesList;
    private BufferedImage backbuffer;
    private Graphics2D g2d;
    private int screenWidth;
    private int screenHeight;

    private Point2D mousePos = new Point2D(0, 0);
    private boolean mouseButtons[] = new boolean[4];

    private int frameCount = 0;
    private int frameRate = 0;
    private int desiredRate;
    private long startTime = System.currentTimeMillis();
    private boolean gamePaused = false;
    
    /**
     * Get the applet of the game
     * @return the applet
     */
    public Applet getApplet()
    {
    	return this;
    }

    /**
     * Return true if the game is paused, false otherwise
     * @return if the game is paused
     */
    public boolean isGamePaused()
    {
    	return gamePaused;
    }
    
    /**
     * Pause the game
     */
    public void pauseGame()
    {
    	gamePaused = true;
    }
    
    /**
     * Exit from pause status
     */
    public void resumeGame()
    {
    	gamePaused = false;
    }
    
    /**
     * Get sprites of the game
     * @return a list of sprites
     */
    public LinkedList<AnimatedSprite> getSprites()
    {
    	return spritesList;
    }

    /**
     * Constructor
     * @param frameRate the framerate of the game
     * @param width the width of the window
     * @param height the height of the window
     */
    public Game(int frameRate, int width, int height)
    {
        desiredRate = frameRate;
        screenWidth = width;
        screenHeight = height;
    }

    public Graphics2D graphics()
    {
    	return g2d;
    }

    /**
     * Get the frame rate of the game
     * @return the frame rate
     */
    public int getFrameRate()
    {
    	return frameRate;
    }

    public boolean mouseButton(int btn)
    {
    	return mouseButtons[btn];
    }
    
    /**
     * Return the position the mouse is
     * @return a point with the 2d coordinates of the mouse position
     */
    public Point2D mousePosition()
    {
    	return mousePos;
    }

    /**
     * Initialize the applet
     */
    public void init()
    {
        backbuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();
        spritesList = new LinkedList<AnimatedSprite>();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        gameStartup();
        getApplet().setFocusable(true);
    }

    /**
     * Update the applet
     */
    public void update(Graphics g)
    {
        frameCount++;
        if (System.currentTimeMillis() > startTime + 1000)
        {
            startTime = System.currentTimeMillis();
            frameRate = frameCount;
            frameCount = 0;
            purgeSprites();
        }

        gameRefreshScreen();
        
        if (!isGamePaused())
        {
            drawSprites();
        }

        paint(g);
    }

     public void paint(Graphics g)
     {
         g.drawImage(backbuffer, 0, 0, this);
     }

     public void start()
     {
         gameloop = new Thread(this);
         gameloop.start();
     }
     
     /**
      * Run the applet
      */
     public void run()
     {
         Thread thread = Thread.currentThread();

         while (thread == gameloop)
         {
             try
             {
                 Thread.sleep(1000 / desiredRate);
             }
             catch(InterruptedException e)
             {
                 e.printStackTrace();
             }

             if (!isGamePaused()) {
                 updateSprites();
                 testCollisions();
             }
             gameTimedUpdate();
             repaint();
         }
     }

     /**
      * Stop the applet
      */
     public void stop()
     {
         gameloop = null;
         gameShutdown();
     }

     public void keyTyped(KeyEvent k)
     {
    	 
     }
     
     public void keyPressed(KeyEvent k)
     {
         gameKeyDown(k.getKeyCode());
     }
     
     public void keyReleased(KeyEvent k)
     {
         gameKeyUp(k.getKeyCode());
     }

     private void checkButtons(MouseEvent e)
     {
             switch(e.getButton())
             {
                case MouseEvent.BUTTON1:
                    mouseButtons[1] = true;
                    mouseButtons[2] = false;
                    mouseButtons[3] = false;
                    break;
                case MouseEvent.BUTTON2:
                    mouseButtons[1] = false;
                    mouseButtons[2] = true;
                    mouseButtons[3] = false;
                    break;
                case MouseEvent.BUTTON3:
                    mouseButtons[1] = false;
                    mouseButtons[2] = false;
                    mouseButtons[3] = true;
                    break;
             }
    }

     public void mousePressed(MouseEvent e)
     {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseDown();
     }
     
     public void mouseReleased(MouseEvent e)
     {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseUp();
     }
     
     public void mouseMoved(MouseEvent e)
     {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }
     
     public void mouseDragged(MouseEvent e)
     {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseDown();
         gameMouseMove();
     }
     
     public void mouseEntered(MouseEvent e)
     {
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }
     
     public void mouseExited(MouseEvent e)
     {
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }
     
     public void mouseClicked(MouseEvent e) { }

     /**
      * Calculate the x component of the angle in radiant
      * @param angle the angle in degree
      * @return the x component of the angle
      */
     protected double calculateAngleMoveX(double angle)
     {
         return (double)(Math.cos(angle * Math.PI / 180));
     }
     
     /**
      * Calculate the y component of the angle in radiant
      * @param angle the angle in degree
      * @return the y component of the angle
      */
     protected double calculateAngleMoveY(double angle)
     {
         return (double) (Math.sin(angle * Math.PI / 180));
     }

     /**
      * Update sprites position, rotation, animation, life and death animation
      */
     protected void updateSprites()
     {
         for (int i = 0; i < spritesList.size(); i++)
         {
             AnimatedSprite spr = (AnimatedSprite) spritesList.get(i);
             if (spr.alive())
             {
                 spr.updatePosition();
                 spr.updateRotation();
                 spr.updateAnimation();
                 spriteUpdate(spr);
                 spr.updateLifetime();
                 if (!spr.alive())
                 {
                     spriteDying(spr);
                 }
             }
         }
     }

     /**
      * Test collisions between sprites
      */
     protected void testCollisions()
     {
         for (int first=0; first < spritesList.size(); first++)
         {
             AnimatedSprite spr1 = (AnimatedSprite) spritesList.get(first);
             if (spr1.alive())
             {
                 for (int second = 0; second < spritesList.size(); second++)
                 {
                     if (first != second)
                     {
                         AnimatedSprite spr2 = (AnimatedSprite) spritesList.get(second);
                         if (spr2.alive())
                         {
                             if (spr2.collidesWith(spr1))
                             {
                                 spriteCollision(spr1, spr2);
                                 break;
                             }
                             else
                                spr1.setCollided(false);

                         }
                     }
                 }
             }
         }
     }

     /**
      * Draws alive sprites
      */
     protected void drawSprites()
     {
         for (int i = 0; i < spritesList.size(); i++)
         {
             AnimatedSprite spr = (AnimatedSprite) spritesList.get(i);
             if (spr.alive())
             {
                 spr.updateFrame();
                 spr.transform();
                 spr.draw();
                 spriteDraw(spr);
             }
         }
     }

     /**
      * Removes dead sprites
      */
     private void purgeSprites()
     {
    	 for (int i = 0; i < spritesList.size(); i++)
         {
             AnimatedSprite spr = (AnimatedSprite) spritesList.get(i);
             if (!spr.alive())
             {
                 spritesList.remove(i);
             }
         }
     }


}
