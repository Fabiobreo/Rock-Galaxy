package rockGalaxy;
/**
 * A basic representation of an entity
 * @author Ing. Fabio Brea
 * @version 1.1
 */
public class BaseGameEntity
{
    protected boolean alive;
    protected double x;
    protected double y;
    protected double velX;
    protected double velY;
    protected double moveAngle;
    protected double faceAngle;

    /**
     * Constructor
     */
    BaseGameEntity()
    {
        setAlive(false);
        setX(0.0);
        setY(0.0);
        setVelX(0.0);
        setVelY(0.0);
        setMoveAngle(0.0);
        setFaceAngle(0.0);
    }
    
    /**
     * Return if the entity is alive
     * @return if the entity is alive
     */
    public boolean isAlive()
    {
    	return alive;
    }
    
    /**
     * Get the abscissa of the entity
     * @return the abscissa of the entity
     */
    public double getX()
    {
    	return x;
    }
    
    /**
     * Get the ordinate of the entity
     * @return the ordinate of the entity
     */
    public double getY()
    {
    	return y;
    }
    
    /**
     * Get the x component of the velocity of the entity
     * @return the x component of the velocity of the entity
     */
    public double getVelX()
    {
    	return velX;
    }
    
    /**
     * Get the y component of the velocity of the entity
     * @return the y component of the velocity of the entity
     */
    public double getVelY()
    {
    	return velY;
    }
    
    /**
     * Get the direction of the movement for the entity
     * @return the direction of the movement for the entity
     */
    public double getMoveAngle()
    {
    	return moveAngle;
    }
    
    /**
     * Get the direction the entity is facing
     * @return the direction the entity is facing
     */
    public double getFaceAngle()
    {
    	return faceAngle;
    }
    
    
    /**
     * Set if the entity is alive
     * @param alive
     */
    public void setAlive(boolean alive)
    {
    	this.alive = alive;
    }
    
    /**
     * Set the abscissa of the entity
     * @param x the abscissa of the entity
     */
    public void setX(double x)
    {
    	this.x = x;
    }
    
    /**
     * Move the abscissa of the entity of an i quantity
     * @param i the shifting quantity
     */
    public void incX(double i)
    {
    	this.x += i;
    }
    
    /**
     * Set the ordinate of the entity
     * @param y the ordinate of the entity
     */
    public void setY(double y)
    {
    	this.y = y;
    }
    
    /**
     * Move the ordinate of the entity of an i quantity
     * @param i the shifting quantity
     */
    public void incY(double i)
    {
    	this.y += i;
    }
    
    /**
     * Set the abscissa component of the entity's velocity
     * @param velX the velocity in X
     */
    public void setVelX(double velX)
    {
    	this.velX = velX;
    }
    
    /**
     * Increment the velocity in X of an i quantity
     * @param i the increment
     */
    public void incVelX(double i)
    {
    	this.velX += i;
    }
    
    /**
     * Set the ordinate component of the entity's velocity
     * @param velY: the velocity in Y
     */
    public void setVelY(double velY)
    {
    	this.velY = velY;
    }
    
    /**
     * Increment the velocity in Y of an i quantity
     * @param i the increment
     */
    public void incVelY(double i)
    {
    	this.velY += i;
    }
    
    /**
     * Set the direction that the entity is facing
     * @param angle the direction of the entity
     */
    public void setFaceAngle(double angle)
    {
    	this.faceAngle = angle;
    }
    
    /**
     * Change the facing direction of an i quantity
     * @param i the increment
     */
    public void incFaceAngle(double i)
    {
    	this.faceAngle += i;
    }
    
    /**
     * Set the direction of the movement of the entity
     * @param angle the direction
     */
    public void setMoveAngle(double angle)
    {
    	this.moveAngle = angle;
    }
    
    /**
     * Change the moving direction of an i quantity
     * @param i the increment
     */
    public void incMoveAngle(double i)
    {
    	this.moveAngle += i;
    }
}
