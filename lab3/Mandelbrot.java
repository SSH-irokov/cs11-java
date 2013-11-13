package lab3;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{

    public static final int MAX_ITERATIONS = 2000;
    
    /**
     * set the initial range to (-2 - 1.5i) - (1 + 1.5i). 
     * That is, the x and y values will be -2 and -1.5 respectively, 
     * and the width and height will both be 3
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -1.5;
        
        range.width = 3;
        range.height = 3;
    }
    
    /**
     * For the Mandelbrot fractal, the function is z_n = (z_(n-1))^2 + c, 
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
            im = (2 * re * im) + y;
            re = (re2 - im2) + x;
            
            re2  = re*re;
            im2  = im*im;
            
            z_n2 = re2 + im2;
            ++count;
        }
        
        return count < MAX_ITERATIONS ? count : -1;
    }
}
