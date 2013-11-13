package lab3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FractalExplorer {
    
    /**
     *  the width and height of the display in pixels. (Our fractal display will be square.)
     */
    private int _displaySize;
    
    /**
     *   reference, so that we can update our display from various methods, as we compute the fractal
     */
    private JImageDisplay _image;
    
    /**
     *   base-class reference so that we can show other kinds of fractals in the future
     */
    private FractalGenerator _gen;
    
    /**
     *  specifying the range of the complex plane that we are currently displaying
     */
    Rectangle2D.Double _range;

    private class ResetHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        { 
            _range = new Rectangle2D.Double();
            _gen.getInitialRange(_range);
            
            drawFractal();
        } 
    }
    
    private class MouseHandler extends MouseAdapter 
    { 
        public void mouseClicked(MouseEvent e)
        { 
            double xCoord = FractalGenerator.getCoord(_range.x, _range.x + _range.width, _displaySize, e.getX());
            double yCoord = FractalGenerator.getCoord(_range.y, _range.y + _range.height, _displaySize, e.getY());
            
            _gen.recenterAndZoomRange(_range,xCoord, yCoord, 0.5);
            
            drawFractal();
        } 
    }
    
    public FractalExplorer(int displaySize)
    {
        _displaySize = displaySize;
        
        _gen = new Mandelbrot();
        
        _range = new Rectangle2D.Double();
        _gen.getInitialRange(_range);
    }
    
    public void createAndShowGUI()
    {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());

        _image = new JImageDisplay(_displaySize, _displaySize);
        contentPane.add(_image, BorderLayout.CENTER);
        
        JButton resetButton = new JButton("Reset Display");
        resetButton.addActionListener(new ResetHandler());
        contentPane.add(resetButton, BorderLayout.SOUTH);
        
        contentPane.addMouseListener(new MouseHandler());
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    public void drawFractal()
    {
        double xCoord = 0;
        double yCoord = 0;
        
        float numIters = 0;
        float hue = 0;
        
        int rgbColor = 0;
        
        for(int x = 0 ; x < _displaySize ; ++x)
        {
            // x is the pixel-coordinate; xCoord is the coordinate in the fractal's space
            xCoord = FractalGenerator.getCoord(_range.x, _range.x + _range.width, _displaySize, x);
            
            for(int y = 0 ; y < _displaySize ; ++y)
            {
                // y is the pixel-coordinate; yCoord is the coordinate in the fractal's space
                yCoord = FractalGenerator.getCoord(_range.y, _range.y + _range.height, _displaySize, y);
                
                numIters = _gen.numIterations(xCoord, yCoord);
                if(numIters < 0)
                {
                    rgbColor = 0;
                }
                else
                {
                    hue = 0.7f + numIters / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                
                _image.drawPixel(x, y, rgbColor);
            }
        }
        
        _image.repaint();
    }
    
    /**
     * Entry-point for the application.  No command-line arguments are
     * recognized at this time.
     **/
    public static void main(String[] args) 
    {
        FractalExplorer explorer = new FractalExplorer (400);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    } 
}
