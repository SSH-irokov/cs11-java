package lab1;

/**
 * A three-dimensional point class.
 */
public class Point3d {
    
    /** X coordinate of the point */
    private double xCoord;
    
    /** Y coordinate of the point */
    private double yCoord;
    
    /** Z coordinate of the point */
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. */
    public Point3d(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. */
    public Point3d() {
        // Call three-argument constructor and specify the origin.
        this(0, 0, 0);
    }

    /** Return the X coordinate of the point. */
    public double getX() {
        return xCoord;
    }

    /** Return the Y coordinate of the point. */
    public double getY() {
        return yCoord;
    }
    
    /** Return the Z coordinate of the point. */
    public double getZ() {
        return zCoord;
    }

    /** Set the X coordinate of the point. */
    public void setX(double val) {
        xCoord = val;
    }

    /** Set the Y coordinate of the point. */
    public void setY(double val) {
        yCoord = val;
    }
    
    /** Set the Z coordinate of the point. */
    public void setZ(double val) {
        zCoord = val;
    }
    
    public boolean equals(Object obj) 
    { 
        // instanceof test an object’s type
        // Defined to return false if the reference is null 
        if (obj instanceof Point3d) 
        {
            // Cast other object to Point3d type, then compare. 
            Point3d other = (Point3d) obj; 
            
            if (xCoord == other.getX() && 
                yCoord == other.getY() &&
                zCoord == other.getZ()) 
            { 
                    return true; 
            } 
        } 
        
        return false; 
    } 
    
    /** computes a double-precision floating-point approximation 
        of the straight-line distance between the two points */
    public double distanceTo(Point3d other)
    {
        // if other is null return invalid value
        if(other == null){
            return -1;
        }
        
        double dist = 0;
        
        dist += Math.pow(xCoord - other.getX() ,2);
        dist += Math.pow(yCoord - other.getY() ,2);
        dist += Math.pow(zCoord - other.getZ() ,2);
        
        dist = Math.sqrt(dist);
        
        return dist;
    }
    
}
