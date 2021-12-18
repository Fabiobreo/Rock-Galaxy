package rockGalaxy;
/**
 * A representation of a two dimensional point
 * @author Ing. Fabio Brea
 * @version 1.1
 */
class Point2D extends Object
{
    private double x;
    private double y;

    /**
     * Create a point in 2D
     * @param x: the abscissa of the point
     * @param y: the ordinate of the point
     */
    public <T> Point2D(T x, T y)
    {
        setX(x);
        setY(y);
    }

    /**
     * Set the abscissa of the point
     * @param x: the abscissa of the point
     */
    public <T> void setX(T x)
    {
    	if (x instanceof Integer)
    	{
        	this.x = ((Integer) x).doubleValue();
    	}
    	else if (x instanceof Float)
    	{
    		this.x = ((Float) x).doubleValue();
    	}
    	else if (x instanceof Double)
    	{
    		this.x = ((Double) x).doubleValue();
    	}
    }
    
    /**
     * Set the ordinate of the point
     * @param y: the ordinate of the point
     */
    public <T> void setY(T y)
    {
    	if (y instanceof Integer)
    	{
        	this.y = ((Integer) y).doubleValue();
    	}
    	else if (y instanceof Float)
    	{
    		this.y = ((Float) y).doubleValue();
    	}
    	else if (y instanceof Double)
    	{
    		this.y = ((Double) y).doubleValue();
    	}
    }

    /**
     * Get the value of the abscissa of the point
     * @return the abscissa of the point
     */
    public double X()
    {
    	return x;
    }
    
    /**
     * Get the value of the ordinate of the point
     * @return the ordinate of the point
     */
    public double Y()
    {
    	return y;
    }
}


