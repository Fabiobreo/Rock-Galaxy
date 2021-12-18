package rockGalaxy;
/**
 * A representation of a sprite
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite
{
	private ImageEntity entity;
	protected Point2D pos;
	protected Point2D vel;
	protected double rotRate;
	protected int currentState;
	protected int sprType;
	protected boolean collided;
	protected int lifespan;
	protected int lifeage;

	/**
	 * Constructor
	 * @param app the applet
	 * @param g2d the graphics
	 */
	Sprite(Applet app, Graphics2D g2d)
	{
		entity = new ImageEntity(app);
		entity.setGraphics(g2d);
		entity.setAlive(false);
		pos = new Point2D(0.0, 0.0);
		vel = new Point2D(0.0, 0.0);
		rotRate = 0.0;
		currentState = 0;
		collided = false;
		lifespan = 0;
		lifeage = 0;
	}

	/**
	 * Load the corresponding sprite
	 * @param filename: the name of the sprite to load
	 */
	public void load(String filename)
	{
		entity.load(filename);
	}

	/**
	 * Apply the transform to the sprite
	 */
	public void transform()
	{
		entity.setX(pos.X());
		entity.setY(pos.Y());
		entity.transform();
	}

	/**
	 * Draws the sprite on the screen
	 */
	public void draw()
	{
		entity.graphics2d.drawImage(entity.getImage(),entity.affineTransform,entity.applet);
	}

	/**
	 * Draws bounds (collision detection)
	 * @param color the color of the rectangle to draw
	 */
	public void drawBounds(Color color)
	{
		entity.graphics2d.setColor(color);
		entity.graphics2d.draw(getBounds());
	}

	/**
	 * Update the sprite position, given the current velocity
	 */
	public void updatePosition()
	{
		pos.setX(pos.X() + vel.X());
		pos.setY(pos.Y() + vel.Y());
	}

	/**
	 * Get the rotation rate of the sprite
	 */
	public double getRotationRate()
	{
		return rotRate;
	}
	
	/**
	 * Set the rotation rate of the sprite
	 * @param rate the rotation rate to set
	 */
	public void setRotationRate(double rate)
	{
		rotRate = rate;
	}
	
	/**
	 * Update the sprite rotation, given the rotation rate and the current facing angle
	 */
	public void updateRotation()
	{
		setFaceAngle(getFaceAngle() + rotRate);
		if (getFaceAngle() < 0)
		{
			setFaceAngle(360 - rotRate);
		}
		else if (getFaceAngle() > 360)
		{
			setFaceAngle(rotRate);
		}
	}

	/**
	 * Get the state of the sprite
	 * @return the state of the sprite
	 */
	public int getState()
	{
		return currentState;
	}
	
	/**
	 * Set the state of the sprite
	 * @param state
	 */
	public void setState(int state)
	{
		currentState = state;
	}

	/**
	 * Get the sprite's bounds
	 * @return a rectangle representing the boundaries of the sprite
	 */
	public Rectangle getBounds()
	{
		return entity.getBounds();
	}

	/**
	 * Get the position of the sprite
	 * @return the position of the sprite
	 */
	public Point2D getPosition()
	{
		return pos;
	}
	
	/**
	 * Set the position of the sprite
	 * @param pos the position of the sprite
	 */
	public void setPosition(Point2D pos)
	{
		this.pos = pos;
	}

	/**
	 * Get the velocity of the sprite
	 * @return the velocity of the sprite
	 */
	public Point2D getVelocity()
	{
		return vel;
	}
	
	/**
	 * Set the velocity of the sprite
	 * @param vel the velocity of the sprite
	 */
	public void setVelocity(Point2D vel)
	{
		this.vel = vel;
	}

	/**
	 * Get the center of the sprite
	 * @return the center of the sprite
	 */
	public Point2D getCenter()
	{
		return(new Point2D(entity.getCenterX(), entity.getCenterY()));
	}

	/**
	 * Get the alive status of the sprite
	 * @return true if the sprite is alive
	 */
	public boolean alive()
	{
		return entity.isAlive();
	}
	
	/**
	 * Set the alive status of the sprite
	 * @param alive the status of the sprite
	 */
	public void setAlive(boolean alive)
	{
		entity.setAlive(alive);
	}

	/**
	 * Get the direction the sprite is facing
	 * @return  the direction the sprite is facing
	 */
	public double getFaceAngle()
	{
		return entity.getFaceAngle();
	}
	
	/**
	 * Set the direction the sprite is facing
	 * @param angle the direction of the sprite
	 */
	public void setFaceAngle(double angle)
	{
		entity.setFaceAngle(angle);
	}
	
	/**
	 * Set the direction the sprite is facing
	 * @param angle the direction of the sprite
	 */
	public void setFaceAngle(float angle)
	{
		entity.setFaceAngle((double) angle);
	}
	
	/**
	 * Set the direction the sprite is facing
	 * @param angle the direction of the sprite
	 */
	public void setFaceAngle(int angle)
	{
		entity.setFaceAngle((double) angle);
	}

	/**
	 * Get the direction the sprite is moving to
	 * @return the direction of the movement
	 */
	public double getMoveAngle()
	{
		return entity.getMoveAngle();
	}
	
	/**
	 * Set the direction the sprite is moving to
	 * @param angle the direction of the sprite movement
	 */
	public void setMoveAngle(double angle)
	{
		entity.setMoveAngle(angle);
	}
	
	/**
	 * Set the direction the sprite is moving to
	 * @param angle the direction of the sprite movement
	 */
	public void setMoveAngle(float angle)
	{
		entity.setMoveAngle((double) angle);
	}
	
	/**
	 * Set the direction the sprite is moving to
	 * @param angle the direction of the sprite movement
	 */
	public void setMoveAngle(int angle)
	{
		entity.setMoveAngle((double) angle);
	}

	/**
	 * Get the width of the image that represent the sprite
	 * @return the width of the image
	 */
	public int getImageWidth()
	{
		return entity.getWidth();
	}
	
	/**
	 * Get the height of the image that represent the sprite
	 * @return the height of the image
	 */
	public int getImageHeight()
	{
		return entity.getHeight();
	}

	/**
	 * Check for collision
	 * @param rect the rectangle to check collisions with
	 * @return if the sprite collides with rect
	 */
	public boolean collidesWith(Rectangle rect)
	{
		return (rect.intersects(getBounds()));
	}
	
	/**
	 * Check for collision
	 * @param sprite the sprite to check collisions with
	 * @return if the sprite collides with the other sprite
	 */
	public boolean collidesWith(Sprite sprite)
	{
		return (getBounds().intersects(sprite.getBounds()));
	}
	
	/**
	 * Check for collision
	 * @param point the point to check collisions with
	 * @return if the sprite contains the point
	 */
	public boolean collidesWith(Point2D point)
	{
		return (getBounds().contains(point.X(), point.Y()));
	}

	/**
	 * Get the applet that contains the sprite
	 * @return the applet
	 */
	public Applet getApplet()
	{
		return entity.applet;
	}
	
	/**
	 * Get the graphics that draws the sprite
	 * @return the graphics
	 */
	public Graphics2D getGraphics()
	{
		return entity.graphics2d;
	}
	
	/**
	 * Get the image that represents the sprite
	 * @return the image that represent the sprite
	 */
	public Image getImage()
	{
		return entity.image;
	}
	
	/**
	 * Set the image that represent the sprite
	 * @param image the image to associate with the sprite
	 */
	public void setImage(Image image)
	{
		entity.setImage(image);
	}

	/**
	 * Get the sprite type
	 * @return the sprite type
	 */
	public int getSpriteType()
	{
		return sprType;
	}
	
	/**
	 * Set the sprite type
	 * @param type the sprite type
	 */
	public void setSpriteType(int type)
	{
		sprType = type;
	}

	/**
	 * Get if the sprite has collided
	 * @return if the sprite has collided
	 */
	public boolean hasCollided()
	{
		return collided;
	}
	
	/**
	 * Set if the sprite has collided or not
	 * @param collide if the sprite has collided or not
	 */
	public void setCollided(boolean collide)
	{
		collided = collide;
	}

	/**
	 * Get the life span of the sprite
	 * @return the life span of the sprite
	 */
	public int getLifespan()
	{
		return lifespan;
	}
	
	/**
	 * Set the life span of the sprite
	 * @param life the life span
	 */
	public void setLifespan(int life)
	{
		lifespan = life;
	}
	
	/**
	 * Get the life age of the sprite
	 * @return the life age
	 */
	public int getLifeage()
	{
		return lifeage;
	}
	
	/**
	 * Set the life age of the sprite
	 * @param age the age of the sprite
	 */
	public void setLifeage(int age)
	{
		lifeage = age;
	}
	
	/**
	 * Update the life age of the sprite, if it's greater than the life span
	 * set alive status to false
	 */
	public void updateLifetime()
	{
		if (lifespan > 0) {
			lifeage++;
			if (lifeage > lifespan) {
				setAlive(false);
				lifeage = 0;
			}
		}
	}

}
