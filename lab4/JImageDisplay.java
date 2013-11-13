package lab4;

import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{

    private BufferedImage _image;
    
    public JImageDisplay(int width, int height)
    {
        _image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);
    }
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        // We are passing null for the ImageObserver, since we don't need that functionality
        g.drawImage(_image, 0, 0, _image.getWidth(), _image.getHeight(), null);
    }
    
    /**
     * sets all pixels in the image data to black (RGB value 0)
     */
    public void clearImage()
    {
        for(int j = 0 ; j < _image.getHeight() ; ++j)
        {
            for(int i = 0 ; i < _image.getWidth() ; ++i)
            {
                this.drawPixel(i, j, 0);
            }
        }
    }
    
    public void drawPixel(int x, int y, int rgbColor)
    {
        _image.setRGB(x, y, rgbColor);
    }
    
    public BufferedImage getImage()
    {
        return _image;
    }
    
}
