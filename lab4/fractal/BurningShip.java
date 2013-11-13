package lab4.fractal;

import java.awt.geom.Rectangle2D;

import lab4.FractalGenerator;

public class BurningShip extends FractalGenerator{

    public static final int MAX_ITERATIONS = 2000;
    
    /**
     * The initial range for this fractal should be (-2, -2.5) to (2, 1.5)
     * That is, the x and y values will be -2 and -2.5 respectively, 
     * and the width and height will both be 4
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2.5;
        
        range.width = 4;
        range.height = 4;
    }
    
    /**
     * For the Mandelbrot fractal, the function is z_n = (|Re(z_(n-1))| + i* |Im(z_(n-1))|)^2 + c, 
     * where all values are complex numbers, z_0 = 0, 
     * and c is the particular point in the fractal that we are displaying. 
     * This computation is iterated until either |z| > 2
     * or until the number of iterations hits a maximum value, e.g. 2000
     */
    public int numIterations(double x, double y)
    {
        int count = 0;
        
        double re = x;
        double im = y;
        
        double re2 = re*re;
        double im2 = im*im;
        
        double z_n2 = 0;
        
        while(count < MAX_ITERATIONS && z_n2 < 4)
        {
            im = Math.abs(2 * re * im) + y;
            re = (re2 - im2) + x;
            
            re2  = re*re;
            im2  = im*im;
            
            z_n2 = re2 + im2;
            ++count;
        }
        
        return count < MAX_ITERATIONS ? count : -1;
    }
    
    public static String nameString()
    {
        return "Burning Ship";
    }
    
}
