package lab1;

import java.io.*;


public class Lab1 {

    public static void main(String[] args)
    {
        Point3d [] points = new Point3d[3]; 
        
        for(int i = 0; i < 3 ; ++i)
        {
            System.out.println("input point" + i + " :");
            
            System.out.print("x" + i + " = ");
            double x = getDouble();
            
            System.out.print("y" + i + " = ");
            double y = getDouble();
            
            System.out.print("z" + i + " = ");
            double z = getDouble();
            
            points[i] = new Point3d(x,y,z);
        }
        
        System.out.println();
        System.out.println("the area is " + computeArea(points[0], points[1], points[2]));
    }
    
    /** takes three Point3d's and computes the area within the triangle bounded by them */
    public static double computeArea(Point3d p1, Point3d p2, Point3d p3) 
    {
        if(p1 == null || p2 == null || p3 == null)
        {
            System.err.println("Error : Lab1.computeArea , null value");
            return -1;
        }
        
        boolean equal1 = p1.equals(p2);
        boolean equal2 = p1.equals(p3);
        boolean equal3 = p2.equals(p3);
        
        if(equal1 || equal2 || equal3)
        {
            String err = equal1 ? "p1 equals p2" : (equal2 ? "p1 equals p3" : "p2 equals p3");
            System.err.println("Error : Lab1.computeArea , " + err);
            return -1;
        }
        
        double d_12 = p1.distanceTo(p2);
        double d_13 = p1.distanceTo(p3);
        double d_23 = p2.distanceTo(p3);
        
        double s = (d_12 + d_13 + d_23) / 2.0;
        
        double dist = s * (s - d_12) * (s - d_13) * (s - d_23);
        dist = Math.sqrt(dist);
        
        return dist;
    }
    
    /**
     * Obtains one double-precision floating point number from
     * standard input.
     *
     * @return return the inputted double, or 0 on error.
     */
    public static double getDouble() 
    {
        // There's potential for the input operation to "fail"; hard with a
        // keyboard, though!
        try 
        {
            // Set up a reader tied to standard input.
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Read in a whole line of text.
            String s = br.readLine();

            // Conversion is more likely to fail, of course, if there's a typo.
            try 
            {
                double d = Double.parseDouble(s);

                //Return the inputted double.
                return d;
            }
            catch (NumberFormatException e) 
            {
                // Bail with a 0.  (Simple solution for now.)
                return 0.0;
            }
        }
        catch (IOException e) 
        {
            // This should never happen with the keyboard, but we'll
            // conform to the other exception case and return 0 here,
            // too.
            return 0.0;
        }
    }

}
