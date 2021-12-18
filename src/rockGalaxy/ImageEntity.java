package rockGalaxy;
/**
 * A representation of an image entity
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class ImageEntity extends BaseGameEntity
{
	protected Image image;
	protected Applet applet;
	protected AffineTransform affineTransform;
	protected Graphics2D graphics2d;

	/**
	 * Constructor
	 * @param app: the applet
	 */
	ImageEntity(Applet app) {
		applet = app;
		setImage(null);
		setAlive(true);
	}

	/**
	 * Get the image associated with the entity
	 * @return the image associated with the entity
	 */
	public Image getImage()
	{
		return image;
	}

	/**
	 * Set the image associated with the entity
	 * @param image: the image to associate
	 */
	public void setImage(Image image)
	{
		this.image = image;
		double x = applet.getSize().width/2  - getWidth()/2;
		double y = applet.getSize().height/2 - getHeight()/2;
		affineTransform = AffineTransform.getTranslateInstance(x, y);
	}

	/**
	 * Get the width of the image
	 * @return the width of the image, if any
	 */
	public int getWidth()
	{
		if (image != null)
		{
			return image.getWidth(applet);
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Get the height of the image
	 * @return the height of the image, if any
	 */
	public int getHeight()
	{
		if (image != null)
		{
			return image.getHeight(applet);
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Get the abscissa of the center of the image
	 * @return the abscissa of the center of the image
	 */
	public double getCenterX()
	{
		return getX() + getWidth() / 2;
	}

	/**
	 * Get the ordinate of the center of the image
	 * @return the ordinate of the center of the image
	 */
	public double getCenterY()
	{
		return getY() + getHeight() / 2;
	}

	/**
	 * Set the graphics of the image
	 * @param graphics: the graphics of the image
	 */
	public void setGraphics(Graphics2D graphics)
	{
		graphics2d = graphics;
	}
	
	/**
	 * Get the graphics of the image
	 * @return
	 */
	public Graphics2D getGraphics()
	{
		return graphics2d;
	}

	/**
	 * Get the path to the image
	 * @param filename: the name of the image to search
	 * @return the absolute path of the image
	 */
	private URL getURL(String filename)
	{
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		}
		catch (Exception e)
		{

		}
		return url;
	}

	/**
	 * Load the image
	 * @param filename: the name of the image to load
	 */
	public void load(String filename)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(getURL(filename));
		while (getImage().getWidth(applet) <= 0);
		double x = applet.getSize().width/2  - getWidth()/2;
		double y = applet.getSize().height/2 - getHeight()/2;
		affineTransform = AffineTransform.getTranslateInstance(x, y);
	}

	/**
	 * Change the affine transform associated with the image
	 */
	public void transform()
	{
		affineTransform.setToIdentity();
		affineTransform.translate((int)getX() + getWidth()/2, (int)getY() + getHeight()/2);
		affineTransform.rotate(Math.toRadians(getFaceAngle()));
		affineTransform.translate(-getWidth()/2, -getHeight()/2);
	}
	
	/**
	 * Return the affine transform of the entity
	 * @return the affine transform of the entity
	 */
	public AffineTransform getAffineTransform()
	{
		return affineTransform;
	}

	/**
	 * Draw the entity on the screen
	 */
	public void draw()
	{
		graphics2d.drawImage(getImage(), affineTransform, applet);
	}

	/**
	 * Get a rectangle representing the bounds of the image
	 * @return a rectangle bounding the image
	 */
	public Rectangle getBounds()
	{
		Rectangle r;
		r = new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight());
		return r;
	}

}
